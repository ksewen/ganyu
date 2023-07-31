#!/bin/sh

wait_for() {
  echo Waiting for ${DEPENDENCY} to listen on ${DEPENDENCY_PORT}...
  while ! nc -z ${DEPENDENCY} ${DEPENDENCY_PORT}; do
    echo waiting...
    sleep 5
  done
}

start_service() {
  java -server ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /ganyu/ganyu-0.0.1-SNAPSHOT.jar
}

if [ ${DEPENDENCY} ] && [ ${DEPENDENCY_PORT} ]; then
  wait_for
fi

start_service
