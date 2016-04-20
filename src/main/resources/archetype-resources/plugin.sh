#!/bin/sh

clear
echo --------------------------------------------------------------------
echo Starting ${artifactId}-rest using $SPRING_PROFILE profile
echo --------------------------------------------------------------------
cd ${artifactId}-rest && mvn spring-boot:run