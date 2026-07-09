#!/bin/bash

echo "============================================"
echo "  pure-mall-backend - Start Backend"
echo "============================================"
echo ""

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

cd "$PROJECT_DIR"

# ──────────────────────────────────────────────
# 1. Check Java
# ──────────────────────────────────────────────
echo "[1/4] Checking Java environment..."
if ! command -v java &>/dev/null; then
    echo "[ERROR] Java not found. Please install JDK 1.8+ and set JAVA_HOME."
    exit 1
fi
echo "[INFO] $(java -version 2>&1 | head -n 1)"

# ──────────────────────────────────────────────
# 2. Check Maven Wrapper
# ──────────────────────────────────────────────
echo "[2/4] Checking Maven Wrapper..."
if [ -f "$PROJECT_DIR/mvnw" ] || [ -f "$PROJECT_DIR/mvnw.cmd" ]; then
    echo "[OK] Maven Wrapper ready"
else
    echo "[ERROR] Maven Wrapper not found in project root."
    exit 1
fi

# ──────────────────────────────────────────────
# 3. Handle port 8080 conflict
# ──────────────────────────────────────────────
echo "[3/4] Checking port 8080..."
if command -v netstat &>/dev/null; then
    PORT_PID=$(netstat -ano 2>/dev/null | grep ":8080" | grep "LISTENING" | awk '{print $5}' | head -1)
    if [ -n "$PORT_PID" ]; then
        echo "[WARN] Port 8080 is in use by PID $PORT_PID. Killing..."
        taskkill //F //PID "$PORT_PID" 2>/dev/null || true
        sleep 2
        echo "[OK] Port 8080 freed"
    else
        echo "[OK] Port 8080 is available"
    fi
else
    echo "[INFO] Skipped (netstat not available)"
fi

# ──────────────────────────────────────────────
# 4. Package and start
# ──────────────────────────────────────────────
echo "[4/4] Building and starting application..."
echo "[INFO] Packaging (skip tests)..."
./mvnw package -DskipTests -q 2>&1

JAR_FILE=$(ls "$PROJECT_DIR/target/"pure-mall-backend-*.jar 2>/dev/null | head -1)
if [ -z "$JAR_FILE" ]; then
    echo "[ERROR] Build failed — jar file not found."
    exit 1
fi

echo "[INFO] Jar: $(basename "$JAR_FILE")"
echo "[INFO] Server URL:  http://localhost:8080"
echo "[INFO] Swagger UI:  http://localhost:8080/swagger-ui/index.html"
echo "[INFO] Press Ctrl+C to stop"
echo ""

exec java -jar "$JAR_FILE"
