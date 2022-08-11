/*
 *  Created by: HieuPV
 *  Mail: hieupv@mpos.vn
 */

package vn.nextpay.nextshop.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import vn.nextpay.nextshop.util.ValidateConstant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupUpdateDTO {
    @NotBlank(message = ValidateConstant.REQUIRED_NAME)
    @Length(min = 1, max = 255, message = ValidateConstant.LENGTH_NAME)
    private String name;
    private String description;
    @NotNull(message = ValidateConstant.REQUIRED_VERSION)
    private Long version;
    @NotBlank(message = ValidateConstant.REQUIRED_MERCHANT)
    private String merchantId;

}
