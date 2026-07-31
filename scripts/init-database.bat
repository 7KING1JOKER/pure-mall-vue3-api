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
set "MYSQL_SERVICE=MySQL"

echo [1/4] Checking MySQL service '%MYSQL_SERVICE%'...
sc query %MYSQL_SERVICE% >nul 2>&1
if %errorlevel% neq 0 goto :svc_missing
sc query %MYSQL_SERVICE% | findstr /i "RUNNING" >nul 2>&1
if %errorlevel% equ 0 goto :svc_running
echo [INFO] Service is stopped. Starting '%MYSQL_SERVICE%'...
net start %MYSQL_SERVICE% >nul 2>&1
if %errorlevel% neq 0 goto :svc_failed
echo [OK] MySQL service started
goto :svc_done
:svc_missing
echo [WARN] Service '%MYSQL_SERVICE%' not found.
echo        If MySQL runs under another name like wampmysqld, set MYSQL_SERVICE in this script.
goto :svc_done
:svc_running
echo [OK] MySQL service is already running
goto :svc_done
:svc_failed
echo [ERROR] Failed to start MySQL service '%MYSQL_SERVICE%'.
echo         A Windows service can only be started with administrator privileges.
echo         Please right-click start-all.bat and select "Run as administrator",
echo         or run this in an admin terminal first:  net start %MYSQL_SERVICE%
exit /b 1
:svc_done

echo.
echo [2/4] Checking MySQL connection...
mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASS% -e "SELECT 1;" >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Cannot connect to MySQL. Please check if MySQL service is running.
    echo         Host: %DB_HOST%:%DB_PORT%
    echo         User: %DB_USER%
    exit /b 1
)
echo [OK] MySQL connection successful

echo.
echo [3/4] Creating database (if not exists)...
mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASS% -e "CREATE DATABASE IF NOT EXISTS %DB_NAME% DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Failed to create database
    exit /b 1
)
echo [OK] Database ready: %DB_NAME%

echo.
echo [4/4] Importing SQL file...
if not exist "%SQL_FILE%" (
    echo [ERROR] SQL file not found: %SQL_FILE%
    exit /b 1
)
chcp 65001 >nul 2>&1
mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASS% --default-character-set=utf8mb4 %DB_NAME% < "%SQL_FILE%" 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] SQL import failed
    echo [INFO] Try running manually in cmd with administrator:
    echo        chcp 65001
    echo        mysql -u root -p123456 --default-character-set=utf8mb4 pure_mall
    echo        SOURCE %SQL_FILE%;
    exit /b 1
)
echo [OK] SQL import completed

echo.
echo ============================================
echo   Database initialization complete!
echo ============================================
exit /b 0
