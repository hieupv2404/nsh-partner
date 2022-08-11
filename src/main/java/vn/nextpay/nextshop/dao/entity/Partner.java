/*
 *  Created by: HieuPV
 *  Mail: hieupv@mpos.vn
 */

package vn.nextpay.nextshop.dao.entity;

import cloud.nextsol.core.models.Contact;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import vn.nextpay.nextshop.enums.SexEnum;
import vn.nextpay.nextshop.util.ValidateConstant;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.io.Serializable;
import java.text.Normalizer;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("partner")
@Builder
public class Partner implements Serializable {
    @Id
    private String id;
//    @NotBlank(message = "Required Name")
    private String name;
    private String fullNameSort = "";
    private String phoneNo;
    private String email;
    @Enumerated(value = EnumType.STRING)
    private SexEnum sex;
    private ZonedDateTime birthday;
    private boolean isPerson;
    private boolean isArchive;
    private String avatarUrl = "";
    private String note;

    @Version
    private Long version;
    private Long coPartnerVersion;
    private String coPartnerId;
    @JsonIgnore
    @CreatedDate
    private ZonedDateTime createdAt;
    @JsonIgnore
    @CreatedBy
    private String createdBy;
    @JsonIgnore
    @LastModifiedDate
    private ZonedDateTime updatedAt;
    @JsonIgnore
    @LastModifiedBy
    private String updatedBy;
    private Set<Contact> contacts;
    private List<String> relationshipTypes;
    private List<Group> groups;
    private String address;
    private String cityCode;
    private String cityValue;
    private String wardCode;
    private String wardValue;
    private String districtCode;
    private String districtValue;
    private String merchantId;


    public void isPerson(boolean checkPerson) {
        isPerson = checkPerson;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Contact {
        @NotBlank(message = ValidateConstant.REQUIRED_NAME)
        @Length(min = 1, max = 255, message = ValidateConstant.LENGTH_NAME)
        private String name;
        @NotBlank(message = ValidateConstant.REQUIRED_PHONE)
        @Pattern(regexp = ValidateConstant.PHONE_REGEX, message = ValidateConstant.WRONG_FORMAT_PHONE)
        private String phone;
        private String email;
        private String rule;
    }

//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @Builder
//    public static class Relationship {
//        private String relationshipType;
//        private String registeredBy;
//        private String signature;
//        private CoCustomerDTO.MetaData metaData;
//    }

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
    public static class MetaData {
        private String note;
    }

    @Data
    public class ZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {
        public ZonedDateTimeDeserializer() {
        }

        @Override
        public ZonedDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {

            return ZonedDateTime.parse(jsonParser.getText(), DateTimeFormatter.ofPattern("dd/mm/yyyy"));
        }

    }

    public String getLastName() {
        String[] lastNameList = name.split(" ");
        return lastNameList[lastNameList.length - 1];
    }

    public String splitFullNameSort(String fullName) {
//        String nfdNormalizedString = Normalizer.normalize(fullName, Normalizer.Form.NFD);
//        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
//        fullName = pattern.matcher(nfdNormalizedString).replaceAll("");
        String[] lastNameList = fullName.toLowerCase().split(" ");
        StringBuilder nameToSort = new StringBuilder();
        nameToSort.append(lastNameList[lastNameList.length - 1]);
        for (int i = 0; i < lastNameList.length - 1; i++) {
            nameToSort.append(" ");
            nameToSort.append(lastNameList[i]);
        }
        return nameToSort.toString();


    }
}
