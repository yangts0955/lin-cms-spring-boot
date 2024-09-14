#bin/sh

docker-compose down -v
sh mvn clean -DskipTests=true install
export MYSQL_USER="neil_yang"
export MYSQL_PASSWORD="yang12345"
export MYSQL_ROOT_PASSWORD="PXCEa0Ld!"
docker-compose -f docker-compose.yml up --force-recreate --build -d
docker image prune --force
docker volume prune --force
