package com.tpi.logistica.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/api/login/oauth2/code")
public class KeycloakCallbackController {

    @Value("${keycloak.url:http://localhost:8080/auth}")
    private String keycloakUrl;

    @Value("${keycloak.realm:logistica}")
    private String realm;

    @GetMapping("/keycloak")
    public String intercambiarCode(@RequestParam String code) throws UnsupportedEncodingException {
        RestClient restClient = RestClient.create();

        String formData = "grant_type=authorization_code" +
                "&code=" + URLEncoder.encode(code, StandardCharsets.UTF_8) +
                "&client_id=tpi-backend-client" +
                "&redirect_uri=" + URLEncoder.encode("http://localhost:8080/api/login/oauth2/code/keycloak", StandardCharsets.UTF_8);

        String tokenEndpoint = keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        String token = restClient.post()
                .uri(tokenEndpoint)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(String.class);

        log.info("🔐 Token recibido desde Keycloak: {}", token);

        return "✅ Token recibido y logueado en consola. Revisar logs del servidor.";
    }
}
