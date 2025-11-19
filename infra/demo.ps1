param(
    [int]$TimeoutSeconds = 180
)

$root = Split-Path -Parent $MyInvocation.MyCommand.Definition
Push-Location $root

Write-Host "Building and starting compose (Keycloak + logistica-backend)..."
docker compose up -d --build

# Esperar a Keycloak
$wait = 0
Write-Host "Esperando a Keycloak (realm 'tpi')..."
while ($wait -lt $TimeoutSeconds) {
    try {
        $code = & curl.exe -s -o NUL -w "%{http_code}" http://localhost:8080/realms/tpi 2>$null
        if ($code -eq '200' -or $code -eq '302') { break }
    } catch {}
    Start-Sleep -Seconds 3
    $wait += 3
    Write-Host -NoNewline "."
}
if ($wait -ge $TimeoutSeconds) {
    Write-Error "Timeout esperando Keycloak en http://localhost:8080/realms/tpi"
    Pop-Location
    exit 1
}
Write-Host "`nKeycloak listo."

# Obtener token usando get-token.ps1
$tokenScript = Join-Path $root 'get-token.ps1'
if (-Not (Test-Path $tokenScript)) {
    Write-Error "No se encuentra get-token.ps1 en $root"
    Pop-Location
    exit 1
}
Write-Host "Obteniendo token... (get-token.ps1)"
$accessToken = & $tokenScript
if (-not $accessToken) {
    Write-Error "No se obtuvo token"
    Pop-Location
    exit 1
}
Write-Host "Token obtenido. Llamadas de prueba:"

Write-Host "`n-> /clientes"
$clientes = & curl.exe -s -H "Authorization: Bearer $accessToken" http://localhost:8081/clientes
Write-Host $clientes

Write-Host "`n-> /tramos"
$tramos = & curl.exe -s -H "Authorization: Bearer $accessToken" http://localhost:8081/tramos
Write-Host $tramos

Pop-Location
