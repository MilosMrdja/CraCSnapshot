#!/bin/bash
set -e

# 📂
CRAC_DIR="/crac-files"

# 🔍
APP_PID=$(jps -l | grep '\.jar' | awk '{print $1}')

if [ -z "$APP_PID" ]; then
  echo "[CraC] ❌ Java PID not found!"
  exit 1
fi

echo "[CraC] ✅ Found Java with PID=$APP_PID"

# 🧹
if [ -d "$CRAC_DIR" ]; then
  if [ "$(ls -A $CRAC_DIR)" ]; then
    echo "[CraC] ⚠️ Folder $CRAC_DIR has old files — deleting..."
    rm -rf "$CRAC_DIR"/*
  else
    echo "[CraC] ✅ Folder $CRAC_DIR is already empty."
  fi
else
  echo "[CraC] 📁 Folder $CRAC_DIR does not exist — creating..."
  mkdir -p "$CRAC_DIR"
fi

# 📸
echo "[CraC] 📸 Create snapshot to $CRAC_DIR ..."
jcmd $APP_PID JDK.checkpoint || {
  echo "[CraC] ❌ Snapshot failed!"
  exit 1
}

echo "[CraC] ✅ Snapshot is successfully created and saved to $CRAC_DIR."
