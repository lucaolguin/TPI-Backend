package com.tpi.logistica.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.core.OAuth2Error;

@Configuration
public class JwtDecoderConfig {

    // Crear un JwtDecoder que use el JWKS de Keycloak (accesible dentro del compose
    // como `http://keycloak:8080/...`) y valide timestamps, pero no fuerce la
    // comparación del claim `iss`. Esto evita problemas de mismatch del host usado
    // para solicitar el token desde el host (localhost) y el host interno usado
    // por los contenedores.
    @Bean
    public JwtDecoder jwtDecoder() {
        String jwkUrl = "http://keycloak:8080/realms/tpi/protocol/openid-connect/certs";
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(jwkUrl).build();

        // Validator que solo comprueba timestamps (exp/nbf)
        JwtTimestampValidator timestampValidator = new JwtTimestampValidator();

        // Agregar un validador que falle con mensaje claro si no cumple timestamps
        OAuth2TokenValidator<Jwt> validator = (jwt) -> {
            OAuth2TokenValidatorResult result = timestampValidator.validate(jwt);
            if (result.hasErrors()) {
                return result;
            }
            return OAuth2TokenValidatorResult.success();
        };

        decoder.setJwtValidator(validator);
        return decoder;
    }
}
