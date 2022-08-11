/*
 *  Created by: HieuPV
 *  Mail: hieupv@mpos.vn
 */

package vn.nextpay.nextshop.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.nextpay.nextshop.controller.dto.request.FilterPartner;
import vn.nextpay.nextshop.controller.dto.request.GroupCreateDTO;
import vn.nextpay.nextshop.controller.dto.request.GroupDeleteDTO;
import vn.nextpay.nextshop.controller.dto.request.GroupUpdateDTO;
import vn.nextpay.nextshop.controller.dto.response.CoreResponseObject;
import vn.nextpay.nextshop.dao.entity.Group;
import vn.nextpay.nextshop.dao.entity.Partner;
import vn.nextpay.nextshop.dao.repository.GroupRepository;
import vn.nextpay.nextshop.dao.repository.PartnerRepository;
import vn.nextpay.nextshop.enums.ResponseStatus;
import vn.nextpay.nextshop.enums.TypePartnerEnum;
import vn.nextpay.nextshop.security.AuthorizationFilter;
import vn.nextpay.nextshop.service.GroupService;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    private static int groupDefaultSize;

    @Override
    public CoreResponseObject<List<Group>> getAllGroups(FilterPartner filterPartner, String type) {
        List<Group> groupList = groupRepository.findGroupByIsArchiveAndTypeAndMerchantId(false, type, AuthorizationFilter.MERCHANT_ID);
        if (type.equals(TypePartnerEnum.CUSTOMER.toString())) {
            groupDefaultSize = 4;
        } else {
            groupDefaultSize = 3;
        }
        List<Group> groupsFilter = groupList.stream()
                .filter(group -> group.isDefaultGroup())
                .collect(Collectors.toList());
        if (groupsFilter.isEmpty() || groupsFilter.size() != groupDefaultSize) {
            initGroup();
        }
        groupList = groupRepository.findGroupByIsArchiveAndTypeAndMerchantId(false, type, AuthorizationFilter.MERCHANT_ID);

//        List<Group> groupsFilter = groupList.stream()
//                .filter(group -> group.isDefaultGroup() || group.getMerchantId().equals(filterPartner.getMerchantId()))
//                .collect(Collectors.toList());
        groupList.sort(Comparator.comparing(Group::getPriority).thenComparing(Group::getCreatedAt).reversed());
        log.info("==== Get List Group Done");
        return new CoreResponseObject<>(true, ResponseStatus.SUCCESSFUL, groupList);
    }


    @Override
    public CoreResponseObject<Group> createGroup(GroupCreateDTO groupCreateDTO, String type) {
        Optional<Group> groupOptional = groupRepository.findGroupByNameAndIsArchiveAndTypeAndMerchantId(groupCreateDTO.getName(), false, type, groupCreateDTO.getMerchantId());
        if (groupOptional.isPresent() && groupOptional.get().getMerchantId().equals(groupCreateDTO.getMerchantId())) {
            log.info("==== Group Existed");
            return new CoreResponseObject<>(true, ResponseStatus.GROUP_EXISTED, groupOptional.get());
        } else {
            groupCreateDTO.setDefaultGroup(false);
            if (groupCreateDTO.isDefaultGroup()) {
                groupCreateDTO.setPriority(groupRepository.countByIsDefaultGroupAndIsArchiveAndType(true, false, type) + 1);
            } else {
                groupCreateDTO.setPriority(0L);
            }
            Group group = Group.builder()
                    .name(groupCreateDTO.getName())
                    .description(groupCreateDTO.getDescription())
                    .isArchive(false)
                    .type(type)
                    .isDefaultGroup(groupCreateDTO.isDefaultGroup())
                    .priority(groupCreateDTO.getPriority())
                    .merchantId(groupCreateDTO.getMerchantId())
                    .createdAt(ZonedDateTime.now().truncatedTo(ChronoUnit.MILLIS))
                    .build();
            group = groupRepository.save(group);
            log.info("==== Group " + type + " Created with ID: " + group.getId());
            return new CoreResponseObject<>(true, ResponseStatus.CREATED, group);
        }
    }

    @Override
    public CoreResponseObject<Group> getGroupById(String groupId) {
        Optional<Group> groupOptional = groupRepository.findGroupByIdAndIsArchive(groupId, false);
        if (!groupOptional.isPresent()) {
            log.info("==== Group Not Found");
            return new CoreResponseObject<>(true, ResponseStatus.GROUP_NOT_FOUND, null);
        } else {
            return new CoreResponseObject<>(true, ResponseStatus.SUCCESSFUL, groupOptional.get());
        }
    }

    @Override
    public CoreResponseObject<Group> updateGroup(String id, GroupUpdateDTO groupUpdateDTO, String type) {
        Optional<Group> groupOptional = groupRepository.findGroupByIdAndIsArchiveAndType(id, false, type);
        if (!groupOptional.isPresent()) {
            log.info("==== Group Not Found");
            return new CoreResponseObject<>(true, ResponseStatus.GROUP_NOT_FOUND, null);
        } else if (groupOptional.get().isDefaultGroup()) {
            log.info("==== Group Can Not Modified");
            return new CoreResponseObject<>(true, ResponseStatus.GROUP_CAN_NOT_MODIFY, null);
        } else if (!groupOptional.get().getVersion().equals(groupUpdateDTO.getVersion())) {
            log.info("==== Conflict Version");
            return new CoreResponseObject<>(true, ResponseStatus.CONFLICT_VERSION, null);
        } else {
            Optional<Group> groupNameOptional = groupRepository.findGroupByNameAndIsArchiveAndTypeAndMerchantId(groupUpdateDTO.getName(), false, type, groupUpdateDTO.getMerchantId());
            if (groupNameOptional.isPresent() && !groupNameOptional.get().getId().equals(groupOptional.get().getId()) && groupOptional.get().getMerchantId().equals(groupUpdateDTO.getMerchantId())) {
                log.info("==== Group Existed");
                return new CoreResponseObject<>(true, ResponseStatus.GROUP_EXISTED, groupNameOptional.get());
            } else {
                Group groupNew = groupOptional.get();
                groupNew.setName(groupUpdateDTO.getName());
                groupNew.setDescription(groupUpdateDTO.getDescription());

                groupNew = groupRepository.save(groupNew);
                log.info("==== Group Updated");
                return new CoreResponseObject<>(true, ResponseStatus.UPDATED, groupNew);
            }
        }
    }

    @Override
    public CoreResponseObject<List<Group>> deleteGroup(GroupDeleteDTO groupDeleteDTO, String type) {
        List<Group> groupListDeleted = new ArrayList<>();
        for (String groupId : groupDeleteDTO.getGroupIds()) {
            Optional<Group> groupOptional = groupRepository.findGroupByIdAndIsArchiveAndType(groupId, false, type);
            if (!groupOptional.isPresent()) {
                log.info("==== Group Not Found");
                return new CoreResponseObject<>(true, ResponseStatus.GROUP_NOT_FOUND, null);
            } else if (groupOptional.get().isDefaultGroup()) {
                log.info("==== Group Can Not Modified");
                return new CoreResponseObject<>(true, ResponseStatus.GROUP_CAN_NOT_MODIFY, null);
            } else {
                groupOptional.get().setArchive(true);
                groupListDeleted.add(groupRepository.save(groupOptional.get()));

            }
        }
        log.info("==== Group List Deleted");
        new Thread(() -> {
            removeGroupFromPartner(groupListDeleted, type);
        }).start();
        return new CoreResponseObject<>(true, ResponseStatus.DELETED, groupListDeleted);
    }

    private void removeGroupFromPartner(List<Group> groupListDeleted, String type) {
        for (Group group : groupListDeleted) {
            List<Partner> partnerList = partnerRepository.findPartnerByGroupId(group.getId(), type);
            log.info("Remove group with ID: " + group.getId());
            for (Partner partner : partnerList) {
                Iterator<Group> it = partner.getGroups().iterator();
                while (it.hasNext()) {
                    Group group1 = it.next();
                    if (group1.getId().equals(group.getId())) {
                        partner.getGroups().remove(group1);
                        partnerRepository.save(partner);
                        break;
                    }
                }
            }
        }
        log.info("==== Delete group in partner done");
    }

    @Override
    public CoreResponseObject<String> initGroup() {
        List<Group> groupList = groupRepository.findGroupByIsArchiveAndMerchantId(false, AuthorizationFilter.MERCHANT_ID);
        HashMap<String, Group> hashMap = new HashMap<>();
        for (Group group : groupList) {
            hashMap.put(group.getName(), group);
        }

        List<Group> groupsDefault = new ArrayList<>();
        Group group = Group.builder()
                .name("VIP")
                .merchantId(AuthorizationFilter.MERCHANT_ID)
                .priority(4L)
                .isArchive(false)
                .description("Nhóm Khách hàng VIP")
                .createdAt(ZonedDateTime.now())
                .isDefaultGroup(true)
                .type(TypePartnerEnum.CUSTOMER.toString())
                .build();
        groupsDefault.add(group);
        // ========
        group = Group.builder()
                .name("Mua Buôn")
                .merchantId(AuthorizationFilter.MERCHANT_ID)
                .priority(3L)
                .isArchive(false)
                .description("Nhóm Khách hàng Mua Buôn")
                .createdAt(ZonedDateTime.now())
                .isDefaultGroup(true)
                .type(TypePartnerEnum.CUSTOMER.toString())
                .build();
        groupsDefault.add(group);
        // ========
        group = Group.builder()
                .name("Mua Lẻ")
                .merchantId(AuthorizationFilter.MERCHANT_ID)
                .priority(2L)
                .isArchive(false)
                .description("Nhóm Khách hàng Mua Lẻ")
                .createdAt(ZonedDateTime.now())
                .isDefaultGroup(true)
                .type(TypePartnerEnum.CUSTOMER.toString())
                .build();
        groupsDefault.add(group);
        // ========
        group = Group.builder()
                .name("Khách Hàng Trung Thành")
                .merchantId(AuthorizationFilter.MERCHANT_ID)
                .priority(1L)
                .isArchive(false)
                .description("Nhóm Khách hàng Trung Thành")
                .createdAt(ZonedDateTime.now())
                .isDefaultGroup(true)
                .type(TypePartnerEnum.CUSTOMER.toString())
                .build();
        groupsDefault.add(group);
        // ========
        group = Group.builder()
                .name("Mặc Định")
                .merchantId(AuthorizationFilter.MERCHANT_ID)
                .priority(3L)
                .isArchive(false)
                .description("Nhóm Nhà cung cấp Mặc Định")
                .createdAt(ZonedDateTime.now())
                .isDefaultGroup(true)
                .type(TypePartnerEnum.SUPPLIER.toString())
                .build();
        groupsDefault.add(group);
        // ========
        group = Group.builder()
                .name("Bán Buôn")
                .merchantId(AuthorizationFilter.MERCHANT_ID)
                .priority(2L)
                .isArchive(false)
                .description("Nhóm Nhà cung cấp Bán Buôn")
                .createdAt(ZonedDateTime.now())
                .isDefaultGroup(true)
                .type(TypePartnerEnum.SUPPLIER.toString())
                .build();
        groupsDefault.add(group);
        // ========
        group = Group.builder()
                .name("Bán Lẻ")
                .merchantId(AuthorizationFilter.MERCHANT_ID)
                .priority(1L)
                .isArchive(false)
                .description("Nhóm Nhà cung cấp Bán Lẻ")
                .createdAt(ZonedDateTime.now())
                .isDefaultGroup(true)
                .type(TypePartnerEnum.SUPPLIER.toString())
                .build();
        groupsDefault.add(group);

        groupDefaultSize = groupsDefault.size();
        if (groupList.isEmpty()) {
            groupRepository.saveAll(groupsDefault);
        } else {
            Map<String, Group> groupsInit = new HashMap<>();
            for (Group group1 : groupsDefault) {
                groupsInit.put(group1.getName(), group1);
            }

            HashSet<String> unionKeys = new HashSet<>(hashMap.keySet());
            unionKeys.addAll(groupsInit.keySet());

            unionKeys.removeAll(hashMap.keySet());

            List<Group> groupNeedAdd = new ArrayList<>();
            for (String key : unionKeys) {
                groupNeedAdd.add(groupsInit.get(key));
            }
            groupRepository.saveAll(groupNeedAdd);
        }
        return new CoreResponseObject<>(true, ResponseStatus.SUCCESSFUL, "Init group for merchant " + AuthorizationFilter.MERCHANT_ID + " done!");

    }

    @Override
    public CoreResponseObject<String> deleteGroupByMerchant(String merchantId) {
        List<Group> groupList = groupRepository.findGroupByIsArchiveAndMerchantId(false, merchantId);
        groupList.forEach(group -> {
            group.setArchive(true);
            groupRepository.save(group);
        });
        return new CoreResponseObject<>(true, ResponseStatus.SUCCESSFUL, "Delete group by merchant " + AuthorizationFilter.MERCHANT_ID + " done!");
    }
}
