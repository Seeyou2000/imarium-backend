FROM openjdk:17
WORKDIR /app
COPY . .
RUN ./gradlew build
COPY build/libs/shop-0.0.1-SNAPSHOT.jar shop-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "shop-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]
