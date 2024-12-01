FROM openjdk:17-alpine
ARG JAR_FILE=./build/libs/ComparusTestTask-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]