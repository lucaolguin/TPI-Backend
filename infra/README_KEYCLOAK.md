Keycloak local de desarrollo (infra)

Este directorio contiene un `docker-compose.yml` que arranca Keycloak en modo `start-dev`
con un realm de ejemplo (`tpi`) y un usuario de prueba.

Archivos:
- `docker-compose.yml` : define el servicio Keycloak (levanta en el puerto 8080).
- `keycloak/realm-export.json` : definición del realm `tpi` con un usuario `testuser` y un
  cliente `logistica-backend` (secret: `logistica-secret`).

Cómo usarlo:

1) Levantar Keycloak (desde la raíz del repo):

```powershell
cd "c:\Users\Usuario\OneDrive\Escritorio\Camilo 2025\SEGUNDO CUATRI\BACKEND\TPI BACKEND\infra"
docker compose up -d
```

2) Acceder a la consola de Keycloak:
- URL: http://localhost:8080
- Usuario admin: `admin`
- Password admin: `admin`

3) Obtener un token para pruebas con curl (grant_type=password):

```bash
curl -X POST "http://localhost:8080/realms/tpi/protocol/openid-connect/token" \
  -d "client_id=logistica-backend" \
  -d "client_secret=logistica-secret" \
  -d "grant_type=password" \
  -d "username=testuser" \
  -d "password=test"
```

Respuesta: JSON con `access_token` que puedes usar en Authorization: Bearer <token>

Notas y recomendaciones:
- Este entorno es solo para desarrollo y pruebas locales (usa `start-dev` y H2 internamente).
- Para entornos más cercanos a producción, configura una base de datos persistente y no uses `start-dev`.
- Si querés añadir los servicios `gateway` y `logistica-backend` al mismo `docker-compose`, lo
  podemos hacer; por ahora dejo Keycloak separado para simplicidad.
