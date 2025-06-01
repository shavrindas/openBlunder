# 1단계: Gradle로 빌드
FROM gradle:8.2.1-jdk17 AS builder
COPY --chown=gradle:gradle . /home/gradle/project
WORKDIR /home/gradle/project
RUN gradle build --no-daemon

# 2단계: 실행을 위한 최소 이미지
FROM openjdk:17-alpine
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
