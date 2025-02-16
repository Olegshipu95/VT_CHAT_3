package subscription.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import subscription.utils.SecurityContextHolder;

import java.util.List;

import static subscription.utils.MappingUtils.toJson;

@Component
public class AuthFeignInterceptor implements RequestInterceptor {

    private static final String USER_ID = "UserId";
    private static final String USERNAME = "Username";
    private static final String USER_ROLES = "UserRoles";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(USER_ID, SecurityContextHolder.getUserId());
        requestTemplate.header(USERNAME, SecurityContextHolder.getUsername());
        requestTemplate.header(USER_ROLES, toJson(List.of("SUPERVISOR")));
    }
}
