package com.tpi.logistica.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Logística API - UTN TPI",
        version = "1.0",
        description = "API para gestionar clientes, contenedores, solicitudes, rutas, tramos, camiones, depósitos y tarifas."
    )
)
public class OpenApiConfig {
}
