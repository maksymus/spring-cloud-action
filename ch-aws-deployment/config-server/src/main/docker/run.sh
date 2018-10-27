#!/bin/sh
echo "********************************************************"
echo "Waiting for the eureka server to start  on port $EUREKASERVER_PORT"
echo "********************************************************"
while ! `nc -z discovery-server $EUREKASERVER_PORT`; do sleep 3; done
echo ">>>>>>>>>>>> Eureka Server has started"

echo "********************************************************"
echo "Starting Configuration Server"
echo "********************************************************"
java -Xmx128m -cp /usr/local/config-server/@project.build.finalName@.jar \
    -Djava.security.egd=file:/dev/./urandom                     \
    -Deureka.client.serviceUrl.defaultZone=$EUREKASERVER_URI    \
    -jar /usr/local/config-server/@project.build.finalName@.jar