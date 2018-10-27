#!/bin/sh
echo "********************************************************"
echo "Waiting for the eureka server to start  on port $EUREKASERVER_PORT"
echo "********************************************************"
while ! `nc -z discovery-server $EUREKASERVER_PORT`; do sleep 3; done
echo ">>>>>>>>>>>> Eureka Server has started"

echo "********************************************************"
echo "Waiting for the database server to start on port $DATABASE_PORT"
echo "********************************************************"
while ! `nc -z database $DATABASE_PORT`; do sleep 3; done
echo "******** Database Server has started "

echo "********************************************************"
echo "Waiting for the configuration server to start on port $CONFIGSERVER_PORT"
echo "********************************************************"
while ! `nc -z config-server $CONFIGSERVER_PORT`; do sleep 3; done
echo "*******  Configuration Server has started"


echo "********************************************************"
echo "Starting Autentication Server"
echo "********************************************************"
java -Xmx128m -cp /usr/local/authentication-server/@project.build.finalName@.jar \
    -Djava.security.egd=file:/dev/./urandom                     \
    -Deureka.client.serviceUrl.defaultZone=$EUREKASERVER_URI    \
    -jar /usr/local/authentication-server/@project.build.finalName@.jar