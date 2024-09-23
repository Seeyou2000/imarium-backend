FROM openjdk:17
VOLUME /tmp
COPY build/libs/shop-0.0.1-SNAPSHOT.jar shop-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/shop-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]