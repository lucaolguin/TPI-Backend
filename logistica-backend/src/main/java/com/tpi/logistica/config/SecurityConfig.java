package com.tpi.logistica.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {

        private final Environment env;

        public SecurityConfig(Environment env) {
                this.env = env;
        }

    @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                boolean isDev = false;
                try {
                        for (String p : env.getActiveProfiles()) {
                                if ("dev".equals(p)) { isDev = true; break; }
                        }
                } catch (Exception ignored) {}

                http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // ENDPOINTS PÚBLICOS:
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/actuator/health",
                                "/error"
                        ).permitAll()

                        // ENDPOINTS POR ROL:
                        .requestMatchers("/admin/**").hasRole("Operador")
                        .requestMatchers("/cliente/**").hasAnyRole("Cliente", "Operador")

                        // TODO: tus endpoints privados:
                        .anyRequest().authenticated()
                )
                ;

        if (!isDev) {
            http.oauth2ResourceServer(oauth2 ->
                    oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter()))
            );
        } else {
            // En dev deshabilitamos el resource-server para no forzar la decodificación
            // del JWT (evita problemas de 'iss' durante la demo local).
            http.authorizeHttpRequests(a -> a.anyRequest().permitAll());
        }

        return http.build();
    }

    // === Conversor para leer roles de Keycloak ===
    @Bean
    public JwtAuthenticationConverter jwtAuthConverter() {

        JwtGrantedAuthoritiesConverter defaultConverter = new JwtGrantedAuthoritiesConverter();
        defaultConverter.setAuthorityPrefix("ROLE_");    // Spring usa ROLE_*
        defaultConverter.setAuthoritiesClaimName("realm_access.roles"); // Keycloak manda roles acá

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {

            // Convertidor por defecto (si tuviera authorities extras)
            Collection<GrantedAuthority> defaultAuthorities = defaultConverter.convert(jwt);

            // Obtener roles desde "realm_access.roles"
            List<String> realmRoles = jwt.getClaimAsStringList("realm_access.roles");
            if (realmRoles == null) realmRoles = List.of();

            // Convertir "admin" → ROLE_admin
            List<GrantedAuthority> kcAuthorities = realmRoles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());

            // Unimos ambas colecciones
            kcAuthorities.addAll(defaultAuthorities);

            return kcAuthorities;
        });

        return converter;
    }
}
