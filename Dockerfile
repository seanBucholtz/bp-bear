# ---- Build Stage ----
FROM azul/zulu-openjdk:23-jdk AS build

# Set working directory
WORKDIR /app

# Install sbt
RUN apt-get update && \
    apt-get install -y curl unzip && \
    curl -L -o sbt.zip https://github.com/sbt/sbt/releases/download/v1.9.4/sbt-1.9.4.zip && \
    unzip sbt.zip -d /usr/local && \
    rm sbt.zip

ENV PATH="/usr/local/sbt/bin:$PATH"

# Copy build files
COPY build.sbt project/ /app/

# Download dependencies
RUN sbt update

# Copy source code
COPY . /app/

# Compile and package the application using sbt-assembly
RUN sbt assembly

# ---- Runtime Stage ----
FROM azul/zulu-openjdk:23-jdk

WORKDIR /app

# Copy the fat jar from build stage
COPY --from=build /app/target/scala-2.13/bpbear-backend-assembly-0.1.0.jar ./bpbear-backend.jar

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "bpbear-backend.jar"]
