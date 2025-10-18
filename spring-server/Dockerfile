FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY target/message-server-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]