package vn.nextpay.nextshop.enums;

public enum ResponseStatus {

    SUCCESSFUL("SUCCESSFUL", "Success"),
    CREATED("SUCCESSFUL", "Created"),
    UPDATED("SUCCESSFUL", "Update"),
    DELETED("SUCCESSFUL", "Deleted"),
    PHONE_EXISTED("PHONE_EXISTED", "Phone Existed"),
    GROUP_EXISTED("GROUP_EXISTED", "Group Existed"),
    PARTNER_NOT_FOUND("PARTNER_NOT_FOUND", "Partner Not Found"),
    GROUP_NOT_FOUND("GROUP_NOT_FOUND", "Group Not Found"),
    GROUP_CAN_NOT_MODIFY("GROUP_CAN_NOT_MODIFY", "Group Can Not Modified"),
    CONFLICT_VERSION("CONFLICT_VERSION", "Conflict Version"),
    WRONG_RELATIONSHIP_TYPE("WRONG_RELATIONSHIP_TYPE", "Wrong Relationship Type"),
    FAILED_AUTHEN_SECURITY("FAILED_AUTHEN_SECURITY", "Token InValid"),
    UNHANDLED_ERROR("UNHANDLED_ERROR", "Unhandled error", "Unhandled error: %s"),
    REQUEST_BODY_INVALID("REQUEST_BODY_INVALID", "Request body is invalid", "Request body is invalid: %s"),
    FIELD_INVALID("FIELD_INVALID", "Field invalid", "Field invalid: %s"),
    CALL_HTTP_HAS_ERROR("CALL_HTTP_HAS_ERROR", "Call http has error", "Call http has error: %s"),
    UNAUTHENTICATED("UNAUTHENTICATED", "UNAUTHENTICATED", "UNAUTHENTICATED: %s"),

    MERCHANT_NOT_FOUND("MERCHANT_NOT_FOUND", "Merchant not found", "Merchant not found: %s"),
    OWNER_NOT_FOUND("OWNER_NOT_FOUND", "Owner not found", "Owner not found: %s"),
    CUSTOMER_NOT_FOUND("CUSTOMER_NOT_FOUND", "Customer not found", "Customer not found: %s"),
    LOCATION_NOT_FOUND("LOCATION_NOT_FOUND", "Location not found", "Location not found: %s"),
    ALIAS_USER_NOT_FOUND("ALIAS_USER_NOT_FOUND", "Alias user not found", "Alias user not found: %s"),

    DUPLICATE_LOCATION_ID("DUPLICATE_LOCATION_ID", "Duplicate locationId", "Duplicate locationId: %s"),
    MERCHANT_ID_AND_LOCATION_IDS_NOT_MATCH("MERCHANT_ID_AND_LOCATION_IDS_NOT_MATCH", "merchantId & locationIds not match", "merchantId & locationIds not match: %s"),
    REQUIRE_ARGUMENT_TO_UPDATE("REQUIRE_ARGUMENT_TO_UPDATE", "Require argument to update", "Require argument to update: %s"),

    ROLE_EXIST("ROLE_EXIST", "Role existed", "Role existed: %s"),
    ROLE_NOT_FOUND("ROLE_NOT_FOUND", "Role not found", "Role not found: %s"),


    CALL_NEXTPAY_IAM_HAS_ERROR("CALL_NEXTPAY_IAM_HAS_ERROR", "Call Nextpay IAM has error", "Call Nextpay IAM has error: %s"),
    DUPLICATE_APP_ID("DUPLICATE_APP_ID", "Duplicate appId", "Duplicate appId: %s"),
    INVALID_LOCATION_ID("INVALID_LOCATION_ID", "Invalid locationId", "Invalid locationId: %s"),
    INVALID_APP_ID("INVALID_APP_ID", "Invalid appId", "Invalid appId: %s"),
    INVALID_PASSWORD("INVALID_PASSWORD", "Invalid 'password' field", "Invalid 'password' field: %s"),
    INVALID_PHONE_NO("INVALID_PHONE_NO", "Invalid 'phoneNo' field", "Invalid 'phoneNo' field: %s"),

    ;

    private String message;
    private String messageFormat;
    private String code;

    ResponseStatus(String code, String message) {
        this.message = message;
        this.code = code;
    }

    ResponseStatus(String code, String message, String messageFormat) {
        this.message = message;
        this.messageFormat = messageFormat;
        this.code = code;
    }

    public ResponseStatus formatMessage(Object... str) {
        if (this.messageFormat != null) {
            this.message = String.format(this.messageFormat, str);
        }
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageFormat() {
        return messageFormat;
    }
}
