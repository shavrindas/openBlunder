# 1단계: Maven 빌드
FROM maven:3.8.8-openjdk-17 AS builder

WORKDIR /app

# 소스 복사
COPY . .

# 빌드 실행 (mvnw 파일 사용 가능, 또는 직접 mvn 사용)
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# 2단계: 실행을 위한 최소 이미지
FROM openjdk:17-alpine

# 빌드 산출물 복사 (jar 위치는 target 디렉토리 내에 있을 것)
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
