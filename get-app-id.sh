#!/bin/bash

# Quick script to get your App ID using API token
# Usage: ./get-app-id.sh YOUR_API_TOKEN

if [ -z "$1" ]; then
    echo "Usage: $0 YOUR_API_TOKEN"
    echo "This will list all your apps and their IDs"
    exit 1
fi

API_TOKEN="$1"

echo "🔍 Fetching your Codemagic apps..."

/data/data/com.termux/files/usr/bin/curl -s \
  -H "Content-Type: application/json" \
  -H "x-auth-token: $API_TOKEN" \
  https://api.codemagic.io/apps | \
  /data/data/com.termux/files/usr/bin/grep -E '"appName"|"_id"' | \
  /data/data/com.termux/files/usr/bin/sed 's/.*"appName": "\([^"]*\)".*/App: \1/' | \
  /data/data/com.termux/files/usr/bin/sed 's/.*"_id": "\([^"]*\)".*/ID:  \1/'

echo ""
echo "Look for 'android-snake-game' and copy the ID below it!"