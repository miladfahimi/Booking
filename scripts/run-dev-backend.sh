#!/bin/bash

# Function to display usage
usage() {
  echo "Usage: $0 [-b|--build] [-r|--run] [-br|--build-run]"
  exit 1
}

# Initialize flags
BUILD=false
RUN=false

while [[ "$1" != "" ]]; do
  case $1 in
    -b | --build )
      BUILD=true
      ;;
    -r | --run )
      RUN=true
      ;;
    -br | --build-run )
      BUILD=true
      RUN=true
      ;;
    -h | --help )
      usage
      ;;
    * )
      usage
      ;;
  esac
  shift
done

# Navigate to the project directory on the C drive
cd /home/TennisTime/tennistime/backend

# Run mvn clean install if build flag is set
if [ "$BUILD" = true ]; then
  echo "Running mvn clean install..."
  mvn clean install
fi

# Run the Java application if the run flag is set
if [ "$RUN" = true ]; then
  echo "Running the application..."
  java -jar backend/target/backend-0.0.1-SNAPSHOT-exec.jar --spring.profiles.active=dev
fi

# If neither build nor run was specified, show usage
if [ "$BUILD" = false ] && [ "$RUN" = false ]; then
  usage
fi
