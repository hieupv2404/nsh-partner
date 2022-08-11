/*
 *  Created by: HieuPV
 *  Mail: hieupv@mpos.vn
 */

package vn.nextpay.nextshop.controller.dto.request;

import cloud.nextsol.core.models.Contact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import vn.nextpay.nextshop.controller.dto.CoCustomerDTO;
import vn.nextpay.nextshop.dao.entity.Partner;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactCreateDTO {
    @Valid
    private Set<Partner.Contact> contacts;
    @NotNull(message = "Required Version")
    private Long version;
}
