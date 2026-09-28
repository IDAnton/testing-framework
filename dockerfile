
FROM maven:3.9.9-eclipse-temurin-21-alpine AS deps
WORKDIR /app
COPY pom.xml .


RUN mvn dependency:go-offline -B

FROM eclipse-temurin:25-jdk-noble
WORKDIR /app

RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

COPY --from=deps /root/.m2 /root/.m2
COPY pom.xml .
COPY src ./src

CMD ["mvn", "test"]
