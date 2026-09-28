FROM maven:3.9.6-eclipse-temurin-25-alpine AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src

CMD ["mvn", "clean", "test"]
