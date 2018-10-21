#!/bin/sh

echo "********************************************************"
echo "Waiting for the eureka server to start on port $EUREKASERVER_PORT"
echo "********************************************************"
while ! `nc -z discovery-server  $EUREKASERVER_PORT`; do sleep 3; done
echo "******* Eureka Server has started"

echo "********************************************************"
echo "Waiting for the configuration server to start on port $CONFIGSERVER_PORT"
echo "********************************************************"
while ! `nc -z config-server $CONFIGSERVER_PORT `; do sleep 3; done
echo ">>>>>>>>>>>> Configuration Server has started"

echo "********************************************************"
echo "Waiting for the database server to start on port $DATABASESERVER_PORT"
echo "********************************************************"
while ! `nc -z database $DATABASESERVER_PORT`; do sleep 3; done
echo ">>>>>>>>>>>> Database Server has started"

echo "********************************************************"
echo "Waiting for the kafka server to start on port  $KAFKASERVER_PORT"
echo "********************************************************"
while ! `nc -z kafkaserver $KAFKASERVER_PORT`; do sleep 10; done
echo "******* Kafka Server has started"

echo "********************************************************"
echo "Waiting for the redis server to start on port  $REDIS_PORT"
echo "********************************************************"
while ! `nc -z redis $REDIS_PORT`; do sleep 10; done
echo "******* Redis Server has started"

echo "********************************************************"
echo "Starting License Server with Configuration Service :  $CONFIGSERVER_URI";
echo "Using Kafka Server: $KAFKASERVER_URI"
echo "Using ZK    Server: $ZKSERVER_URI"
echo "********************************************************"
java -Xmx256m                                                               \
     -Djava.security.egd=file:/dev/./urandom -Dserver.port=$SERVER_PORT     \
     -Deureka.client.serviceUrl.defaultZone=$EUREKASERVER_URI               \
     -Dspring.cloud.config.uri=$CONFIGSERVER_URI                            \
     -Dspring.profiles.active=$PROFILE                                      \
     -Dsecurity.oauth2.resource.userInfoUri=$AUTHSERVER_URI                 \
     -Dspring.cloud.stream.kafka.binder.zkNodes=$KAFKASERVER_URI          \
     -Dspring.cloud.stream.kafka.binder.brokers=$ZKSERVER_URI             \
     -jar /usr/local/license-service/@project.build.finalName@.jar
