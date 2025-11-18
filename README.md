# TPI Backend - Instrucciones de configuración

## Clave de Google Maps

La aplicación puede usar la API de Google Maps Directions para obtener distancia y duración reales entre ubicaciones. La integración está deshabilitada por defecto en entornos donde no se provee la clave; en esos casos se utiliza un cliente de fallback que estima distancia con la fórmula de Haversine.

- Variable de entorno: `MAPS_GOOGLE_API_KEY`
- Propiedad en `application.yml`: `maps.google.api-key: ${MAPS_GOOGLE_API_KEY:}`

Si quieres habilitar la integración con la API de Google Maps, exporta la variable antes de iniciar la aplicación:

- Windows PowerShell:

  PS> $env:MAPS_GOOGLE_API_KEY = "TU_API_KEY"

- Linux / macOS (bash):

  $ export MAPS_GOOGLE_API_KEY=\"TU_API_KEY\"

## Configuración en CI (GitHub Actions)

Guarda la clave en los *Secrets* del repositorio con el nombre `MAPS_GOOGLE_API_KEY` y luego referencia el secreto en el workflow. Ejemplo de fragmento `workflow.yml`:

```yaml
env:
  MAPS_GOOGLE_API_KEY: ${{ secrets.MAPS_GOOGLE_API_KEY }}
```

Con esto, la aplicación arrancará la implementación de GoogleMapsClient cuando la propiedad `maps.google.api-key` esté presente y no vacía; si no está presente, se usa la estimación local para mantener tests y desarrollo estables.

---

Si querés, puedo:
- Agregar un ejemplo de workflow completo para CI.
- Añadir pruebas unitarias que mockeen la llamada a Google Maps.
- Asegurar que la aplicación falle en producción si la clave no existe (opcional).
