package vn.nextpay.nextshop.enums;

import lombok.Getter;

@Getter
public enum Config {
    CORE_API_GATEWAY_URL("CORE_API_GATEWAY_URL", ""),
    MY_NEXTPAY_URL("MY_NEXTPAY_URL", ""),
    MY_NEXTPAY_TOKEN("MY_NEXTPAY_TOKEN", ""),
    NEXTPAY_IAM_DOMAIN_URL("NEXTPAY_IAM_DOMAIN_URL", ""),
    APP_ID("APP_ID", ""),
    APPS_TO_REGISTER_PERMISSION("APPS_TO_REGISTER_PERMISSION", "");
    private final String code;
    private final String value;

    Config(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
