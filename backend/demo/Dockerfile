FROM eclipse-temurin:19-jdk-alpine As Builder
WORKDIR /project
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew assemble

FROM eclipse-temurin:19-jre-alpine
RUN apk add dumb-init
WORKDIR /app
RUN addgroup --system javauser && adduser -S -s /usr/sbin/nologin -G javauser javauser
COPY --from=Builder /project/build/libs/2cha-demo.jar app.jar
RUN chown -R javauser:javauser /app
USER javauser
CMD ["dumb-init", "java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
# TODO: inject configurations