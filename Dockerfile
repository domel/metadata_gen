# metadata_gen Dockerfile
# 
# Maintainer: Łukasz Szeremeta
# Email: l.szeremeta.dev+mmlkg@gmail.com
# https://github.com/lszeremeta
#
# Usage:
# docker build -t metadata_gen .
# docker run --rm -v <data_path>:/app/data metadata_gen /app/data/esx_mml > metadata.xml
# docker run --rm -v <data_path>:/app/data metadata_gen /app/data/esx_mml /app/data/config.toml > metadata.xml
#
# Replace <data_path> with the path to your directory containing ESX MML files (required) and config.toml file (optional).

# Build stage
FROM maven:3.9.2-eclipse-temurin-17 as build
WORKDIR /app

# Copy the project files into the docker image (see .dockerignore)
COPY . .

# Build and rename jar, all in one layer
RUN mvn -B package --file=pom.xml \
    && mv target/metadata-gen-*-jar-with-dependencies.jar metadata-gen.jar

# Package stage
FROM gcr.io/distroless/java17-debian11
LABEL maintainer="Łukasz Szeremeta <l.szeremeta.dev+mmlkg@gmail.com>"

# Copy the jar file from the build stage
COPY --from=build /app/metadata-gen.jar /app/metadata-gen.jar

WORKDIR /app

ENTRYPOINT ["java", "-jar", "metadata-gen.jar"]