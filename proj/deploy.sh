#!/bin/bash
cd /srv/spring_remind
COMPOSE_PROD_PATH="docker-compose-prod.yml"
NGINX_AVAILABLE_DIR="conf-available"
NGINX_CONF_DIR="./nginx/conf.d"
DELAY=5
NGINX_CONTAINER="nginx"
HEALTH_DELAY=30

BLUE_CT=$(docker ps --filter "name=app-blue" --filter "status=running" --format "{{.ID}}")

if [[ -n "$BLUE_CT" ]]; then
  echo "=== BLUE -> GREEN ==="
  CURRENT_ENV='app-blue'
  NEW_ENV='app-green'
  NEW_NGINX_CONF='green.conf'
  HC_ENDPOINT="http://localhost:8001/swagger-ui/index.html"
else
  echo "=== GREEN -> BLUE ==="
  CURRENT_ENV='app-green'
  NEW_ENV='app-blue'
  NEW_NGINX_CONF='blue.conf'
  HC_ENDPOINT="http://localhost:8000/swagger-ui/index.html"
fi

echo "===================="
echo "Building Dockerfile: $NEW_ENV"
docker-compose -f $COMPOSE_PROD_PATH build $NEW_ENV
echo "===================="

echo "===================="
echo "Pulling the new env image: $NEW_ENV"
docker-compose -f $COMPOSE_PROD_PATH pull $NEW_ENV
echo "===================="

echo "===================="
echo "Building the new image: $NEW_ENV with no cache"
docker-compose -f $COMPOSE_PROD_PATH build --no-cache $NEW_ENV
echo "===================="

echo "===================="
echo "Starting new env: $NEW_ENV"
docker-compose -f $COMPOSE_PROD_PATH up -d --no-deps $NEW_ENV
echo "===================="

sleep $DELAY

echo "===================="
echo "Waiting for the new env to be healthy"
for i in $(seq 1 $HEALTH_DELAY); do
  HTTP_STATUS=$(docker-compose -f $COMPOSE_PROD_PATH exec -T $NEW_ENV curl -s -o /dev/null -w "%{http_code}" $HC_ENDPOINT)

  echo "[$i/$HEALTH_DELAY] Health check HTTP status: $HTTP_STATUS"

    if [ "$HTTP_STATUS" == "200" ]; then
      echo "Health check passed!"
      break
    else
      echo "Waiting for service to be ready..."
      sleep 2
    fi
done
echo "===================="

if [[ "$HTTP_STATUS" -ne 200 ]]; then
  echo "===================="
  echo "New environment did not become healthy in time."
  echo "===================="
  exit 1
fi

echo "===================="
docker-compose -f $COMPOSE_PROD_PATH exec "$NGINX_CONTAINER" ln -sf "/etc/nginx/conf-available/$NEW_NGINX_CONF" "/etc/nginx/conf.d/active.conf"
echo "===================="

echo "===================="
echo "Reloading nginx.."
docker-compose -f $COMPOSE_PROD_PATH exec "$NGINX_CONTAINER" nginx -s reload
echo "===================="

echo "===================="
echo "Stopping and removing the old environment: $CURRENT_ENV"
docker-compose -f $COMPOSE_PROD_PATH stop "$CURRENT_ENV"
docker-compose -f $COMPOSE_PROD_PATH rm -f "$CURRENT_ENV"
echo "===================="

echo "배포 완료!"