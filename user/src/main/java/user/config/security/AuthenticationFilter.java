package user.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import user.exception.ErrorCode;
import user.exception.InternalException;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements WebFilter {

    private static final int BEARER_PREFIX_LENGTH = 7;

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final AuthenticationProvider authenticationProvider;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String bearerToken = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION);
        if (bearerToken != null) {
            if (bearerToken.startsWith(BEARER_PREFIX)) {
                bearerToken = bearerToken.substring(BEARER_PREFIX_LENGTH);
            } else {
                throw new InternalException(HttpStatus.UNAUTHORIZED, ErrorCode.TOKEN_DOES_NOT_EXIST);
            }
        }
        Authentication auth = null;
        if (bearerToken != null && authenticationProvider.isValid(bearerToken)) {
            auth = authenticationProvider.getAuthentication(bearerToken);
        }
        if (auth == null) {
            return chain.filter(exchange);
        } else {
            return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
        }
    }
}
