#!/bin/bash
set -e

# -------------------- Config --------------------
# Список сервісів з їх портами
declare -A SERVICES
SERVICES=(
  [config_service]=8888
  [api_gateway_service]=8080
  [discovery_service]=8761
  [main_service_old]=8082
  [notification_service]=8075
)

# Шляхи до env файлів (якщо є)
declare -A ENV_FILES
ENV_FILES=(
  [main_service_old]="./main_service_old/src/main/resources/.env"
  [notification_service]="./notification_service/src/main/resources/.env"
)

DOCKERFILE="Dockerfile.template"

# -------------------- Build --------------------
echo "Building all services..."
for module in "${!SERVICES[@]}"; do
  port=${SERVICES[$module]}
  echo "----------------------------------------"
  echo "Building module: $module on port $port"

  env_arg=""
  if [[ -n "${ENV_FILES[$module]}" ]]; then
    env_file=${ENV_FILES[$module]}
    if [[ ! -f "$env_file" ]]; then
      echo "Warning: env file $env_file not found!"
    fi
  fi

  docker build \
    --build-arg MODULE_PATH=$module \
    --build-arg MODULE_PORT=$port \
    -f $DOCKERFILE \
    -t $module .
done

# -------------------- Compose Up --------------------
echo "Starting all services via docker-compose..."
sudo docker compose up -d
