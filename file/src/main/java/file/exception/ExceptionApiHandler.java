package file.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.multipart.MultipartException;
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

    @ExceptionHandler(S3Exception.class)
    public ResponseEntity<Error> s3Exception(S3Exception ex) {
        log.error("Error working with s3", ex);
        return new Error(HttpStatus.NOT_FOUND.value(), ErrorCode.S3_ERROR)
            .asResponseEntity();
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<Error> fileException(Exception ex) {
        log.error("Error with file resolution", ex);
        return new Error(HttpStatus.BAD_REQUEST.value(), ErrorCode.WRONG_FILE)
            .asResponseEntity();
    }

    @ExceptionHandler({
        ConstraintViolationException.class,
        MethodArgumentNotValidException.class,
        WebExchangeBindException.class,
        ServerWebInputException.class
    })
    public ResponseEntity<Error> validationExceptions(Exception ex) {
        log.error("Handle validation error", ex);
        return new Error(HttpStatus.BAD_REQUEST.value(), ErrorCode.VALIDATION_FAILED)
            .asResponseEntity();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Error> authorizationDeniedException(AccessDeniedException ex) {
        log.error("Access denied", ex);
        return new Error(HttpStatus.FORBIDDEN.value(), ErrorCode.FORBIDDEN)
            .asResponseEntity();
    }
}
