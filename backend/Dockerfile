# Step 1: Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:17-jre-alpine

# Step 2: Set the working directory in the container
WORKDIR /app

# Step 3: Copy the pre-built jar into the container
# We use a wildcard to handle different version names
COPY build/libs/*.jar app.jar

# Step 4: Expose the port the app runs on
EXPOSE 8080

# Step 5: Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=8080"]
