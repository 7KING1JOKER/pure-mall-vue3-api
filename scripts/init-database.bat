@echo off
setlocal enabledelayedexpansion

echo ============================================
echo   pure-mall-backend - Database Init
echo ============================================
echo.

set "SCRIPT_DIR=%~dp0"
set "PROJECT_DIR=%SCRIPT_DIR%.."

set "DB_HOST=localhost"
set "DB_PORT=3306"
set "DB_NAME=pure_mall"
set "DB_USER=root"
set "DB_PASS=123456"
set "SQL_FILE=%PROJECT_DIR%\mall_database.sql"

echo [1/3] Checking MySQL connection...
mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASS% -e "SELECT 1;" >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Cannot connect to MySQL. Please check if MySQL service is running.
    echo         Host: %DB_HOST%:%DB_PORT%
    echo         User: %DB_USER%
    exit /b 1
)
echo [OK] MySQL connection successful

echo.
echo [2/3] Creating database (if not exists)...
mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASS% -e "CREATE DATABASE IF NOT EXISTS %DB_NAME% DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Failed to create database
    exit /b 1
)
echo [OK] Database ready: %DB_NAME%

echo.
echo [3/3] Importing SQL file...
if not exist "%SQL_FILE%" (
    echo [ERROR] SQL file not found: %SQL_FILE%
    exit /b 1
)
mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASS% %DB_NAME% < "%SQL_FILE%" 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] SQL import failed
    exit /b 1
)
echo [OK] SQL import completed

echo.
echo ============================================
echo   Database initialization complete!
echo ============================================
exit /b 0
