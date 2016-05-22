#!/bin/sh

echo --------------------------------------------------------------------
echo Starting zuul using the default profile
echo --------------------------------------------------------------------
mvn clean package && java -jar target/zuul.jar