# Use official OpenJDK 21 image
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy jar file (pastikan sudah di mvn clean install)
COPY target/*.jar app.jar

# Expose port (ubah sesuai service)
EXPOSE 8082

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
