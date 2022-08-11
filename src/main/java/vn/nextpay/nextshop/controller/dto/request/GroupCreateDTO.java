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

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupCreateDTO {
    @NotBlank(message = "Required Name")
    @Length(min = 1, max = 255, message = ValidateConstant.LENGTH_NAME)
    private String name;
    private String description;
    private boolean isDefaultGroup = false;
    private Long priority;
    @NotBlank(message = ValidateConstant.REQUIRED_MERCHANT)
    private String merchantId;

}
