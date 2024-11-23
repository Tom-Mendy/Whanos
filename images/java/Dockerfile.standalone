FROM maven:3.9.6 AS dependencies

WORKDIR /app

COPY app/pom.xml /app

RUN mvn dependency:go-offline


FROM dependencies AS build

WORKDIR /app

COPY ./app /app

RUN mvn package


FROM openjdk:21-jdk AS standalone

WORKDIR /app

COPY --from=build /app/target/app.jar app.jar

CMD ["java", "-jar", "app.jar"]