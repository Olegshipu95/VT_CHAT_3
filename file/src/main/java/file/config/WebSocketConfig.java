package file.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socket/files").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(16 * 1024 * 1024);
        registry.setSendBufferSizeLimit(16 * 1024 * 1024);
        registry.setSendTimeLimit(16 * 1024 * 1024);
    }

    @Bean
    public ServletServerContainerFactoryBean createServletServerContainerFactoryBean() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(16 * 1024 * 1024);
        container.setMaxSessionIdleTimeout(6 * 1024 * 1024L);
        container.setAsyncSendTimeout(16 * 1024 * 1024L);
        container.setMaxBinaryMessageBufferSize(16 * 1024 * 1024);
        return container;
    }

    /*
    @Bean
    public ServletServerContainerFactoryBean createServletServerContainerFactoryBean() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(4 * 1024 * 1024);
        container.setMaxSessionIdleTimeout(4 * 1024 * 1024L);
        container.setAsyncSendTimeout(4 * 1024 * 1024L);
        container.setMaxBinaryMessageBufferSize(4 * 1024 * 1024);
        return container;
    }


    private UsernamePasswordAuthenticationToken getUserIdFromToken(String token, PublicKey publicKey) {
        try {
            Claims claims = (Claims) Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parse(token)
                .getPayload();

            String userId = claims.get("id", String.class);
            List<SimpleGrantedAuthority> authorities = ((List<?>) claims.get("roles"))
                .stream()
                .map(Objects::toString)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

            boolean isEnabled = claims.get("enabled", Boolean.class) != null && claims.get("enabled", Boolean.class);
            return isEnabled ? new UsernamePasswordAuthenticationToken(userId, null, authorities) : null;
        } catch (JwtException e) {
            return null;
        } catch (Exception e) {
            log.error("Error while parsing JWT token", e);
            return null;
        }
    }



    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                /*
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String token = accessor.getFirstNativeHeader("Authorization");

                    if (token != null && token.startsWith(BEARER_PREFIX)) {
                        token = token.substring(BEARER_PREFIX.length());
                        UsernamePasswordAuthenticationToken authentication = getUserIdFromToken(token, publicKey);

                        if (authentication != null) {
                            accessor.setUser(authentication);
                            log.info("Connection successful");
                        }
                    }
                }


                return message;
            }
        });
    }

    */
}
