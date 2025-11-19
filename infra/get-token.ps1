param(
    [string]$ClientId = 'logistica-backend',
    [string]$ClientSecret = 'logistica-secret',
    [string]$Username = 'testuser',
    [string]$Password = 'test',
    [string]$Realm = 'tpi',
    [string]$KeycloakUrl = 'http://localhost:8080'
)

try {
    # Intentar obtener token ejecutando curl dentro de la red de compose (usa
    # la imagen curlimages/curl). Esto hace la petición usando el hostname
    # interno 'keycloak' para que el 'iss' del token sea consistente con el
    # issuer que usa el backend dentro del contenedor.
    try {
        $network = "infra_default"
        $curlImage = "curlimages/curl:8.1.0"
        $postData = @(
            "client_id=$ClientId",
            "client_secret=$ClientSecret",
            "grant_type=password",
            "username=$Username",
            "password=$Password"
        ) -join "&"

        $args = @(
            'run','--rm','--network',$network,$curlImage,'-s','-X','POST',"http://keycloak:8080/realms/$Realm/protocol/openid-connect/token",
            '-d',"client_id=$ClientId",'-d',"client_secret=$ClientSecret",'-d','grant_type=password','-d',"username=$Username",'-d',"password=$Password"
        )
        $out = & docker @args 2>$null
        if ($out) {
            try { $tokenResp = $out | ConvertFrom-Json } catch { $tokenResp = $null }
        }
    } catch {
        $tokenResp = $null
    }

    # Fallback: petición directa al localhost (si docker-run no funcionó)
    if ($null -eq $tokenResp -or $null -eq $tokenResp.access_token) {
        $body = @{client_id=$ClientId; client_secret=$ClientSecret; grant_type='password'; username=$Username; password=$Password}
        $tokenResp = Invoke-RestMethod -Uri "$KeycloakUrl/realms/$Realm/protocol/openid-connect/token" -Method Post -Body $body
    }

    if ($null -eq $tokenResp -or $null -eq $tokenResp.access_token) {
        Write-Error "No se obtuvo access_token. Respuesta: $($tokenResp | ConvertTo-Json -Depth 5)"
        exit 1
    }
    $tokenResp.access_token
} catch {
    Write-Error "Error solicitando token: $($_.Exception.Message)"
    exit 1
}
