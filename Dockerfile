# 1. 빌드 단계: Gradle을 이용하여 .jar 파일 생성
FROM gradle:7.6.0-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle build -x test  # 테스트 제외하고 빌드, 필요 시 -x test 제거

# 2. 실행 단계: 빌드된 .jar 파일로 애플리케이션 실행
FROM --platform=linux/amd64 openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]