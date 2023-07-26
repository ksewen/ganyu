#!/bin/sh

java -server ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /ganyu/ganyu-0.0.1-SNAPSHOT.jar
