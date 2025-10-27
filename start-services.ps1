Write-Host "==========================================="
Write-Host "  PORTAIL DE SUIVI DU DOCTORAT - DEMARRAGE"
Write-Host "==========================================="
Write-Host ""

# Fonction pour démarrer un service
function Start-Service {
    param([string]$name, [string]$path, [string]$port)
    Write-Host "[+] Démarrage de $name sur le port $port..."
    Set-Location $path
    Start-Process -FilePath ".\mvnw.cmd" -ArgumentList "spring-boot:run", "-Dserver.port=$port" -NoNewWindow
    Start-Sleep -Seconds 5
}

# Démarrer Eureka Server
Start-Service "Eureka Server" "..\Eureka-server" "8761"

# Démarrer Gateway
Start-Service "Gateway Service" "..\gateway-service" "8888"

# Démarrer les microservices
Start-Service "Service Notification" "..\Notification_communication" "8081"
Start-Service "Service Gestion Comptes" "..\Gestion_des_comptes_et_authentification" "8082"
Start-Service "Service Inscription" "..\inscription_et_reinscription" "8083"
Start-Service "Service Soutenance" "..\Soutenance" "8084"

Write-Host ""
Write-Host "==========================================="
Write-Host "         SERVICES DEMARRES !"
Write-Host "==========================================="
Write-Host ""
Write-Host "URLs d'accès :"
Write-Host "- Eureka Server: http://localhost:8761"
Write-Host "- Gateway: http://localhost:8888"
Write-Host "- Notification: http://localhost:8081"
Write-Host "- Gestion Comptes: http://localhost:8082"
Write-Host "- Inscription: http://localhost:8083"
Write-Host "- Soutenance: http://localhost:8084"
Write-Host ""
Read-Host "Appuyez sur Entrée pour quitter"