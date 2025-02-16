package gateway.exceptions;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
@Hidden
public class FallBackController {

    @RequestMapping(
            method = {
                    RequestMethod.GET,
                    RequestMethod.HEAD,
                    RequestMethod.POST,
                    RequestMethod.PUT,
                    RequestMethod.PATCH,
                    RequestMethod.DELETE,
                    RequestMethod.OPTIONS,
                    RequestMethod.TRACE
            }
    )
    public void fallback() {
        throw new InternalException(HttpStatus.SERVICE_UNAVAILABLE, ErrorCode.CIRCUIT_BREAKER_STOP);
    }
}