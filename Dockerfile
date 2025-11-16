
FROM maven:3.9.9 AS build
WORKDIR /app
COPY pom.xml .
COPY /src ./src
RUN mvn clean package -DskipTests

FROM bellsoft/liberica-openjdk-alpine:17
WORKDIR /app
COPY --from=build /app/target/car-rental-0.0.1-SNAPSHOT.jar car-rental.jar
EXPOSE 9000

ENTRYPOINT ["java", "-jar", "car-rental.jar"]