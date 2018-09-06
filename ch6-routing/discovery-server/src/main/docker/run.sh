#!/bin/sh
echo "********************************************************"
echo "Starting the Eureka Server"
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom -jar /usr/local/discovery-server/@project.build.finalName@.jar
