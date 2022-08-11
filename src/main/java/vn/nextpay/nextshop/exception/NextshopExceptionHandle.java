package vn.nextpay.nextshop.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import vn.nextpay.nextshop.enums.EResponseStatus;
import vn.nextpay.nextshop.util.HttpResponseObject;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class NextshopExceptionHandle {
    @ExceptionHandler
    ResponseEntity<HttpResponseObject<Object>> handle(NextshopException error) {
        HttpResponseObject<Object> r = HttpResponseObject.failed();
        r.setCode(error.getStatus().getErrorCode());
        r.setMessage(error.getMessage());
        r.setErrors(error.getErrorDetails());
        return new ResponseEntity<>(r, error.getStatus().getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    HttpResponseObject<String> unhandledError(Exception e) {
        log.error(e.getMessage(), e);
        return new HttpResponseObject<>(false, vn.nextpay.nextshop.enums.ResponseStatus.UNHANDLED_ERROR, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpResponseObject<Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        HttpResponseObject<Object> r = HttpResponseObject.failed();

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        r.setErrors(errors);
        r.setCode(EResponseStatus.UNPROCESSABLE_ENTITY.getErrorCode());
        r.setMessage(EResponseStatus.UNPROCESSABLE_ENTITY.getMessage());
        return new ResponseEntity<>(r, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
