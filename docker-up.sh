#!/bin/bash
set -e

# -------------------- Config --------------------
declare -A SERVICES
SERVICES=(
  [main_service_old]=8081
  [notification_service]=8075
  [config_service]=8888
  [api_gateway_service]=8080
)

declare -A ENV_FILES
ENV_FILES=(
  [main_service_old]="./main_service_old/src/main/resources/.env"
  [notification_service]="./notification_service/src/main/resources/.env"
)

DOCKERFILE="Dockerfile.template"

# -------------------- Build --------------------
echo "Building all modules..."
for module in "${!SERVICES[@]}"; do
  port=${SERVICES[$module]}
  echo "----------------------------------------"
  echo "Building module: $module on port $port"

  docker build \
    --build-arg MODULE_PATH=$module \
    --build-arg MODULE_PORT=$port \
    -f $DOCKERFILE \
    -t $module .
done

# -------------------- Compose Up --------------------
echo "Starting all services via docker-compose..."
sudo docker compose up -d