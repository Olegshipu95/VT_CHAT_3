package user.utils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.TestSecurityContextHolder;

import java.util.List;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

public final class SecurityMock {

    public static void mockSecurityContext() {
        org.springframework.security.core.context.SecurityContext securityContext = mock(SecurityContext.class);
        Authentication auth = getAuthentication();
        lenient().when(securityContext.getAuthentication()).thenReturn(auth);
        TestSecurityContextHolder.setContext(securityContext);
    }

    public static Authentication getAuthentication() {
        Authentication auth = mock(UsernamePasswordAuthenticationToken.class);
        lenient().when(auth.getPrincipal()).thenReturn("userId");
        lenient().when(auth.getCredentials()).thenReturn("username");
        return auth;
    }

    public static Authentication createAuthentication() {
        return new UsernamePasswordAuthenticationToken(
            "userId", "username", List.of(new SimpleGrantedAuthority("SUPERVISOR")));
    }
}
