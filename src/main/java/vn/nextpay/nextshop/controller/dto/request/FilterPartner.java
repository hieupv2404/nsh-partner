/*
 *  Created by: HieuPV
 *  Mail: hieupv@mpos.vn
 */

package vn.nextpay.nextshop.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.nextpay.nextshop.util.ValidateConstant;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterPartner {
    private String query ="";
    @NotBlank(message = ValidateConstant.REQUIRED_MERCHANT)
    private String merchantId;
    private Integer person = 2;
    private String sort = "";
}
