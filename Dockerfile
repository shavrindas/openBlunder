FROM gradle:8.2.1-jdk17 AS builder
COPY --chown=gradle:gradle . /home/gradle/project
WORKDIR /home/gradle/project

RUN chmod +x ./gradlew
RUN ./gradlew build --no-daemon

FROM openjdk:17-alpine
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
