FROM amazoncorretto:17

WORKDIR /src/main
EXPOSE 10100
ARG JAR_FILE=./target/latticy-*.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]
