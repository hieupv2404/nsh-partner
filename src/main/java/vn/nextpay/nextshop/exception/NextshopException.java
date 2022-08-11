package vn.nextpay.nextshop.exception;


import vn.nextpay.nextshop.util.IStatusCode;

import java.util.HashMap;
import java.util.Map;

public class NextshopException extends RuntimeException {
    private IStatusCode status;
    private Map<String, String> errorDetails;

    public NextshopException(IStatusCode status, String message) {
        super(message);
        this.status = status;
        this.errorDetails = new HashMap<>();
    }

    public NextshopException(IStatusCode status) {
        super(status.getErrorCode());
        this.status = status;
        this.errorDetails = new HashMap<>();
    }

    public NextshopException(IStatusCode status, Map<String, String> errorDetails) {
        super(status.getErrorCode());
        this.status = status;
        this.errorDetails = errorDetails;
    }

    public IStatusCode getStatus() {
        return this.status;
    }

    public Map<String, String> getErrorDetails() {
        return this.errorDetails;
    }

    public void setStatus(final IStatusCode status) {
        this.status = status;
    }

    public void setErrorDetails(final Map<String, String> errorDetails) {
        this.errorDetails = errorDetails;
    }
}
