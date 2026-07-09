@echo off
setlocal enabledelayedexpansion

echo ============================================
echo   pure-mall-backend - One-Click Start
echo ============================================
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
    exit /b 1
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
    exit /b 1
)
exit /b 0
