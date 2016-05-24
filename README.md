# Spring Boost

## What the hell is it? 
Spring Boost is a Maven Archetype that helps to create a multi-module maven project.
 
## Why did you need it?  
Having worked on quite a few Spring Boot projects it became quite tedious to recreate the module structure that suited my needs, hence this 'booster'.
Spring Initializr is great and probably is good enough for most projects. 
I however wanted separated modules, default controllers (ping, help), settings (application.yml), logging etc.

## Maven Modules

The following modules are generated:

* Exception - contains default a default runtime exception, business codes (similar concept to HTTP codes) and a client exception model.

* Model - a module which should house DTOs, JavaBeans etc. 

* Service - default is empty, but this is where my services would be housed. 

* Rest - Spring controllers are here. 

## Exception Module

This module is used to provide a central and consistent way of handling exceptions. It contains the following:

1. BusinessCode - interface that expects implementations to provide a valid business code and description. 
By providing business codes to the client we are able to clearly relate problems. 
For example if a user has attempted to login but the password has expired then we could lets say return a code such as C100 - password expired. The codes should not be limited to validation, but rather it should cater for all circumstances, e.g. database connection has dropped. 

2. BusinessException - this is just a runtime exception that provides some extra properties, such as a exception Id, correlation Id, list of params, http error codes etc. The exception Id is important for investigations and this is returned back to clients. The correlation Id is used whenever an external service call fails. That external service might return back an exception Id which we map to a correlation Id. This gives us complete end-to-end traceability.

3. ClientExceptionModel - in order to be consistent with clients, whenever an exception occurs a exception handler converts that exception to this model. One important feature of this model is that it contains unqiue Id. This id can be easily linked to log files for further investigation.

4. GlobalBusinessCodes - a list of business codes that have been defined. This is simply an implementation of BusinessCode.


## Model Module

Currently defaults to empty, but the aim of this module is to house all touch point models. For example the service returns something back to the rest tier. That something is probably a DTO and that DTO lives in the model module. One benefit is that we can easily package this module as a jar and provide to clients.

## Service Module

Also currently empty. But its obvious what it would be used for :)

## Rest Module

All HTTP requests are handled by this tier. This module is transformed into an executable JAR via spring boot. By default the following controllers are provided:

1. PingController - a simple end point, via /ping, which returns HTTP 200 OK when running. This can be useful to see if the application is running.
2. HelpController - /help provides a client with a list of all BusinessCodes and HTTP codes.
3. ExceptionController - ControllerAdvice that handles all exceptions and maps them to the the ClientExceptionModel.

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
