/*
 *  Created by: HieuPV
 *  Mail: hieupv@mpos.vn
 */

package vn.nextpay.nextshop.util;

import cloud.nextsol.core.Environment;
import cloud.nextsol.core.NextpayClient;
import cloud.nextsol.core.api.PartnerApi;
import cloud.nextsol.core.exception.ApiException;
import cloud.nextsol.core.models.Contact;
import cloud.nextsol.core.models.Partner;
import cloud.nextsol.core.models.common.ArchiveResponse;
import cloud.nextsol.core.models.request.partner.ArchiveRequest;
import cloud.nextsol.core.models.request.partner.CreateNonPlatformPartnerRequest;
import cloud.nextsol.core.models.request.partner.UpdateRequest;
import cloud.nextsol.core.net.Headers;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import vn.nextpay.nextshop.security.SecurityUtils;

import java.io.IOException;
import java.util.*;

@Slf4j
public class PartnerConnectAPI {
    protected static final String userAgent = "Nextpay-Java-SDK/0.0.0.20201001";
    private PartnerApi partnerApi;
    private NextpayClient nextpayClient;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    private final static String NP_LOCATION_ID = "locationId";

    private final static String TOKEN_HOST_MASTER =
            "eyJhbGciOiJSUzI1NiJ9.eyJBUFBfUEFSRU5UX0lEIjoiNWVmZWYyMGM0NTIxZmQzYWFhYmY0MTk3IiwiUEVSTUlTU0lPTlMiOltdLCJDSEFOTkVMIjoid2ViIiwiVVNFUl9JRCI6IjYxNjkzNjYzY2UwZTkwMDAxODY4ZGQyNiIsImV4cCI6MTY2NjQ4ODM0OCwiaWF0IjoxNjM0OTUyMzQ4LCJNRVJDSEFOVF9JRCI6IjYxNjkzNmM1NTFmNWI4MDAxMTJmMzA0NSJ9.Xy2Zlb_uMWALQl7Dy2ORomKSX4T24EZOdTS-7sOUq36T_Ss4hMKFH_647kyfTsOipalS9Ed1-NoGFhi_6FsYEjc0UKM1-UuuXOjmeUBwcbP5_igIToNipaxtwKxRaoL8A3V1cFUowdKnu7AUqbsmaB0NxAyJv0473X1_LB5wadU";


    public PartnerConnectAPI() {
    }


    public Partner saveToCore(vn.nextpay.nextshop.dao.entity.Partner partnerFromService) {
        this.setupConnection();
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setSkipNullEnabled(true);

        Map<String, String> note = new HashMap<>();
        note.put("note", partnerFromService.getNote());

//        Contact contact = modelMapper.map(partnerFromService.getContacts(), Contact.class);
        List<Contact> contact = new ArrayList<>();
        if (partnerFromService.getContacts() != null) {
            for (vn.nextpay.nextshop.dao.entity.Partner.Contact contact1 : partnerFromService.getContacts()) {
                Contact contact2 = new Contact();
                contact2.setEmail(contact1.getEmail());
                contact2.setName(contact1.getName());
                contact2.setPhone(contact1.getPhone());
                contact2.setRule(contact1.getRule());
                contact.add(contact2);
            }
        }

        String NP_USER_ID = SecurityUtils.getCurrentActor().get().getUserId();
        String NP_APP_ID = SecurityUtils.getCurrentActor().get().getAppParentId();
        String MERCHANT_ID = SecurityUtils.getCurrentActor().get().getMerchantId();

        CreateNonPlatformPartnerRequest partnerToCore = CreateNonPlatformPartnerRequest.builder()
                .userId(NP_USER_ID)
                .locationId("localtionID")
                .displayName(partnerFromService.getName())
                .iconUrl(partnerFromService.getAvatarUrl())
                .name(partnerFromService.getName())
                .address(partnerFromService.getAddress())
                .phoneNo(partnerFromService.getPhoneNo())
                .email(partnerFromService.getEmail())
                .isPerson(partnerFromService.isPerson())
                .contact(contact)
                .faceId("faceID")
                .fingerprintId("fingerprintID")
                .metaData(note)
                .isUnknown(true)
                .onlyVisibleToCreatorApp(false)
                .objectClass("objectClass")
                .objectName("objectName")
                .appSubClass("appSubClass")
                .appKey("appKey")
                .appSubKey("appSubKey")
                .build();
        try {
            Partner res = partnerApi.createNonPlatformPartner(partnerToCore, getHeader(MERCHANT_ID));
            log.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            log.info("################## [CREATE NON PLATFORM PARTNER SUCCESS] #######################");
            log.info("-----------------------------------------------------------------------------");
            log.info("ACCESS TOKEN: " + SecurityUtils.getCurrentUserJWT().orElse(TOKEN_HOST_MASTER));
            log.info("-----------------------------------------------------------------------------");
            log.info("ENVIRONMENT: " + Environment.valueOf(System.getenv("ENVIRONMENT")));
            log.info("-----------------------------------------------------------------------------");
            log.info("CUSTOME BASE URI: " + System.getenv("CORE_API_URL"));
            log.info("-----------------------------------------------------------------------------");
            log.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            log.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            return res;
        } catch (Exception e) {
            log.info("############################################################################");
            log.info("################## [CREATE NON PLATFORM PARTNER ERROR] #######################");
            log.info("-----------------------------------------------------------------------------");
            log.info("ACCESS TOKEN: " + SecurityUtils.getCurrentUserJWT().orElse(TOKEN_HOST_MASTER));
            log.info("-----------------------------------------------------------------------------");
            log.info("ENVIRONMENT: " + Environment.valueOf(System.getenv("ENVIRONMENT")));
            log.info("-----------------------------------------------------------------------------");
            log.info("CUSTOME BASE URI: " + System.getenv("CORE_API_URL"));
            log.info("-----------------------------------------------------------------------------");
            log.info("ERROR: " + e);
            log.info("-----------------------------------------------------------------------------");
            e.printStackTrace();
            log.info("-----------------------------------------------------------------------------");
            log.info("############################################################################");
            log.info("############################################################################");
        }
        return null;

    }

