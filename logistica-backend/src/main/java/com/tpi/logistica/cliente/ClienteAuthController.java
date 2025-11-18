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
    
    @Value("${keycloak.realm:logistica}")
    private String realm;
    
    @Value("${keycloak.admin.username:admin}")
    private String adminUsername;
    
    @Value("${keycloak.admin.password:admin}")
    private String adminPassword;
    
    @Value("${keycloak.admin.client-id:admin-cli}")
    private String adminClientId;

    private final ClienteService clienteService;

    @PostMapping("/registro")
    public String registrar(@RequestBody Cliente cliente) {

        // 1) Crear usuario en Keycloak
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakUrl)
                .realm(realm)
                .clientId(adminClientId)
                .username(adminUsername)
                .password(adminPassword)
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

        keycloak.realm(realm).users().create(user);

        // 2) Guardar cliente en BD
        clienteService.create(cliente);

        return "Cliente registrado exitosamente.";
    }
}
