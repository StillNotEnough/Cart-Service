FROM openjdk:17-jdk-slim-buster
WORKDIR /app
COPY /target/cart-service-0.0.1-SNAPSHOT.jar /app/amazing-shop-cart-service.jar
ENTRYPOINT ["java", "-jar", "amazing-shop-cart-service.jar"]