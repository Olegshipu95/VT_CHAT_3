package user.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import reactor.core.publisher.Mono;
import user.dto.response.AuthorizationDetails;
import user.entity.Role;

import java.util.List;
import java.util.UUID;

public final class SecurityContext {

    public static Mono<AuthorizationDetails> getAuthorizationDetails() {
        return getAuthentication().map(auth ->
            new AuthorizationDetails(
                getId(auth),
                getUsername(auth),
                getRoles(auth).stream()
                    .map(Role::valueOf)
                    .toList())
        );
    }

    private static Mono<Authentication> getAuthentication() {
        return ReactiveSecurityContextHolder.getContext()
            .map(org.springframework.security.core.context.SecurityContext::getAuthentication);
    }

    private static UUID getId(Authentication authentication) {
        return UUID.fromString((String) authentication.getPrincipal());
    }

    private static String getUsername(Authentication authentication) {
        return (String) authentication.getCredentials();
    }

    private static List<String> getRoles(Authentication authentication) {
        return authentication.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .toList();
    }
}
