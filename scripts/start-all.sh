#!/bin/bash
set -e

echo "============================================"
echo "  pure-mall-backend - One-Click Start"
echo "============================================"
echo ""

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

echo "============================================"
echo "  Step 1: Initialize Database"
echo "============================================"
echo ""
bash "$SCRIPT_DIR/init-database.sh"

echo ""
echo "============================================"
echo "  Step 2: Start Backend Server"
echo "============================================"
echo ""
exec bash "$SCRIPT_DIR/start-backend.sh"
