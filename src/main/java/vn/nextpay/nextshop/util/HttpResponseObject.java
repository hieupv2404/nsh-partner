package vn.nextpay.nextshop.util;

import vn.nextpay.nextshop.enums.ResponseStatus;

import java.util.Map;

public class HttpResponseObject<DataType> {
    private DataType data;
    private String code;
    private Map<String, String> errors;
    private boolean success;
    private String message;

    public HttpResponseObject() {
    }

    private HttpResponseObject(boolean success) {
        this.success = success;
    }

    private HttpResponseObject(boolean success, String code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public HttpResponseObject(boolean success, String code, String message, DataType data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public HttpResponseObject(Boolean success, ResponseStatus status, DataType data) {
        this.success = success;
        this.code = status.getCode();
        this.message = status.getMessage();
        this.data = data;
    }

    public static <DataType> HttpResponseObject<DataType> success() {
        return new HttpResponseObject(true);
    }

    public static <DataType> HttpResponseObject<DataType> successWithCodeMessageDefault() {
        return new HttpResponseObject(true, "SUCCESS", "success");
    }

    public static <DataType> HttpResponseObject<DataType> failed() {
        return new HttpResponseObject(false);
    }

    public DataType getData() {
        return this.data;
    }

    public HttpResponseObject<DataType> setData(DataType data) {
        this.data = data;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public HttpResponseObject<DataType> setCode(String code) {
        this.code = code;
        return this;
    }

    public HttpResponseObject<DataType> setMessage(String message) {
        this.message = message;
        return this;
    }

    public Map<String, String> getErrors() {
        return this.errors;
    }

    public HttpResponseObject<DataType> setErrors(Map<String, String> errors) {
        this.errors = errors;
        return this;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(final boolean success) {
        this.success = success;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof HttpResponseObject)) {
            return false;
        } else {
            HttpResponseObject<?> other = (HttpResponseObject)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label63: {
                    Object this$data = this.getData();
                    Object other$data = other.getData();
                    if (this$data == null) {
                        if (other$data == null) {
                            break label63;
                        }
                    } else if (this$data.equals(other$data)) {
                        break label63;
                    }

                    return false;
                }

                Object this$code = this.getCode();
                Object other$code = other.getCode();
                if (this$code == null) {
                    if (other$code != null) {
                        return false;
                    }
                } else if (!this$code.equals(other$code)) {
                    return false;
                }

                Object this$errors = this.getErrors();
                Object other$errors = other.getErrors();
                if (this$errors == null) {
                    if (other$errors != null) {
                        return false;
                    }
                } else if (!this$errors.equals(other$errors)) {
                    return false;
                }

                if (this.isSuccess() != other.isSuccess()) {
                    return false;
                } else {
                    Object this$message = this.getMessage();
                    Object other$message = other.getMessage();
                    if (this$message == null) {
                        return other$message == null;
                    } else return this$message.equals(other$message);

                }
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof HttpResponseObject;
    }

    public int hashCode() {
        int result = 1;
        Object $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        Object $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        Object $errors = this.getErrors();
        result = result * 59 + ($errors == null ? 43 : $errors.hashCode());
        result = result * 59 + (this.isSuccess() ? 79 : 97);
        Object $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        return result;
    }

    public String toString() {
        Object var10000 = this.getData();
        return "HttpResponseObject(data=" + var10000 + ", code=" + this.getCode() + ", errors=" + this.getErrors() + ", success=" + this.isSuccess() + ", message=" + this.getMessage() + ")";
    }
}
