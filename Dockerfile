FROM maven:latest

WORKDIR /cdn-backend
COPY . .
RUN mvn clean install -Dmaven.test.skip=true
CMD mvn spring-boot:run