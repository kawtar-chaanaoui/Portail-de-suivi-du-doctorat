r@echo off
echo ========================================
echo Demarrage du microservice SOUTENANCE
echo ========================================
echo.

cd /d "%~dp0"

echo [1/3] Verification du port 8084...
netstat -ano | findstr :8084 >nul
if %errorlevel% equ 0 (
    echo ERREUR: Port 8084 occupe!
    echo Tentative d'arret du processus...
    for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8084') do (
        taskkill /F /PID %%a 2>nul
    )
    timeout /t 3 >nul
)

echo [2/3] Compilation...
call mvn clean compile -q
if %errorlevel% neq 0 (
    echo ERREUR DE COMPILATION!
    pause
    exit /b 1
)

echo [3/3] Demarrage du service...
echo.
echo Service disponible sur: http://localhost:8084
echo Mot de passe securite genere dans les logs
echo.
echo Appuyez sur Ctrl+C pour arreter
echo ========================================
echo.

mvn spring-boot:run

pause

