FROM maven:latest AS builder

WORKDIR /cdn-backend
COPY . .
RUN mvn clean install -Dmaven.test.skip=true

FROM amazoncorretto:17-alpine
WORKDIR /cdn-backend
COPY --from=builder /cdn-backend/target/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
