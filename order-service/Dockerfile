# Gunakan base image Java yang sesuai dengan versi proyek Anda (misalnya Java 21)
FROM openjdk:21-jdk-slim

# Tentukan argumen file JAR hasil build Maven
ARG JAR_FILE=target/*.jar

# Salin file JAR yang sudah di-build ke dalam container
# Nama file JAR harus sesuai dengan <finalName> di pom.xml
COPY ${JAR_FILE} app.jar

# Tentukan perintah yang dijalankan saat container start
ENTRYPOINT ["java","-jar","/app.jar"]