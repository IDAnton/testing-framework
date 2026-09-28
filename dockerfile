FROM eclipse-temurin:25-jdk-noble AS builder
WORKDIR /app
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*
COPY pom.xml .
COPY src ./src
CMD ["mvn", "test", "-Dspring.classformat.ignore=true -Dallure.results.directory=target/allure-results"]
