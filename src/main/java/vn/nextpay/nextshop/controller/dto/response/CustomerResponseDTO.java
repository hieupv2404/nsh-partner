/*
 *  Created by: HieuPV
 *  Mail: hieupv@mpos.vn
 */

package vn.nextpay.nextshop.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.nextpay.nextshop.controller.dto.CoCustomerDTO;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDTO implements Serializable {
    private String id;
    private String name;
    private String phoneNo;
    private String address;
    private int version;
    private CoCustomerDTO.MetaData metaData;


    @Override
    public String toString() {
        return "CustomerResponseDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", address='" + address + '\'' +
                ", note='" + metaData.note + '\'' +
                '}';
    }
}
