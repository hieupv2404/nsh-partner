/*
 *  Created by: HieuPV
 *  Mail: hieupv@mpos.vn
 */

package vn.nextpay.nextshop.service.impl;

import cloud.nextsol.core.models.Contact;
import cloud.nextsol.core.models.common.ArchiveReqObject;
import cloud.nextsol.core.models.common.ArchiveResponse;
import cloud.nextsol.core.models.request.partner.ArchiveRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.nextpay.nextshop.controller.dto.request.*;
import vn.nextpay.nextshop.controller.dto.response.CoreResponseObject;
import vn.nextpay.nextshop.dao.entity.Group;
import vn.nextpay.nextshop.dao.entity.Partner;
import vn.nextpay.nextshop.dao.repository.GroupRepository;
import vn.nextpay.nextshop.dao.repository.PartnerRepository;
import vn.nextpay.nextshop.enums.EResponseStatus;
import vn.nextpay.nextshop.enums.ResponseStatus;
import vn.nextpay.nextshop.enums.TypePartnerEnum;
import vn.nextpay.nextshop.exception.NextshopException;
import vn.nextpay.nextshop.service.PartnerService;
import vn.nextpay.nextshop.util.PartnerConnectAPI;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class PartnerServiceImpl implements PartnerService {

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private MongoOperations mongoTemplate;

    @Autowired
    private PartnerConnectAPI partnerConnectAPI;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CoreResponseObject<Partner> createPartner(PartnerCreateDTO partnerCreateDTO, String type) {
        log.info("===== Create " + type);
        Optional<Partner> partnerOptional = partnerRepository.findPartnerByPhoneNoAndIsArchiveAndRelationshipTypesAndMerchantId(partnerCreateDTO.getPhoneNo(), false, type, partnerCreateDTO.getMerchantId());
        if (partnerOptional.isPresent()) {
            log.info("===== Phone Existed");
            return new CoreResponseObject<>(true, ResponseStatus.PHONE_EXISTED, partnerOptional.get());
        } else {
            List<Group> groupList = new ArrayList<>();
            if (partnerCreateDTO.getGroups() != null) {
                partnerCreateDTO.getGroups().forEach(groupId -> {
                    Optional<Group> groupOptional = groupRepository.findGroupByIdAndIsArchiveAndTypeOrNameAndIsArchiveAndType(groupId, false, type, groupId, false, type);
//                    Optional<Group> groupOptional = groupRepository.findGroupByIdOrNameWithIsArchiveAndType(groupId, type);
                    if (!groupOptional.isPresent()) {
                        log.info("==== Group not found");
                        throw new NextshopException(EResponseStatus.GROUP_NOT_FOUND, "GROUP_NOT_FOUND");
                    } else if (!groupOptional.get().getMerchantId().equals(partnerCreateDTO.getMerchantId())) {
                        log.info("==== Group not found");
                        throw new NextshopException(EResponseStatus.GROUP_NOT_FOUND, "GROUP_NOT_FOUND");
                    } else {
                        groupList.add(groupOptional.get());
                    }
                });
            }
            Partner partner = Partner.builder()
                    .name(partnerCreateDTO.getName())
                    .phoneNo(partnerCreateDTO.getPhoneNo())
                    .email(partnerCreateDTO.getEmail())
                    .birthday(partnerCreateDTO.getBirthday())
                    .sex(partnerCreateDTO.getSex())
                    .cityCode(partnerCreateDTO.getCityCode())
                    .avatarUrl(partnerCreateDTO.getAvatarUrl())
                    .districtCode(partnerCreateDTO.getDistrictCode())
                    .wardCode(partnerCreateDTO.getWardCode())
                    .cityValue(partnerCreateDTO.getCityValue())
                    .districtValue(partnerCreateDTO.getDistrictValue())
                    .wardValue(partnerCreateDTO.getWardValue())
                    .address(partnerCreateDTO.getAddress())
                    .groups(groupList)
                    .contacts(partnerCreateDTO.getContacts())
                    .note(partnerCreateDTO.getNote())
                    .isPerson(partnerCreateDTO.isPerson())
                    .isArchive(false)
                    .merchantId(partnerCreateDTO.getMerchantId())
                    .relationshipTypes(Arrays.asList(type))
                    .createdAt(ZonedDateTime.now().truncatedTo(ChronoUnit.MILLIS))
                    .updatedAt(ZonedDateTime.now().truncatedTo(ChronoUnit.MILLIS))
                    .build();
            partner.setFullNameSort(partner.splitFullNameSort(partnerCreateDTO.getName()));
            Partner partnerCreated = partnerRepository.save(partner);
            log.info("===== Created " + type + " with ID: " + partnerCreated.getId());

//            new Thread(() -> {
//                syncNewPartnerToCore(partnerCreated);
//            }
//            ).start();
            syncNewPartnerToCore(partnerCreated);
            partnerCreated = getPartnerById(FilterPartner.builder()
                    .query(partnerCreated.getId())
                    .merchantId(partnerCreateDTO.getMerchantId())
                    .build()).getData();

            return new CoreResponseObject<>(true, ResponseStatus.CREATED, partnerCreated);
        }
    }

    public void syncNewPartnerToCore(Partner partnerFromService) {
        log.info("==== Start sync data to core: ");
        cloud.nextsol.core.models.Partner coPartner = partnerConnectAPI.saveToCore(partnerFromService);
        if (coPartner == null) {
            log.info("==== Sync Data to core Failed!, req= {}", partnerFromService);
            PartnerListDeleteDTO partnerListDeleteDTO = new PartnerListDeleteDTO();
            partnerListDeleteDTO.setPartnerIds(new HashSet<>(Arrays.asList(partnerFromService.getId())));
            partnerListDeleteDTO.setMerchantId(partnerFromService.getMerchantId());
            deletePartners(partnerListDeleteDTO, partnerFromService.getRelationshipTypes().get(0));
            throw  new NextshopException(EResponseStatus.FAILED_IN_AUTHENTICATION);
        } else {
            log.info("Sync Data to Core Successful with ID: " + coPartner.getId());
            Update update = Update.update("coPartnerId", coPartner.getId());
            update.set("version", partnerFromService.getVersion());
            update.set("coPartnerVersion", coPartner.getVersion());
            mongoTemplate.update(Partner.class)
                    .matching(where("id").is(partnerFromService.getId()))
                    .apply(update)
                    .withOptions(FindAndModifyOptions.options().returnNew(true))
                    .findAndModifyValue();
        }
    }

    @Override
    public CoreResponseObject<Partner> getPartnerById(FilterPartner filterPartner) {
        Optional<Partner> partnerOptional = partnerRepository.findAllByIdAndIsArchiveAndMerchantId(filterPartner.getQuery(), false, filterPartner.getMerchantId());
        if (!partnerOptional.isPresent()) {
            log.info("===== Partner not found");
            return new CoreResponseObject<>(true, ResponseStatus.PARTNER_NOT_FOUND, null);
        } else {
            return new CoreResponseObject<>(true, ResponseStatus.SUCCESSFUL, partnerOptional.get());
        }
    }

    @Override
    public CoreResponseObject<List<Partner>> getAllPartner(FilterPartner filterPartner, String type) {
        List<Partner> partnerList = new ArrayList<>();
        filterPartner.setQuery(filterPartner.getQuery().trim().toLowerCase());
        if (filterPartner.getPerson().equals(2)) {
            partnerList = partnerRepository.findAllByMerchantIdAndRelationshipTypesAndIsArchiveAndFullNameSortIsContainingOrMerchantIdAndRelationshipTypesAndIsArchiveAndPhoneNoIsContaining(filterPartner.getMerchantId(), type, false, filterPartner.getQuery(), filterPartner.getMerchantId(), type, false, filterPartner.getQuery());
        } else {
            boolean isPerson = false;
            if (filterPartner.getPerson().equals(0)) {
                partnerList = partnerRepository.findAllByMerchantIdAndRelationshipTypesAndIsArchiveAndFullNameSortIsContainingAndIsPersonOrMerchantIdAndRelationshipTypesAndIsArchiveAndPhoneNoIsContainingAndIsPerson(filterPartner.getMerchantId(), type, false, filterPartner.getQuery(), isPerson, filterPartner.getMerchantId(), type, false, filterPartner.getQuery(), isPerson);
            } else {
                partnerList = partnerRepository.findAllByMerchantIdAndRelationshipTypesAndIsArchiveAndFullNameSortIsContainingAndIsPersonOrMerchantIdAndRelationshipTypesAndIsArchiveAndPhoneNoIsContainingAndIsPerson(filterPartner.getMerchantId(), type, false, filterPartner.getQuery(), !isPerson, filterPartner.getMerchantId(), type, false, filterPartner.getQuery(), !isPerson);
            }
        }
        if (filterPartner.getSort() == null) {
            filterPartner.setSort("name");
        }
        if (filterPartner.getSort().equals("date")) {
            partnerList.sort(Comparator.comparing(Partner::getCreatedAt).reversed());
        } else {
            partnerList.sort(Comparator.comparing(Partner::getFullNameSort));
        }
        log.info("==== Get List Partner Done");
        return new CoreResponseObject<>(true, ResponseStatus.SUCCESSFUL, partnerList);

    }

    @Override
    public CoreResponseObject<Partner> updatePartner(String id, PartnerUpdateDTO partnerUpdateDTO, String type) {
        Optional<Partner> partnerOptional = partnerRepository.findAllByIdAndIsArchiveAndMerchantId(id, false, partnerUpdateDTO.getMerchantId());
        if (!partnerOptional.isPresent()) {
            log.info("==== Partner not found");
            return new CoreResponseObject<>(true, ResponseStatus.PARTNER_NOT_FOUND, null);
        } else if (!partnerOptional.get().getVersion().equals(partnerUpdateDTO.getVersion())) {
            log.info("==== Conflict Version");
            return new CoreResponseObject<>(true, ResponseStatus.CONFLICT_VERSION, null);
        } else if (!partnerOptional.get().getRelationshipTypes().contains(type)) {
            log.info("==== Wrong Relationship Type");
            return new CoreResponseObject<>(true, ResponseStatus.WRONG_RELATIONSHIP_TYPE, null);
        } else {
            Optional<Partner> partnerPhoneOptional = partnerRepository.findPartnerByPhoneNoAndIsArchiveAndRelationshipTypesAndMerchantId(partnerUpdateDTO.getPhoneNo(), false, type, partnerUpdateDTO.getMerchantId());
            if (partnerPhoneOptional.isPresent() && !partnerPhoneOptional.get().getId().equals(partnerOptional.get().getId())) {
                log.info("===== Phone Existed");
                return new CoreResponseObject<>(true, ResponseStatus.PHONE_EXISTED, partnerPhoneOptional.get());
            } else {
                Partner partnerOld = partnerOptional.get();
                partnerOld.setAddress(partnerUpdateDTO.getAddress());
                partnerOld.setAvatarUrl(partnerUpdateDTO.getAvatarUrl());
                partnerOld.setBirthday(partnerUpdateDTO.getBirthday());
                partnerOld.setCityCode(partnerUpdateDTO.getCityCode());
                partnerOld.setDistrictCode(partnerUpdateDTO.getDistrictCode());
                partnerOld.setWardCode(partnerUpdateDTO.getWardCode());
                partnerOld.setCityValue(partnerUpdateDTO.getCityValue());
                partnerOld.setWardValue(partnerUpdateDTO.getWardValue());
                partnerOld.setDistrictValue(partnerUpdateDTO.getDistrictValue());
                partnerOld.setUpdatedAt(ZonedDateTime.now().truncatedTo(ChronoUnit.MILLIS));
                partnerOld.setEmail(partnerUpdateDTO.getEmail());
                partnerOld.isPerson(partnerUpdateDTO.isPerson());
                partnerOld.setSex(partnerUpdateDTO.getSex());
                partnerOld.setName(partnerUpdateDTO.getName());
                partnerOld.setNote(partnerUpdateDTO.getNote());
                partnerOld.setPhoneNo(partnerUpdateDTO.getPhoneNo());
                partnerOld.setContacts(partnerUpdateDTO.getContacts());
                partnerOld.setFullNameSort(partnerOld.splitFullNameSort(partnerUpdateDTO.getName()));

                List<Group> groupList = new ArrayList<>();
                if (partnerUpdateDTO.getGroups() != null) {
                    for (String groupId : partnerUpdateDTO.getGroups()) {
                        Optional<Group> groupOptional = groupRepository.findGroupByIdAndIsArchiveAndTypeOrNameAndIsArchiveAndType(groupId, false, type, groupId, false, type);
                        if (!groupOptional.isPresent()) {
                            log.info("==== Group not found");
                            return new CoreResponseObject<>(true, ResponseStatus.GROUP_NOT_FOUND, null);
                        } else if (!groupOptional.get().getMerchantId().equals(partnerUpdateDTO.getMerchantId())) {
                            log.info("==== Group not found");
                            throw new NextshopException(EResponseStatus.GROUP_NOT_FOUND, "GROUP_NOT_FOUND");
                        } else {
                            groupList.add(groupOptional.get());
                        }
                    }
                }
                partnerOld.setGroups(groupList);
                log.info("==== Partner update done");
                Partner partnerUpdate = partnerRepository.save(partnerOld);
                new Thread(() -> {
                    syncUpdatePartnerToCore(partnerUpdate);
                }).start();

                return new CoreResponseObject<>(true, ResponseStatus.UPDATED, partnerUpdate);
            }
        }
    }

    public void syncUpdatePartnerToCore(Partner partnerFromService) {
        log.info("==== Start Update data to core: ");
        cloud.nextsol.core.models.Partner coPartner = partnerConnectAPI.updateToCore(partnerFromService);
        if (coPartner == null) {
            log.info("==== Sync Data to core Failed!, req= {}", partnerFromService);
            throw  new NextshopException(EResponseStatus.FAILED_IN_AUTHENTICATION);
        } else {
            log.info("Update Data to Core Successful with ID: " + coPartner.getId());
            Update update = Update.update("coPartnerId", coPartner.getId());
            update.set("version", partnerFromService.getVersion());
            update.set("coPartnerVersion", coPartner.getVersion());
            mongoTemplate.update(Partner.class)
                    .matching(where("id").is(partnerFromService.getId()))
                    .apply(update)
                    .withOptions(FindAndModifyOptions.options().returnNew(true))
                    .findAndModifyValue();
        }
    }

    public boolean checkPartnerPresent(boolean present) {
        if (!present) {
            log.info("==== Partner not found");
            return false;
        } else {
            return true;
        }
    }

    public boolean checkPartnerRelationshipType(Partner partner, String type) {
        if (!partner.getRelationshipTypes().contains(type)) {
            log.info("==== Wrong Partner's Relationship Type");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public CoreResponseObject<List<Partner>> deletePartners(PartnerListDeleteDTO partnerListDeleteDTO, String type) {
        List<Partner> partnerList = new ArrayList<>();
        for (String partnerId : partnerListDeleteDTO.getPartnerIds()) {
            Optional<Partner> partnerOptional = partnerRepository.findAllByIdAndIsArchiveAndMerchantId(partnerId, false, partnerListDeleteDTO.getMerchantId());
            if (!partnerOptional.isPresent()) {
                log.info("==== Partner not found");
                return new CoreResponseObject<>(true, ResponseStatus.PARTNER_NOT_FOUND, null);
            } else if (!partnerOptional.get().getRelationshipTypes().contains(type)) {
                log.info("==== Wrong Relationship Type");
                return new CoreResponseObject<>(true, ResponseStatus.WRONG_RELATIONSHIP_TYPE, null);
            } else {
                partnerOptional.get().setArchive(true);
                partnerOptional.get().setUpdatedAt(ZonedDateTime.now().truncatedTo(ChronoUnit.MILLIS));
                partnerList.add(partnerRepository.save(partnerOptional.get()));

            }
        }
        new Thread(() -> {
            syncDeletePartnerToCore(partnerList);
        }).start();
        log.info("==== Delete Partner Done");
        return new CoreResponseObject<>(true, ResponseStatus.DELETED, partnerList);
    }

    public void syncDeletePartnerToCore(List<Partner> partnerList) {
        log.info("==== Start Delete data in core: ");
        List<ArchiveReqObject> ids = new ArrayList<>();
        for (Partner partner : partnerList) {
            ids.add(ArchiveReqObject.builder()
                    .id(partner.getCoPartnerId())
                    .optimisticVersion(partner.getCoPartnerVersion())
                    .build());
        }
        ArchiveRequest request = ArchiveRequest.builder()
                .userId(System.getenv("NP_USER_ID"))
                .locationId(System.getenv("NP_LOCATION_ID"))
                .ids(ids)
                .build();

        ArchiveResponse response = partnerConnectAPI.deleteToCore(request);

        if (response == null) {
            log.info("==== Sync Data to core Failed!, req= {}", request);
            throw  new NextshopException(EResponseStatus.FAILED_IN_AUTHENTICATION);
        } else {
            log.info("Delete Data to Core Successful with List: " + request.getIds());
            response.getSuccess().forEach(success -> {
                for (Partner partner : partnerList) {
                    if (partner.getCoPartnerId().equals(success.getId())) {
                        Update update = Update.update("coPartnerId", success.getId());
                        update.set("version", partner.getVersion());
                        update.set("coPartnerVersion", success.getVersion());
                        mongoTemplate.update(Partner.class)
                                .matching(where("id").is(partner.getId()))
                                .apply(update)
                                .withOptions(FindAndModifyOptions.options().returnNew(true))
                                .findAndModifyValue();
                    }
                }
            });
        }
    }

    @Override
    public CoreResponseObject<Partner> updateContact(String partnerId, ContactCreateDTO contactCreateDTO) {
        Optional<Partner> partnerOptional = partnerRepository.findAllByIdAndIsArchive(partnerId, false);
        if (!partnerOptional.isPresent()) {
            log.info("==== Partner not found");
            return new CoreResponseObject<>(true, ResponseStatus.PARTNER_NOT_FOUND, null);
        } else if (!contactCreateDTO.getVersion().equals(partnerOptional.get().getVersion())) {
            log.info("==== Conflict Version");
            return new CoreResponseObject<>(true, ResponseStatus.CONFLICT_VERSION, null);
        } else {
            modelMapper.map(contactCreateDTO.getContacts(), Contact.class);
            partnerOptional.get().setContacts(contactCreateDTO.getContacts());

            Partner partner = partnerRepository.save(partnerOptional.get());
            new Thread(() -> {
                syncUpdatePartnerToCore(partner);
            }).start();

            return new CoreResponseObject<>(true, ResponseStatus.UPDATED, partner);
        }
    }

    @Override
    public CoreResponseObject<List<Partner>> getPartnersByMerchantId(FilterPartner filterPartner) {
        List<Partner> partnerList = new ArrayList<>();
        List<Partner> customerList = new ArrayList<>();
        List<Partner> supplierList = new ArrayList<>();
        filterPartner.setQuery(filterPartner.getQuery().trim().toLowerCase());
        // Customers
        if (filterPartner.getPerson().equals(2)) {
            customerList = partnerRepository.findAllByMerchantIdAndRelationshipTypesAndIsArchiveAndFullNameSortIsContainingOrMerchantIdAndRelationshipTypesAndIsArchiveAndPhoneNoIsContaining(filterPartner.getMerchantId(), TypePartnerEnum.CUSTOMER.toString(), false, filterPartner.getQuery(), filterPartner.getMerchantId(), TypePartnerEnum.CUSTOMER.toString(), false, filterPartner.getQuery());
        } else {
            boolean isPerson = false;
            if (filterPartner.getPerson().equals(0)) {
                customerList = partnerRepository.findAllByMerchantIdAndRelationshipTypesAndIsArchiveAndFullNameSortIsContainingAndIsPersonOrMerchantIdAndRelationshipTypesAndIsArchiveAndPhoneNoIsContainingAndIsPerson(filterPartner.getMerchantId(), TypePartnerEnum.CUSTOMER.toString(), false, filterPartner.getQuery(), isPerson, filterPartner.getMerchantId(), TypePartnerEnum.CUSTOMER.toString(), false, filterPartner.getQuery(), isPerson);
            } else {
                customerList = partnerRepository.findAllByMerchantIdAndRelationshipTypesAndIsArchiveAndFullNameSortIsContainingAndIsPersonOrMerchantIdAndRelationshipTypesAndIsArchiveAndPhoneNoIsContainingAndIsPerson(filterPartner.getMerchantId(), TypePartnerEnum.CUSTOMER.toString(), false, filterPartner.getQuery(), !isPerson, filterPartner.getMerchantId(), TypePartnerEnum.CUSTOMER.toString(), false, filterPartner.getQuery(), !isPerson);
            }
        }
        if (filterPartner.getSort() == null) {
            filterPartner.setSort("name");
        }
        if (filterPartner.getSort().equals("date")) {
            customerList.sort(Comparator.comparing(Partner::getCreatedAt).reversed());
        } else {
            customerList.sort(Comparator.comparing(Partner::getFullNameSort));
        }
        partnerList.addAll(customerList);

        // Suppliers
        if (filterPartner.getPerson().equals(2)) {
            supplierList = partnerRepository.findAllByMerchantIdAndRelationshipTypesAndIsArchiveAndFullNameSortIsContainingOrMerchantIdAndRelationshipTypesAndIsArchiveAndPhoneNoIsContaining(filterPartner.getMerchantId(), TypePartnerEnum.SUPPLIER.toString(), false, filterPartner.getQuery(), filterPartner.getMerchantId(), TypePartnerEnum.SUPPLIER.toString(), false, filterPartner.getQuery());
        } else {
            boolean isPerson = false;
            if (filterPartner.getPerson().equals(0)) {
                supplierList = partnerRepository.findAllByMerchantIdAndRelationshipTypesAndIsArchiveAndFullNameSortIsContainingAndIsPersonOrMerchantIdAndRelationshipTypesAndIsArchiveAndPhoneNoIsContainingAndIsPerson(filterPartner.getMerchantId(), TypePartnerEnum.SUPPLIER.toString(), false, filterPartner.getQuery(), isPerson, filterPartner.getMerchantId(), TypePartnerEnum.SUPPLIER.toString(), false, filterPartner.getQuery(), isPerson);
            } else {
                supplierList = partnerRepository.findAllByMerchantIdAndRelationshipTypesAndIsArchiveAndFullNameSortIsContainingAndIsPersonOrMerchantIdAndRelationshipTypesAndIsArchiveAndPhoneNoIsContainingAndIsPerson(filterPartner.getMerchantId(), TypePartnerEnum.SUPPLIER.toString(), false, filterPartner.getQuery(), !isPerson, filterPartner.getMerchantId(), TypePartnerEnum.SUPPLIER.toString(), false, filterPartner.getQuery(), !isPerson);
            }
        }
        if (filterPartner.getSort() == null) {
            filterPartner.setSort("name");
        }
        if (filterPartner.getSort().equals("date")) {
            supplierList.sort(Comparator.comparing(Partner::getCreatedAt).reversed());
        } else {
            supplierList.sort(Comparator.comparing(Partner::getFullNameSort));
        }
        partnerList.addAll(supplierList);
        log.info("==== Get List Partner Done");
        return new CoreResponseObject<>(true, ResponseStatus.SUCCESSFUL, partnerList);

    }

    @Override
    public CoreResponseObject<String> deletePartnerByMerchant(String merchantId) {
        List<Partner> partnerList = partnerRepository.findAllByIsArchiveAndMerchantId(false, merchantId);
        partnerList.forEach(partner -> {
            partner.setArchive(true);
            partnerRepository.save(partner);
        });
        new Thread(() -> {
            syncDeletePartnerToCore(partnerList);
        }).start();
        log.info("==== Delete Partner by Merchant Done");
        return new CoreResponseObject<>(true, ResponseStatus.SUCCESSFUL, "Delete partner by " + merchantId + " done!");

    }
}
