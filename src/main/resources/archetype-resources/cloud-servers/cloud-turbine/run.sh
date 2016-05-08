#!/bin/sh

echo --------------------------------------------------------------------
echo Starting turbine using the default profile
echo --------------------------------------------------------------------
mvn clean package && java -jar target/turbine.jar