    private void setupConnection() {
        this.nextpayClient = new NextpayClient.Builder()
                .environment(Environment.valueOf(System.getenv("ENVIRONMENT")))
                .customBaseUri(System.getenv("CORE_API_URL"))
                // SDK yêu cầu token not nul, set default để đủ điều kiện call qua anonymous
                .accessToken(SecurityUtils.getCurrentUserJWT().orElse(TOKEN_HOST_MASTER))
                .build();
        this.partnerApi = nextpayClient.getPartnerApi();
    }

    public Headers getHeader(String merchantId) {

        Headers headers = getBaseHeader();
        headers.add("np-merchant-id", merchantId);

        String token = SecurityUtils.getCurrentUserJWT().orElse(null);
        if (token == null) {
            headers.add("np-anonymous", "true");
        }

        return headers;
    }

    public Headers getBaseHeader() {
        String NP_USER_ID = SecurityUtils.getCurrentActor().get().getUserId();
        String NP_APP_ID = SecurityUtils.getCurrentActor().get().getAppParentId();
        String MERCHANT_ID = SecurityUtils.getCurrentActor().get().getMerchantId();
        Headers headers = new Headers();
        headers.add("user-agent", userAgent);
        headers.add("accept", "application/json");
        headers.add("content-type", "application/json");
        headers.add("np-user-id", NP_USER_ID);
        headers.add("np-location-id", NP_LOCATION_ID);
        headers.add("np-app-id", NP_APP_ID);
        return headers;
    }


    public Partner updateToCore(vn.nextpay.nextshop.dao.entity.Partner partnerFromService) {
        this.setupConnection();
        String NP_USER_ID = SecurityUtils.getCurrentActor().get().getUserId();
        String NP_APP_ID = SecurityUtils.getCurrentActor().get().getAppParentId();
        String MERCHANT_ID = SecurityUtils.getCurrentActor().get().getMerchantId();
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setSkipNullEnabled(true);

        Map<String, String> note = new HashMap<>();
        note.put("note", partnerFromService.getNote());

        List<Contact> contact = new ArrayList<>();
        if (partnerFromService.getContacts() != null) {
            for (vn.nextpay.nextshop.dao.entity.Partner.Contact contact1 : partnerFromService.getContacts()) {
                Contact contact2 = new Contact();
                contact2.setEmail(contact1.getEmail());
                contact2.setName(contact1.getName());
                contact2.setPhone(contact1.getPhone());
                contact2.setRule(contact1.getRule());
                contact.add(contact2);
            }
        }
//        Contact contact = modelMapper.map(new vn.nextpay.nextshop.dao.entity.Partner.Contact(), Contact.class);
        UpdateRequest partnerToCore = UpdateRequest.builder()
                .id(partnerFromService.getCoPartnerId())
                .displayName(partnerFromService.getName())
                .iconUrl(partnerFromService.getAvatarUrl())
                .name(partnerFromService.getName())
                .address(partnerFromService.getAddress())
                .phoneNo(partnerFromService.getPhoneNo())
                .email(partnerFromService.getEmail())
                .contact(contact)
                .metaData(note)
                .optimisticVersion(partnerFromService.getCoPartnerVersion())
                .build();
        try {
            return partnerApi.update(partnerToCore, getHeader(MERCHANT_ID));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ApiException e) {
            log.info("Call Update partner to core ERROR", e);
        }
        return null;
    }

    public ArchiveResponse deleteToCore(ArchiveRequest request) {
        this.setupConnection();
        String NP_USER_ID = SecurityUtils.getCurrentActor().get().getUserId();
        String NP_APP_ID = SecurityUtils.getCurrentActor().get().getAppParentId();
        String MERCHANT_ID = SecurityUtils.getCurrentActor().get().getMerchantId();
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setSkipNullEnabled(true);

        try {
            return partnerApi.archive(request, getHeader(MERCHANT_ID));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ApiException e) {
            log.info("Call Delete partner to core ERROR", e);
        }
        return null;
    }
}
