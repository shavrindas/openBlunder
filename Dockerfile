# 1단계: 빌드
FROM maven:3.9.4-eclipse-temurin-21-alpine AS builder

WORKDIR /app
COPY . .
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

# 2단계: 실행
FROM eclipse-temurin:21-jdk-alpine

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
