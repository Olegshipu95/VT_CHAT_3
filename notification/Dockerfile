# Этап сборки
FROM gradle:8.8-jdk17 AS build

# Рабочая директория для сборки
WORKDIR /workspace

# Копируем только файлы, необходимые для сборки
COPY build.gradle settings.gradle ./
COPY src ./src

# Сборка проекта и проверка, что артефакт создан
RUN gradle clean build -x test -x jacocoTestCoverageVerification

# Этап запуска
FROM amazoncorretto:17-alpine

# Рабочая директория для приложения
WORKDIR /app

# Копируем собранный JAR-файл из предыдущего этапа
COPY --from=build /workspace/build/libs/*.jar ./chat-notification.jar

# Точка входа
ENTRYPOINT ["java", "-jar", "chat-notification.jar"]
