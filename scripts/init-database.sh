#!/bin/bash

echo "============================================"
echo "  pure-mall-backend - Database Init"
echo "============================================"
echo ""

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

DB_HOST="localhost"
DB_PORT="3306"
DB_NAME="pure_mall"
DB_USER="root"
DB_PASS=""

# 从 scripts/.env 加载数据库凭据（该文件已被 .gitignore 忽略，参考 .env.example）
if [ -f "$SCRIPT_DIR/.env" ]; then
    set -a
    . "$SCRIPT_DIR/.env"
    set +a
fi

if [ -z "$DB_PASS" ]; then
    echo "[ERROR] 未配置 DB_PASS。"
    echo "        请复制 scripts/.env.example 为 scripts/.env 并填入数据库密码。"
    exit 1
fi

SQL_FILE="$PROJECT_DIR/mall_database.sql"

# ──────────────────────────────────────────────
# 1. Find mysql command
# ──────────────────────────────────────────────
find_mysql() {
    if command -v mysql &>/dev/null; then
        MYSQL_CMD="mysql"
        return
    elif command -v mysql.exe &>/dev/null; then
        MYSQL_CMD="mysql.exe"
        return
    fi
    for DRIVE in /c /d /e /f /g; do
        for VER in "8.0" "8.4" "9.0" "5.7" "5.6"; do
            for BASE in "$DRIVE/Program Files/MySQL" "$DRIVE/MySQL"; do
                if [ -f "$BASE/MySQL Server $VER/bin/mysql.exe" ]; then
                    MYSQL_CMD="$BASE/MySQL Server $VER/bin/mysql.exe"
                    MYSQL_BIN_DIR="$BASE/MySQL Server $VER/bin"
                    return
                fi
            done
        done
    done
    MYSQL_CMD=""
}

MYSQL_CMD=""
MYSQL_BIN_DIR=""
find_mysql

if [ -z "$MYSQL_CMD" ]; then
    echo "[ERROR] MySQL client not found. Please install MySQL or add it to PATH."
    exit 1
fi
echo "[INFO] Using MySQL client: $MYSQL_CMD"

# ──────────────────────────────────────────────
# 2. Ensure MySQL is running
# ──────────────────────────────────────────────
echo "[1/4] Checking MySQL connection..."

if "$MYSQL_CMD" -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASS" -e "SELECT 1;" >/dev/null 2>&1; then
    echo "[OK] MySQL connection successful"
else
    echo "[WARN] MySQL is not running. Attempting to start..."

    # Find mysqld.exe
    MYSQLD_CMD=""
    if [ -n "$MYSQL_BIN_DIR" ] && [ -f "$MYSQL_BIN_DIR/mysqld.exe" ]; then
        MYSQLD_CMD="$MYSQL_BIN_DIR/mysqld.exe"
    elif [ -f "$(dirname "$(dirname "$MYSQL_CMD")")/bin/mysqld.exe" ]; then
        MYSQLD_CMD="$(dirname "$(dirname "$MYSQL_CMD")")/bin/mysqld.exe"
    fi

    STARTED=false

    # Method 1: Try net start (needs admin)
    for SVC in MySQL MySQL80 Mysql wampmysqld wampmariadb MariaDB; do
        SC_STATE=$(sc query "$SVC" 2>/dev/null | grep -i "STATE" || true)
        if echo "$SC_STATE" | grep -q "STOPPED"; then
            echo "[INFO] Found stopped service: $SVC, trying net start..."
            net start "$SVC" 2>/dev/null && STARTED=true && break
        elif echo "$SC_STATE" | grep -q "RUNNING"; then
            STARTED=true && break
        fi
    done

    # Method 2: Try PowerShell elevated
    if [ "$STARTED" = false ]; then
        echo "[INFO] Trying elevated PowerShell..."
        for SVC in MySQL MySQL80 Mysql; do
            powershell.exe -Command "Start-Process -FilePath 'net' -ArgumentList 'start','$SVC' -Verb RunAs -Wait" 2>/dev/null && STARTED=true && break
        done
    fi

    # Method 3: Start mysqld directly (no admin needed)
    if [ "$STARTED" = false ] && [ -n "$MYSQLD_CMD" ]; then
        echo "[INFO] Starting MySQL directly (console mode, no admin required)..."
        "$MYSQLD_CMD" --console &
        STARTED=true
    fi

    # Wait for MySQL to be ready
    if [ "$STARTED" = true ]; then
        echo "[INFO] Waiting for MySQL to accept connections..."
        for i in $(seq 1 20); do
            if "$MYSQL_CMD" -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASS" -e "SELECT 1;" >/dev/null 2>&1; then
                echo "[OK] MySQL is now running"
                break
            fi
            sleep 1
        done
    fi

    # Final verification
    if "$MYSQL_CMD" -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASS" -e "SELECT 1;" >/dev/null 2>&1; then
        echo "[OK] MySQL connection successful"
    else
        echo ""
        echo "[ERROR] Cannot connect to MySQL ($DB_HOST:$DB_PORT)."
        echo "        Please start MySQL manually:"
        echo "          Option A: Run CMD as Admin:  net start MySQL"
        echo "          Option B: Start directly:     mysqld --console"
        echo "        Credentials:  user=$DB_USER  password=***"
        exit 1
    fi
fi

# ──────────────────────────────────────────────
# 3. Create database
# ──────────────────────────────────────────────
echo ""
echo "[2/4] Creating database '$DB_NAME' (if not exists)..."
"$MYSQL_CMD" -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASS" --default-character-set=utf8mb4 -e "CREATE DATABASE IF NOT EXISTS $DB_NAME DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" 2>/dev/null
echo "[OK] Database ready: $DB_NAME"

# ──────────────────────────────────────────────
# 4. Import SQL file (using SOURCE command to avoid encoding issues)
# ──────────────────────────────────────────────
echo ""
echo "[3/4] Checking SQL file..."
if [ ! -f "$SQL_FILE" ]; then
    echo "[ERROR] SQL file not found: $SQL_FILE"
    exit 1
fi
echo "[OK] SQL file found: $SQL_FILE"

echo ""
echo "[4/4] Importing SQL file..."
# Convert path for MySQL SOURCE command (must use forward slashes)
SQL_FILE_FORWARDSLASH="$(echo "$SQL_FILE" | sed 's|\\|/|g' | sed 's|^/\([a-zA-Z]\)/|\1:/|')"
echo "[INFO] Using SOURCE command to preserve UTF-8 encoding..."

if "$MYSQL_CMD" -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASS" --default-character-set=utf8mb4 -e "
SET FOREIGN_KEY_CHECKS = 0;
SET sql_mode = 'NO_ENGINE_SUBSTITUTION';
USE $DB_NAME;
SOURCE $SQL_FILE_FORWARDSLASH;
SET FOREIGN_KEY_CHECKS = 1;" 2>&1; then
    echo "[OK] SQL import completed"
else
    echo "[ERROR] SQL import failed."
    exit 1
fi

echo ""
echo "============================================"
echo "  Database initialization complete!"
echo "============================================"
