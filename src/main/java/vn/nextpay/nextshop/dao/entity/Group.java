/*
 *  Created by: HieuPV
 *  Mail: hieupv@mpos.vn
 */

package vn.nextpay.nextshop.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import vn.nextpay.nextshop.util.ValidateConstant;

import javax.validation.constraints.NotBlank;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("group")
@Builder
public class Group {
    @Id
    private String id;
//    @NotBlank(message = "Required Name")
    private String name;
    private String description;
    private String type;
    private boolean isDefaultGroup;
    private boolean isArchive;
    @NotBlank(message = ValidateConstant.REQUIRED_MERCHANT)
    private String merchantId;
    @Version
    private Long version;
    private Long priority;
    @JsonIgnore
    @CreatedDate
    private ZonedDateTime createdAt;


}
