#!/bin/sh

echo --------------------------------------------------------------------
echo Starting eureka using the default profile
echo --------------------------------------------------------------------
mvn clean package && java -jar target/eureka.jar