FROM maven:3.9.6 AS dependencies

WORKDIR /app

COPY app/pom.xml /app

RUN mvn dependency:go-offline

COPY . /app

FROM dependencies AS build

WORKDIR /app

RUN mvn package

FROM openjdk:21-jdk AS standalone

WORKDIR /app

COPY --from=build /app/target/app.jar /app/app.jar

CMD ["java", "-jar", "app.jar"]