package vn.nextpay.nextshop.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.nextpay.nextshop.controller.dto.MetaDTO;
import vn.nextpay.nextshop.enums.ResponseStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoreResponseObject<T> {
    private Boolean success;
    private String code;
    private String message;
    private T data;
    private String status;
    private MetaDTO meta;

    public CoreResponseObject(Boolean success, ResponseStatus status, T data) {
        this.success = success;
        this.code = status.getCode();
        this.message = status.getMessage();
        this.data = data;
    }
}
