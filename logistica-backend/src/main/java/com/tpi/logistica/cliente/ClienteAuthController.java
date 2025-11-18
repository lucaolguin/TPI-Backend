package com.tpi.logistica.cliente;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteAuthController {

    @Value("${keycloak.url}")
    private String keycloakUrl;

    private final ClienteService clienteService;

    @PostMapping("/registro")
    public String registrar(@RequestBody Cliente cliente) {

        // 1) Crear usuario en Keycloak
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakUrl)
                .realm("logistica")
                .clientId("admin-cli")
                .username("admin")
                .password("admin")
                .build();

        UserRepresentation user = new UserRepresentation();
        user.setUsername(cliente.getEmail());
        user.setEmail(cliente.getEmail());
        user.setEnabled(true);

        CredentialRepresentation password = new CredentialRepresentation();
        password.setTemporary(false);
        password.setType(CredentialRepresentation.PASSWORD);
        password.setValue("cliente123"); // contraseña default

        user.setCredentials(java.util.List.of(password));

        keycloak.realm("logistica").users().create(user);

        // 2) Guardar cliente en BD
        clienteService.create(cliente);

        return "Cliente registrado exitosamente.";
    }
}
