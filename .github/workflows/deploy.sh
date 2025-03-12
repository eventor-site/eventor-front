set -e

ERR_MSG=''

trap 'echo "Error Occurred: $ERR_MSG. Exiting deploy script."; exit 1' ERR

if sudo docker ps --filter "name=blue" --format '{{.ID}}' | grep -E .; then
  RUN_TARGET="green"
  STOP_TARGET="blue"
  WAS_RUN_PORT=8082
  WAS_STOP_PORT=8081
else
  RUN_TARGET="blue"
  STOP_TARGET="green"
  WAS_RUN_PORT=8081
  WAS_STOP_PORT=8082
fi

echo "The $STOP_TARGET version is currently running on the server. Starting the $RUN_TARGET version."

DOCKER_COMPOSE_FILE="compose.$RUN_TARGET-deploy.yml"

sudo docker-compose -f "$DOCKER_COMPOSE_FILE" pull
sudo docker-compose -f "$DOCKER_COMPOSE_FILE" up -d


echo "Health check passed. Reloading nginx to transfer traffic from $STOP_TARGET to $RUN_TARGET."

NGINX_ID=$(sudo docker ps --filter "name=nginx" --quiet)
NGINX_CONFIG="/etc/nginx/conf.d/default.conf"

sudo docker exec $NGINX_ID /bin/bash -c "sed -i 's/was-$STOP_TARGET:$WAS_STOP_PORT/was-$RUN_TARGET:$WAS_RUN_PORT/' $NGINX_CONFIG"
sudo docker exec $NGINX_ID /bin/bash -c "sed -i 's/signal-$STOP_TARGET:$((WAS_STOP_PORT + 1))/signal-$RUN_TARGET:$((WAS_RUN_PORT + 1))/' $NGINX_CONFIG"
sudo docker exec $NGINX_ID /bin/bash -c "nginx -s reload"

echo "Terminating the $STOP_TARGET applications."

STOP_CONTAINER_ID=$(sudo docker ps --filter "name=$STOP_TARGET" --quiet)
if [ -n "$STOP_CONTAINER_ID" ]; then
  sudo docker stop $STOP_CONTAINER_ID
  sudo docker rm $STOP_CONTAINER_ID
  sudo docker image prune -af
fi