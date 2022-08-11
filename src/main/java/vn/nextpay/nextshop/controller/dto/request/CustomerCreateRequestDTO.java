/*
 *  Created by: HieuPV
 *  Mail: hieupv@mpos.vn
 */

package vn.nextpay.nextshop.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCreateRequestDTO implements Serializable {
    @NotBlank(message = "Required name!")
    private String name;
    @NotBlank(message = "Required phone!")
    private String phoneNo;
    @NotBlank(message = "Required address!")
    private String address;
    @Length(max = 500, message = "Max 500 characters")
    private String note;
    @NotBlank(message = "Required Merchant Id")
    private String merchantId;
    private String email;

}
