package user.config.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import user.entity.Role;
import user.exception.ErrorCode;
import user.exception.InternalException;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuthenticationProvider {

    private SecretKey key;

    private final AuthenticationProperties props;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public boolean isValid(String token) {
        try {
            var jwtParser = Jwts.parser().verifyWith(key).build();
            var claims = jwtParser.parseSignedClaims(token);
            return claims.getPayload().getExpiration().after(new Date());
        } catch (Exception e) {
            throw new InternalException(HttpStatus.UNAUTHORIZED, ErrorCode.TOKEN_EXPIRED);
        }
    }

    public String createAccessToken(UUID userId, String username, Set<Role> roles) {
        var claims = Jwts.claims()
            .subject(username)
            .add("id", userId.toString())
            .add("roles", roles)
            .build();
        Instant validity = Instant.now().plus(props.getAccess(), ChronoUnit.HOURS);
        return Jwts.builder()
            .claims(claims)
            .expiration(Date.from(validity))
            .signWith(key)
            .compact();
    }

    public String createRefreshToken(UUID userId, String username) {
        var claims = Jwts.claims()
            .subject(username)
            .add("id", userId.toString())
            .build();
        Instant validity = Instant.now().plus(props.getRefresh(), ChronoUnit.HOURS);
        return Jwts.builder()
            .claims(claims)
            .expiration(Date.from(validity))
            .signWith(key)
            .compact();
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        return new UsernamePasswordAuthenticationToken(getIdFromToken(token), getUsernameFromToken(token), getRolesFromToken(token));
    }

    public String getIdFromToken(String token) {
        var jwtParser = Jwts.parser().verifyWith(key).build();
        return jwtParser.parseSignedClaims(token).getPayload().get("id", String.class);
    }

    public String getUsernameFromToken(String token) {
        var jwtParser = Jwts.parser().verifyWith(key).build();
        return jwtParser.parseSignedClaims(token).getPayload().getSubject();
    }

    public List<GrantedAuthority> getRolesFromToken(String token) {
        var jwtParser = Jwts.parser().verifyWith(key).build();
        List<Object> roles = (List<Object>) jwtParser.parseSignedClaims(token).getPayload().get("roles");
        return roles.stream()
            .map(Objects::toString)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }
}
