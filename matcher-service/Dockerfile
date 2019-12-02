FROM oracle/graalvm-ce

WORKDIR /app

COPY target/matcher-service-0.0.1-SNAPSHOT.jar /app/app.jar
CMD ["java", "-jar", "app.jar"]
