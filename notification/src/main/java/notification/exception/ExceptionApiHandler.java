package notification.exception;

import com.fasterxml.jackson.core.JacksonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionApiHandler {

    @ExceptionHandler(Exception.class)
    public void defaultHandler(Exception ex) {
        log.error("Handle default error", ex);
    }

    @ExceptionHandler(MailException.class)
    public void internalException(MailException ex) {
        log.error("Mail error", ex);
    }

    @ExceptionHandler(JacksonException.class)
    public void authorizationDeniedException(JacksonException ex) {
        log.error("Parse message error", ex);
    }
}
