@echo off
setlocal enabledelayedexpansion

echo ============================================
echo   pure-mall-backend - Start Backend
echo ============================================
echo.

set "SCRIPT_DIR=%~dp0"
set "PROJECT_DIR=%SCRIPT_DIR%.."

cd /d "%PROJECT_DIR%"

:: 1. Check Java
echo [1/4] Checking Java environment...
where java >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Java not found. Please install JDK 1.8+ and set JAVA_HOME.
    exit /b 1
)
for /f "tokens=*" %%i in ('java -version 2^>^&1 ^| findstr /i "version"') do echo [INFO] %%i

:: 2. Check Maven Wrapper
echo [2/4] Checking Maven Wrapper...
if not exist "%PROJECT_DIR%\mvnw.cmd" (
    echo [ERROR] Maven Wrapper ^(mvnw.cmd^) not found.
    exit /b 1
)
echo [OK] Maven Wrapper ready

:: 3. Check and free port 8080
echo [3/4] Checking port 8080...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":8080" ^| findstr "LISTENING"') do (
    echo [WARN] Port 8080 in use by PID %%a. Killing...
    taskkill /F /PID %%a >nul 2>&1
    timeout /t 2 /nobreak >nul
    echo [OK] Port 8080 freed
    goto :port_done
)
echo [OK] Port 8080 is available
:port_done

:: 4. Build and start
echo [4/4] Building and starting application...
echo [INFO] Packaging (skip tests)...
call "%PROJECT_DIR%\mvnw.cmd" package -DskipTests -q

for %%f in ("%PROJECT_DIR%\target\pure-mall-backend-*.jar") do set "JAR_FILE=%%f"
if not exist "!JAR_FILE!" (
    echo [ERROR] Build failed - jar file not found.
    exit /b 1
)

echo [INFO] Jar: %JAR_FILE%
echo [INFO] Server URL:  http://localhost:8080
echo [INFO] Swagger UI:  http://localhost:8080/swagger-ui/index.html
echo [INFO] Press Ctrl+C to stop
echo.

java -jar "!JAR_FILE!"

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Server failed to start.
    exit /b 1
)
exit /b 0
