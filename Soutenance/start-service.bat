@echo off
echo "Demarrage du microservice Soutenance..."
echo "Port: 8084"
echo "Profile: dev"

mvn clean install
if %ERRORLEVEL% EQU 0 (
    echo "Build successful - Lancement du service..."
    mvn spring-boot:run
) else (
    echo "Erreur de build - Verification des dependances..."
    pause
)
