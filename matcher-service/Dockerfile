FROM springci/graalvm-ce:java11-0.12.x

WORKDIR /app

COPY target/matcher-service-0.0.3-SNAPSHOT.jar /app/app.jar
CMD ["java", "-jar", "app.jar"]
