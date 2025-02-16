package subscription.utils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

public final class SecurityMock {

    public static void mockSecurityContext() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication auth = getAuthentication();
        lenient().when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    public static Authentication getAuthentication() {
        Authentication auth = mock(UsernamePasswordAuthenticationToken.class);
        lenient().when(auth.getPrincipal()).thenReturn(UUID.randomUUID().toString());
        lenient().when(auth.getCredentials()).thenReturn("username");
        return auth;
    }
}
