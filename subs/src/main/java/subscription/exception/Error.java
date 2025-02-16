package subscription.exception;

import subscription.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Getter
public class Error {

    private final String errorId = UUID.randomUUID().toString();
    private final int status;
    private final ErrorCode code;

    public Error(int status, ErrorCode code) {
        this.status = status;
        this.code = code;
    }

    public ResponseEntity<Error> asResponseEntity() {
        return ResponseEntity.status(this.status).body(this);
    }
}