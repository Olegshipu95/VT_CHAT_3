package messenger.config;

import com.fasterxml.jackson.core.type.TypeReference;
import messenger.utils.MappingUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements Filter {

    private static final String USER_ID = "UserId";
    private static final String USERNAME = "Username";
    private static final String USER_ROLES = "UserRoles";


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String userId = httpRequest.getHeader(USER_ID);
        String username = httpRequest.getHeader(USERNAME);
        String userRoles = httpRequest.getHeader(USER_ROLES);
        if (userId != null && username != null && userRoles != null) {
            List<SimpleGrantedAuthority> roles =
                MappingUtils.toObject(httpRequest.getHeader(USER_ROLES), new TypeReference<List<SimpleGrantedAuthority>>() {
                });
            SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(userId, username, roles));
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
