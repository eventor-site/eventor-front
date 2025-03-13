# OpenJDK 21 기반 이미지 사용
FROM eclipse-temurin:21-jdk

WORKDIR /app

ARG PROD_SERVER_PORT=${PROD_SERVER_PORT}

ENV PROD_SERVER_PORT=${PROD_SERVER_PORT}

# JAR 파일 복사
COPY target/eventor-front-0.0.1-SNAPSHOT.jar eventor-front.jar

# 환경변수 설정 (Spring Boot에 직접 전달)
ENV SPRING_APPLICATION_JSON='{"server.port":"'"${PROD_SERVER_PORT}"'"}'

# 애플리케이션 실행
CMD ["java", "-Dspring.profiles.active=prod", "-jar", "eventor-front.jar"]