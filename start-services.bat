@echo off
echo ===========================================
echo   PORTAIL DE SUIVI DU DOCTORAT - DEMARRAGE
echo ===========================================

echo.
echo [1/4] Démarrage du Serveur Eureka...
cd Eureka-server
start "Eureka-Server" cmd /c ".\mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8761"
timeout /t 10 /nobreak > nul

echo.
echo [2/4] Démarrage du Gateway Service...
cd ..\gateway-service
start "Gateway-Service" cmd /c ".\mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8888"
timeout /t 10 /nobreak > nul

echo.
echo [3/4] Démarrage du Service de Notification...
cd ..\Notification_communication
start "Notification-Service" cmd /c ".\mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081"
timeout /t 5 /nobreak > nul

echo.
echo [4/4] Démarrage des autres services...
cd ..\Gestion_des_comptes_et_authentification
start "Gestion-Comptes" cmd /c ".\mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8082"

cd ..\inscription_et_reinscription
start "Inscription-Reinscription" cmd /c ".\mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8083"

cd ..\Soutenance
start "Soutenance" cmd /c ".\mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8084"

echo.
echo ===========================================
echo   SERVICES DEMARRES !
echo ===========================================
echo.
echo URLs d'accès :
echo - Eureka Server: http://localhost:8761
echo - Gateway: http://localhost:8888
echo - Notification: http://localhost:8081
echo - Gestion Comptes: http://localhost:8082
echo - Inscription: http://localhost:8083
echo - Soutenance: http://localhost:8084
echo.
echo Appuyez sur une touche pour quitter...
pause > nul