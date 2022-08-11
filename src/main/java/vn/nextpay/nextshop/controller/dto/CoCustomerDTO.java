package vn.nextpay.nextshop.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;


public class CoCustomerDTO {
    public MetaData metaData;
    public String id;
    public String displayName;
    public String iconUrl;
    public String name;
    public String address;
    public String phoneNo;
    public String email;
    public ZonedDateTime lastActivityAt;
    public String lastActivityDetail;
    public String faceId;
    public String fingerprintId;
    public boolean isPerson;
    public boolean isUnknown;
    public boolean isArchived;
    public String platformObject;
    public String platformObjectId;
    public boolean onlyVisibleToCreatorApp;
    public String locationToCreate;
    public String appToCreate;
    public String objectClass;
    public String objectName;
    public String appSubClass;
    public String appKey;
    public String appSubKey;
    public String owner;
    public int version;
    public ZonedDateTime createdAt;
    public String createdBy;
    public ZonedDateTime updatedAt;
    public String updatedBy;
    public String dataVersion;

    public List<Contact> contact;
    public List<String> lockedBy;
    public List<FaceUrl> faceUrl;
    public List<FingerPrintUrl> fingerprintUrl;
    public List<RelationshipType> relationshipType;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Contact {
        private String name;
        private String phone;
        private String email;
        private String rule;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Relationship {
        private String relationshipType;
        private String registeredBy;
        private String signature;
        private MetaData metaData;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RelationshipType {
        private String type;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FaceUrl {
        private String id;
        private String label;
        private String url;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FingerPrintUrl {
        private String id;
        private String label;
        private String url;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MetaData {
        public String note;
    }


}

