#!/usr/bin/env bash
set -e

SOURCE="app/src/main/res/values/strings.xml"
TARGET="app/src/main/res/values-en/strings.xml"

if [ ! -f "$SOURCE" ]; then
  echo "❌ Source not found: $SOURCE"
  exit 0
fi

# 2. Ensure target directory exists
TARGET_DIR="$(dirname "$TARGET")"
mkdir -p "$TARGET_DIR"

# 3. If target exists, check diff
if [ -f "$TARGET" ]; then
  if cmp -s "$SOURCE" "$TARGET"; then
    echo "ℹ️ No changes detected, skipping sync"
    exit 0
  fi
fi

# Check if files are identical
if cmp -s "$SOURCE" "$TARGET"; then
  echo "ℹ️ No changes detected, skipping sync"
  exit 0
fi

# Copy content only (overwrite, no recreation)
cat "$SOURCE" > "$TARGET"

echo "✅ Synced default values → values-en"