FROM gradle:8.8-jdk17 AS build

WORKDIR /workspace

COPY src ./src
COPY build.gradle ./build.gradle
COPY settings.gradle ./settings.gradle

RUN gradle clean build -x test -x jacocoTestCoverageVerification

FROM amazoncorretto:17-alpine

WORKDIR /app

COPY --from=build /workspace/build/libs/*.jar ./chat-feed.jar

ENTRYPOINT ["java", "-jar", "chat-feed.jar"]