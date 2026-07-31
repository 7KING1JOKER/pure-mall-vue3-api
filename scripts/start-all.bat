@echo off
setlocal enabledelayedexpansion

set "MYSQL_SERVICE=MySQL"

:: Auto-elevate: request admin ONLY when the MySQL service needs starting.
:: If MySQL is already running, or we already have admin rights, skip elevation.
sc query %MYSQL_SERVICE% | findstr /i "RUNNING" >nul 2>&1
if %errorlevel% equ 0 goto :start
net session >nul 2>&1
if %errorlevel% equ 0 goto :start
echo [INFO] MySQL service is not running. Administrator rights are needed to start it.
echo [INFO] Requesting elevation via UAC - please click "Yes" on the prompt...
powershell -NoProfile -Command "Start-Process -FilePath '%~f0' -Verb RunAs"
exit /b 0

:start
echo ============================================
echo   pure-mall-backend - One-Click Start
echo ============================================
echo.
echo [NOTE] The MySQL service is started automatically if it is stopped.
echo        A UAC prompt may appear to request Administrator rights.
echo.

set "SCRIPT_DIR=%~dp0"

echo ============================================
echo   Step 1: Initialize Database
echo ============================================
echo.

call "%SCRIPT_DIR%init-database.bat"
if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Database initialization failed. Startup aborted.
    goto :end
)

echo.
echo ============================================
echo   Step 2: Start Backend Server
echo ============================================
echo.

call "%SCRIPT_DIR%start-backend.bat"
if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Backend server failed to start.
)

:end
echo.
echo Press any key to close this window...
pause >nul
exit /b 0
