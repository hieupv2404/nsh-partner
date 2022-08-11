/*
 *  Created by: HieuPV
 *  Mail: hieupv@mpos.vn
 */

package vn.nextpay.nextshop.controller.dto.request;

import cloud.nextsol.core.models.Contact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import vn.nextpay.nextshop.dao.entity.Partner;
import vn.nextpay.nextshop.enums.SexEnum;
import vn.nextpay.nextshop.util.ValidateConstant;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartnerCreateDTO implements Serializable {
    @NotBlank(message = ValidateConstant.REQUIRED_NAME)
    @Length(min = 1, max = 255, message = ValidateConstant.LENGTH_NAME)
    private String name;
    @NotBlank(message = ValidateConstant.REQUIRED_PHONE)
    @Pattern(regexp = ValidateConstant.PHONE_REGEX, message = ValidateConstant.WRONG_FORMAT_PHONE)
    private String phoneNo;
    @Pattern(regexp = ValidateConstant.EMAIL_REGEX, message = ValidateConstant.WRONG_FORMAT_EMAIL)
    @Length(min = 6, max = 255, message = ValidateConstant.LENGTH_EMAIL)
    private String email;
    private SexEnum sex;
    private String birthday;
    private boolean isPerson = true;
    private String avatarUrl;
    private String note;
    @NotBlank(message = ValidateConstant.REQUIRED_MERCHANT)
    private String merchantId;
    @Valid
    private Set<Partner.Contact> contacts = new HashSet<>(0);
    private Set<String> groups = new HashSet<>(0);
    @Length(min = 3, max = 255, message = ValidateConstant.LENGTH_ADDRESS)
    private String address;
    private String cityCode;
    private String cityValue;
    private String districtCode;
    private String districtValue;
    private String wardCode;
    private String wardValue;

    public ZonedDateTime getBirthday(){
        if(birthday == null){
            return null;
        }else {
            return ZonedDateTime.parse(birthday);
        }
    }

}
