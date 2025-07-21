FROM eclipse-temurin:21-jre
COPY build/libs/brito_andres_shipflow-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]