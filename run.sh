#bin/sh

docker-compose down -v
sh mvn clean -DskipTests=true install
export MYSQL_USER="alice"
export MYSQL_PASSWORD="123456"
export MYSQL_ROOT_PASSWORD="root"
docker-compose -f docker-compose.yml up --force-recreate --build -d
docker image prune --force
docker volume prune --force
