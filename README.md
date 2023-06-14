# metadata_gen

Generating a property graph metadata.
Read more about the MMLKG project at [MMLKG website](https://mmlkg.uwb.edu.pl).

# JAR file usage

Build the project with `mvn clean package`.

```shell
java -jar target/metadata_gen-1.0-SNAPSHOT-jar-with-dependencies.jar <esx_mml> <config_file>
```

## Required arguments

* `<esx_mml>` - the path to the `esx_mml` directory

## Optional arguments

* `<config_file>` - the path to the TOML config file (if not specified, the default config file will be used)

# Docker usage

You need to have Docker installed on your machine to be able to build and run the Docker image.

## Build the Docker image

To build the Docker image, run the following command:

```shell
docker build -t metadata_gen .
```

## Run the Docker image

To run the Docker image, run the following command:

```shell
docker run --rm -v <data_path>:/app/data metadata_gen /app/data/esx_mml > metadata.xml
```

or

```shell
docker run --rm -v <data_path>:/app/data metadata_gen /app/data/esx_mml /app/data/config.toml > metadata.xml
```

Replace `<data_path>` with the path to your directory containing ESX MML files (required) and `config.toml` file (optional).
