FROM eclipse-temurin:17-jdk-focal AS build

ARG MODULE

WORKDIR /dist
COPY . .
RUN --mount=type=cache,target=/root/.gradle ./gradlew --no-daemon -i  clean :${MODULE}:build

FROM gcr.io/distroless/java17-debian11

ARG MODULE

COPY --from=build /dist/${MODULE}/build/libs/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "./app.jar"]
