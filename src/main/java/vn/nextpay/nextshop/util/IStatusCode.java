package vn.nextpay.nextshop.util;

import org.springframework.http.HttpStatus;

public interface IStatusCode {
    HttpStatus getHttpStatus();
    String getErrorCode();
}
