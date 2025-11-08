#!/bin/bash
set -e

PROJECT_DIR="/home/ubuntu/BackEnd"
JAR_NAME="$PROJECT_DIR/backend.jar"
LOG_DIR="$PROJECT_DIR/logs"
PROFILE="prod"

HEALTH_CHECK_URL="http://localhost:8080/actuator/health"
MAX_ATTEMPTS=12
WAIT_SECONDS=5

cd $PROJECT_DIR

echo "========================================="
echo "Start Deployment for PROFILE: $PROFILE"
echo "========================================="

echo ">> Check running application on port 8080"
CURRENT_PID=$(lsof -t -i:8080 || true)

if [ -z "$CURRENT_PID" ]; then
    echo "No running application found."
else
    echo "Terminate running application: $CURRENT_PID"
    sudo kill -15 $CURRENT_PID
    sleep 5

    if ps -p $CURRENT_PID > /dev/null; then
        echo "Application did not terminate gracefully. Forcing kill."
        sudo kill -9 $CURRENT_PID
        sleep 2
    fi
    echo "Application terminated."
fi

echo ">> Deploy new application"

mkdir -p "$LOG_DIR"
LOG_PATH="$LOG_DIR/server_log_$(date +%Y-%m-%d).txt"

nohup java -jar -Dspring.profiles.active=$PROFILE "$JAR_NAME" >> "$LOG_PATH" 2>&1 &

# --- Health Check 로직 시작 ---
echo ">> Waiting for application to start... (Max $MAX_ATTEMPTS attempts)"

START_SUCCESS=false
for i in $(seq 1 $MAX_ATTEMPTS); do
    echo ">> Health check attempt $i/$MAX_ATTEMPTS..."
    HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" $HEALTH_CHECK_URL)

    if [ "$HTTP_CODE" -eq 200 ]; then
        HEALTH_STATUS=$(curl -s $HEALTH_CHECK_URL | grep -o '"status":"UP"')

        if [ -n "$HEALTH_STATUS" ]; then
            echo ">> Application is UP and healthy."
            START_SUCCESS=true
            break
        else
            echo ">> HTTP 200 OK, but status is not 'UP'. Retrying in $WAIT_SECONDS sec..."
        fi
    else
        echo ">> Application not responding (HTTP Code: $HTTP_CODE). Retrying in $WAIT_SECONDS sec..."
    fi

    sleep $WAIT_SECONDS
done

if [ "$START_SUCCESS" = false ]; then
    echo ">> ERROR: New application failed to start. Please check the log at $LOG_PATH"
    tail -n 50 "$LOG_PATH"
    exit 1
fi

NEW_PID=$(lsof -t -i:8080)
echo ">> New application started successfully with PID: $NEW_PID"

echo ">> Clean up old log files (older than 7 days)"
find $LOG_DIR -type f -name "*.txt" -mtime +7 -delete

echo "========================================="
echo "Deployment complete for PROFILE: $PROFILE"
echo "========================================="
