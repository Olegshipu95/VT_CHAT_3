package user.exception;

import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

@Slf4j
@ControllerAdvice
public class RestControllerAdvice {

    @ApiResponse(responseCode = "500", useReturnTypeSchema = true)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> defaultHandler(Exception ex) {
        log.error("Handle default error", ex);
        return new Error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.SERVICE_UNAVAILABLE)
            .asResponseEntity();
    }

    @ExceptionHandler(InternalException.class)
    public ResponseEntity<Error> internalException(InternalException ex) {
        log.error("Handle internal error", ex);
        return new Error(ex.getHttpStatus().value(), ex.getErrorCode())
            .asResponseEntity();
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Error> jwtException(JwtException ex) {
        log.error("Handle jwt error", ex);
        return new Error(HttpStatus.BAD_REQUEST.value(), ErrorCode.TOKEN_INCORRECT_FORMAT)
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
        return new Error(HttpStatus.BAD_REQUEST.value(), ErrorCode.INVALID_REQUEST)
            .asResponseEntity();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Error> authorizationDeniedException(AccessDeniedException ex) {
        log.error("Handle auth error", ex);
        return new Error(HttpStatus.FORBIDDEN.value(), ErrorCode.FORBIDDEN)
            .asResponseEntity();
    }
}