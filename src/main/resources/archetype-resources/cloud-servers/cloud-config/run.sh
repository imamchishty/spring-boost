#!/bin/sh

echo --------------------------------------------------------------------
echo Starting config using the default profile
echo --------------------------------------------------------------------
mvn clean package && java -jar target/config.jar