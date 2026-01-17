FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy source
COPY . .

# Install Maven
RUN apt-get update && apt-get install -y maven

# Build Spring Boot jar
RUN mvn clean package -DskipTests

# Rename jar to a fixed name
RUN mv target/*SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
