package com.tpi.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@Profile("!dev-no-security")
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http, Environment env) {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeExchange(exchanges -> exchanges
                // Endpoints públicos
                .pathMatchers(
                        "/actuator/health",
                        "/error",
                        "/clientes/registro",
                        "/api/login/oauth2/code/keycloak"
                ).permitAll()
                // Todos los demás requieren autenticación
                .anyExchange().authenticated()
            );
            // Configuración de OAuth2 Resource Server con JWT (reactiva)
        if (env.getProperty("spring.security.oauth2.resourceserver.jwt.jwk-set-uri") != null
                || env.getProperty("spring.security.oauth2.resourceserver.jwt.issuer-uri") != null) {
            http.oauth2ResourceServer(oauth2 -> oauth2.jwt());
        }

        return http.build();
    }
}
