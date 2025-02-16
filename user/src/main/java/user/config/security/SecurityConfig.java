package user.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationFilter authenticationFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
            .cors(ServerHttpSecurity.CorsSpec::disable)
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
            .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
            .anonymous(ServerHttpSecurity.AnonymousSpec::disable)
            //.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeExchange(exchange -> {
                exchange.pathMatchers(
                    "/actuator/**",
                    "/webjars/**",
                    "/swagger-ui/**",
                    "chat-user-cloud/v3/api-docs/**",
                    "/auth/tokens/**",
                    "/accounts/users/**"
                ).permitAll();
                exchange.pathMatchers("/**").authenticated();
            })
            .addFilterBefore(authenticationFilter, SecurityWebFiltersOrder.AUTHORIZATION)
            .exceptionHandling(handler -> {
                handler.authenticationEntryPoint((exchange, __) -> Mono.fromRunnable(() -> exchange.getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED)));
                handler.accessDeniedHandler((exchange, __) -> Mono.fromRunnable(() -> exchange.getResponse()
                    .setStatusCode(HttpStatus.FORBIDDEN)));
            })
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
