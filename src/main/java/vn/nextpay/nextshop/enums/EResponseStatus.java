package vn.nextpay.nextshop.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import vn.nextpay.nextshop.util.IStatusCode;

@Getter
@AllArgsConstructor
public enum EResponseStatus implements IStatusCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "INTERNAL_SERVER_ERROR"),
    UNPROCESSABLE_ENTITY(HttpStatus.BAD_REQUEST, "UNPROCESSABLE_ENTITY", "UNPROCESSABLE_ENTITY"),
    FAILED_IN_AUTHENTICATION(HttpStatus.FORBIDDEN, "FAILED_IN_AUTHENTICATION", "FAILED_IN_AUTHENTICATION"),
    PHONE_NUMBER_IS_EXISTED(HttpStatus.BAD_REQUEST, "PHONE_NUMBER_IS_EXISTED", "PHONE_NUMBER_IS_EXISTED"),
    GROUP_NOT_FOUND(HttpStatus.BAD_REQUEST, "GROUP_NOT_FOUND", "GROUP_NOT_FOUND"),
    PARTNER_NOT_FOUND(HttpStatus.BAD_REQUEST, "PARTNER_NOT_FOUND", "PARTNER_NOT_FOUND"),
    WRONG_RELATIONSHIP_TYPE(HttpStatus.BAD_REQUEST, "WRONG_RELATIONSHIP_TYPE", "WRONG_RELATIONSHIP_TYPE"),
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "UNAUTHENTICATED", "UNAUTHENTICATED"),

    ;

    private String errorCode;
    private String message;
    private HttpStatus httpStatus;

    EResponseStatus(HttpStatus httpStatus, String message, String errorCode) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.errorCode = errorCode;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public String getErrorCode() {
        return this.errorCode;
    }

    public String getMessage() {
        return this.message;
    }
}
