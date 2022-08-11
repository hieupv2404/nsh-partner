/*
 *  Created by: HieuPV
 *  Mail: hieupv@mpos.vn
 */

package vn.nextpay.nextshop.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.nextpay.nextshop.util.ValidateConstant;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartnerListDeleteDTO {
    private Set<String> partnerIds = new HashSet<>(0);
    @NotBlank(message = ValidateConstant.REQUIRED_MERCHANT)
    private String merchantId;

}
