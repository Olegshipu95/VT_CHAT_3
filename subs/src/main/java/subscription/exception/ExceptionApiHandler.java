package subscription.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

@Slf4j
@RestControllerAdvice
public class ExceptionApiHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleDefaultException(Exception ex) {
        log.error("Handle default exception", ex);
        return new Error(HttpStatus.SERVICE_UNAVAILABLE.value(), ErrorCode.SERVICE_UNAVAILABLE)
            .asResponseEntity();
    }

    @ExceptionHandler(InternalException.class)
    public ResponseEntity<Error> internalException(InternalException ex) {
        log.error("Handle internal error", ex);
        return new Error(ex.getHttpStatus().value(), ex.getErrorCode())
            .asResponseEntity();
    }

    @ExceptionHandler({
        ConstraintViolationException.class,
        MethodArgumentNotValidException.class,
        WebExchangeBindException.class,
        ServerWebInputException.class,
        HttpMessageNotReadableException.class
    })
    public ResponseEntity<Error> validationExceptions(Exception ex) {
        log.error("Handle validation error", ex);
        return new Error(HttpStatus.BAD_REQUEST.value(), ErrorCode.VALIDATION_FAILED)
            .asResponseEntity();
    }
}
