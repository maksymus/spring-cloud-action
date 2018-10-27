#!/bin/sh

echo "********************************************************"
echo "Waiting for the eureka server to start on port $EUREKASERVER_PORT"
echo "********************************************************"
while ! `nc -z discovery-server  $EUREKASERVER_PORT`; do sleep 3; done
echo "******* Eureka Server has started"

echo "********************************************************"
echo "Waiting for the configuration server to start on port $CONFIGSERVER_PORT"
echo "********************************************************"
while ! `nc -z config-server $CONFIGSERVER_PORT`; do sleep 3; done
echo "*******  Configuration Server has started"

echo "********************************************************"
echo "Waiting for the ZIPKIN server to start  on port $ZIPKIN_PORT"
echo "********************************************************"
while ! `nc -z zipkin $ZIPKIN_PORT`; do sleep 10; done
echo "******* ZIPKIN has started"

echo "********************************************************"
echo "Starting Zuul Service with $CONFIGSERVER_URI"
echo "********************************************************"


java -Xmx256m                                                               \
     -Djava.security.egd=file:/dev/./urandom -Dserver.port=$SERVER_PORT     \
     -Deureka.client.serviceUrl.defaultZone=$EUREKASERVER_URI               \
     -Dspring.cloud.config.uri=$CONFIGSERVER_URI                            \
     -Dspring.profiles.active=$PROFILE                                      \
     -Dspring.zipkin.baseUrl=$ZIPKIN_URI                                    \
     -jar /usr/local/routing-server/@project.build.finalName@.jar