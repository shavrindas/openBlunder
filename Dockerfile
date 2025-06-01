# 1단계: Maven 빌드
FROM maven:3.8.5-openjdk-17 AS builder

WORKDIR /app

# 소스 복사
COPY . .

# 빌드 실행
RUN ./mvnw clean package -DskipTests

# 2단계: 실행을 위한 최소 이미지
FROM openjdk:17-alpine

# 빌드 산출물 복사
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
