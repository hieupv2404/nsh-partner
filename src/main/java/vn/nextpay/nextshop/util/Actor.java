package vn.nextpay.nextshop.util;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Data
public class Actor {
    private String token;
    private String userId;
    private String merchantId;
    private String channel;
    private String appParentId;
    private Object permissions;
    private Boolean isInside;

}
