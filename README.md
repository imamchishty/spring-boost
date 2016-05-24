# Spring Boost

[![Build Status](https://travis-ci.org/imamchishty/spring-boost.svg?branch=master "spring-boost")](https://travis-ci.org/imamchishty/spring-boost) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.shedhack.tool/spring-boost/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.shedhack.tool/spring-boost)

## What the hell is it? 
Spring Boost is a Maven Archetype that helps to create a multi-module maven project.
 
## Why did you need it?  
Having worked on quite a few Spring Boot projects it became quite tedious to recreate the module structure that suited my needs, hence this 'booster'.
Spring Initializr is great and probably is good enough for most projects. 
I however wanted separated modules, default controllers (ping, help), settings (application.yml), logging etc.

## Maven Modules

The following modules are generated:

- **Exception** - contains default a default runtime exception, business codes (similar concept to HTTP codes) and a client exception model.

- **Model** - a module which should house DTOs, JavaBeans etc. 

- **Service** - default is empty, but this is where my services would be housed. 

- **Rest** - Spring controllers are here. 

- **Domain** - domain layer, for example repos/entities sit here (as should your core logic). 

## Edge Services

Bundled in the cloud-servers folder you'll see the following servers (each with a run.sh script):

* cloud-config - spring cloud config, runs on port 8070, please see the application.yml for some useful tips.

* cloud-eureka - netflix eureka (service registration & discovery), run on port 8071, the application.yml in rest module points to this.

* cloud-turbine - netflix central dashboard, runs on port 8073.

* cloud-zuul - API gateway, see the bootstrap.yml file (in the cloud-zuul folder) for examples. By default it blocks /admin and permits /api/**.

At the root of the cloud-servers folder you'll see a pom.xml, this allows you to control the version of spring-cloud-parent for the servers, currently set to BRIXTON. It is useful to have these servers running locally when developing. In order to use these with the rest module you'll need to make a few changes, described in the rest module properties section later.

## Exception Module

This module is used to provide a central and consistent way of handling exceptions. It heavily relies on [__exception-core__](https://github.com/imamchishty/exception-core) which provides a simple way to build exceptions. So in reality the exception-module is providing a reusable (centralised) list of business codes (for exceptional circumstances). It should be where all exception (common to the application) should be housed, an example is:

- **GlobalBusinessCodes** - a list of business codes that have been defined. This is simply an implementation of BusinessCode.

## Model Module

Currently defaults to empty, but the aim of this module is to house all touch point models. For example the service returns something back to the rest tier. That something is probably a DTO and that DTO lives in the model module. One benefit is that we can easily package this module as a jar and provide to clients.

## Service and Domain Modules

Also currently empty. But its obvious what they'd be used for :)

## Rest Module

All HTTP requests are handled by this tier. This module is transformed into an executable JAR via spring boot. By default the following controllers are provided:

1. PingController - a simple end point, via /api/ping, which returns HTTP 200 OK when running. This can be useful to see if the application is running. This controller also contains a Netflix Feign example available via /api/accounts - requires Eureka.
2. HelpController - /api/help provides a client with a list of all BusinessCodes and HTTP codes.

### Rest Module properties

## How do I create a new project using 'boost'?

Spring-boost is available in maven central and bintray so you don't need to build the project locally.
To run the archetype:


mvn archetype:generate -DgroupId=YOUR_GROUP_ID -DartifactId=YOUR_ARTIFACT_ID -Dversion=YOUR_VERSION -Dpackage=YOUR_ROOT_JAVA_PACKAGE -DarchetypeGroupId=com.shedhack.tool -DarchetypeArtifactId=spring-boost -DarchetypeVersion=1.0.2 -DinteractiveMode=false -q

Example:

     mvn archetype:generate -DgroupId=com.kungfu -DartifactId=panda -Dversion=1.0.0-SNAPSHOT -Dpackage=com.kungfu.panda -DarchetypeGroupId=com.shedhack.tool -DarchetypeArtifactId=spring-boost -DarchetypeVersion=1.4.0 -DinteractiveMode=false -q

This will create a project folder locally. Open that folder and the try to run the shell script, run.sh (you might need to chmod it, e.g. chmod u+x run.sh). The application should now run.

## Docker?

A Dockerfile is generated using a Spotify plugin. This has been used in the rest module. To make use of it you need to start docker and then

1. mvn package docker:build
2. docker run -p 8080:8080 -t ${docker.image.prefix}/${project.artifactId}
3. docker ps
