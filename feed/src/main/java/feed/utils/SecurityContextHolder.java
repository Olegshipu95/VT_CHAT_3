package feed.utils;

import feed.exception.ErrorCode;
import feed.exception.InternalException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

public final class SecurityContextHolder {

    public static String getUsername() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            throw new InternalException(HttpStatus.FORBIDDEN, ErrorCode.FORBIDDEN);
        }
        return authentication.getCredentials().toString();
    }

    public static String getUserId() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            throw new InternalException(HttpStatus.FORBIDDEN, ErrorCode.FORBIDDEN);
        }
        return authentication.getPrincipal().toString();
    }

    private static Authentication getAuthentication() {
        return  org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
    }
}
