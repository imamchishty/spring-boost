# Spring Boost

[![Build Status](https://travis-ci.org/imamchishty/spring-boost.svg?branch=master "spring-boost")](https://travis-ci.org/imamchishty/spring-boost) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.shedhack.tool/spring-boost/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.shedhack.tool/spring-boost)

## What the hell is it? 
Spring Boost is a Maven Archetype that creates a spring boot multi-module maven project perfect for building microservices. If you need to get up to speed quickly then this archetype will provide you with lots of default settings allowing you to focus on your business logic. If you use this for building all of your microservices then you'll end up with a consistent way of building them, moving from ms1 to ms2 should be very easy. The following sections will describe the features provided.
 
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

## Exception Module

This module is used to provide a central and consistent way of handling exceptions. It heavily relies on [__exception-core__](https://github.com/imamchishty/exception-core) which provides a simple way to build exceptions. So in reality the exception-module is providing a reusable (centralised) list of business codes (for exceptional circumstances). It should be where all exception (common to the application) should be housed, an example is:

- **GlobalBusinessCodes** - a list of business codes that have been defined. This is simply an implementation of BusinessCode.

Please note that the rest module also contains an exception controller.

## Model Module

Currently defaults to empty, but the aim of this module is to house all touch point models. For example the service returns something back to the rest tier. That something is probably a DTO and that DTO lives in the model module. One benefit is that we can easily package this module as a jar and provide to clients.

## Service and Domain Modules

Also currently empty. But its obvious what they'd be used for :)

## Rest Module

All HTTP requests are handled by this tier. This module is transformed into an executable JAR via spring boot. By default the following controllers are provided:

- **PingController** - a simple end point, via /api/ping, which returns HTTP 200 OK when running. This can be useful to see if the application is running. This controller also contains a Netflix Feign example available via /api/accounts - requires cloud-eureka to be running.

- **HelpController** - /api/help provides a client with a list of all BusinessCodes and HTTP codes.

- **BuildController** - provides build/git information via the git-build.properties, CI (if defined) and also maven.  

### Exception Handling

When an exception is thrown the stack trace is useful for developers, however for clients it's not ideal and we need to 'convert' it so that it's much more manageable. It would also be great if we could provide a consistent 'error' model in exceptional circumstances. In order to achieve these and more spring-boost uses a exception controller component, [__exception-controller-spring__](https://github.com/imamchishty/exception-controller-spring). This component is activated via the __@EnableExceptionController__ in the Application class.

This controller will handle all exceptions, and wrap them into an [__exception model__](https://github.com/imamchishty/exception-core). The controller also provides you access to the exception model + exception via interceptors.

Two admin endpoints are available that provide an exceptions count (/admin/health) and also details of the last n number of exceptions (in detail) via /admin/exceptions. You can configure the size of the ring buffer (used to store the last n number of exceptions) by changing:

    exception.interceptor.queue.size: 50
    exception.interceptor.endpoint: exceptions
    exception.interceptor.stacktrace: false

In the above you can see `exception.interceptor.endpoint` set to `exceptions` this maps to /admin/exceptions. You can change it if required.

### Rest Module properties

- **bootstrap.yml** - 

- **application.yml** - 

- **git-build.properties** -

- **log4j2.xml** -

### Application.java - configuration

All configuration for the rest-module is done from within __Application__. The default config includes

- **Swagger** - swagger is configured using both  __@EnableSwagger2__ and some bean definitions with the __Application__ class.

- [**Request Trace**](https://github.com/imamchishty/trace-request-filter) - Enabled using the __@EnableTraceRequestJpa__ annotation. The filter is also configured as a bean. You can add other interceptors if you choose.

- [**Thread Context**](https://github.com/imamchishty/thread-context-aspect) -  @EnableThreadContextAspect

- [**Global Exception Handling**](https://github.com/imamchishty/exception-controller-spring) @EnableExceptionController

- [**Custom Actuators**](https://github.com/imamchishty/spring-actuator)  @EnableActuatorsAndInterceptors

- Netflix  @EnableDiscoveryClient @EnableFeignClients @EnableHystrix @EnableHystrixDashboard

### Swagger


### Thread Context

### Custom actuators

### Netflix

### Tracing requests

    trace.interceptor.queue.size: 50
    trace.interceptor.endpoint: requests

### API endpoints

API end points are managed in a simple Java Constants file, __ApiConstants__.

#### /api

By default anything under /api is where your clients interact with your services. /api/** should be permitted via the API Gateway (see the boostrap.yml in cloud-zuul for details).

| URI                        | Description                                                            |
| -------------------------- |:----------------------------------------------------------------------:|
| /api/ping                  |  200 OK, end point to check service is running.                        |
| /api/help                  | returns a list of all business codes as well as all HTTP ones.         |
| /api/docs                  | Swagger API is available from this URI.                                |
| /api/accounts              | TEST endpoint to show how to work with Feign, Hystrix and Eureka.      |
| /api/accounts/{id}/balance | TEST endpoint to show how to work with Feign, Hystrix and Eureka.      |
| /api/accounts/problems     | TEST endpoint to show how to exceptions are handled.                   |

#### /admin

All actuators are under /admin. These endpoints shouldn't be available to clients and it would be a good idea to block them from the API gateway (already done in cloud-zuul via bootstrap.yml).

| URI                        | Description                                                            |
| -------------------------- |:----------------------------------------------------------------------:|
| /admin/build |  Build, git, ci information. Mainly taken from git-build.properties |
| /admin/exceptions | Custom end point that shows the last n number of exceptions, please see the exception handling section for more details.|
| /admin/requests | The trace request filter + interceptors can provide lots of details of requests. For more details see 'tracing requests' | 
| /admin/health | Spring actuator with some extra exceptions details |
| /admin/autoconfig | Spring actuator |
| /admin/beans  | Spring actuator |
|/admin/configprops  | Spring actuator |
| /admin/dump | Spring actuator |
| /admin/env | Spring actuator |
| /admin/info | Spring actuator |
| /admin/mappings | Spring actuator |
| /admin/metrics | Spring actuator |
| /admin/trace | Spring actuator |
| /admin/docs | Spring actuator |


### Testing

### Logging

### Running the application

### FIX_ME's

## Delivery Pipeline

## Maven site

## Edge Services

Bundled in the cloud-servers folder you'll see the following servers (each with a run.sh script):

- **cloud-config** - spring cloud config, runs on port 8070, please see the application.yml for some useful tips.

- **cloud-eureka** - netflix eureka (service registration & discovery), run on port 8071, the application.yml in rest module points to this.

- **cloud-turbine** - netflix central dashboard, runs on port 8073.

- **cloud-zuul** - API gateway running on port 8072, see the bootstrap.yml file (in the cloud-zuul folder) for examples. By default it blocks /admin and permits /api/**.

At the root of the cloud-servers folder you'll see a pom.xml, this allows you to control the version of spring-cloud-parent for the servers, currently set to BRIXTON. It is useful to have these servers running locally when developing. In order to use these with the rest module you'll need to make a few changes, described in the rest module properties section later.

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
