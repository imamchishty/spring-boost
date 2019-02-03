# Spring Boost

[![Build Status](https://travis-ci.org/imamchishty/spring-boost.svg?branch=master "spring-boost")](https://travis-ci.org/imamchishty/spring-boost) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.shedhack.tool/spring-boost/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.shedhack.tool/spring-boost)

## What the hell is it? 
Spring Boost is a Maven Archetype that creates a spring boot maven project perfect for building microservices. If you need to get up to speed quickly then this archetype will provide you with lots of default settings allowing you to focus on your business logic. If you use this for building all of your microservices then you'll end up with a consistent way of building them, moving from ms1 to ms2 should be very easy.  

Uses Spring Boot __2.1.1.RELEASE__

__Key features:__

- __Spring Boot template project for microservice__

- __Configured with appropriate plugins & dependencies__

- __Utilises custom components__

- __Examples (Jmeter etc)__

- __Consistent__

## How do I create a new project using 'boost'?

Spring-boost is available in maven central and bintray so you don't need to build the project locally. To run the archetype:

`mvn archetype:generate -DgroupId=YOUR_GROUP_ID -DartifactId=YOUR_ARTIFACT_ID -Dversion=YOUR_VERSION -Dpackage=YOUR_ROOT_JAVA_PACKAGE -DarchetypeGroupId=com.shedhack.tool -DarchetypeArtifactId=spring-boost -DarchetypeVersion=3.0.0 -DinteractiveMode=false -q`

Example:

`mvn archetype:generate -DgroupId=com.kungfu -DartifactId=panda -Dversion=1.0.0-SNAPSHOT -Dpackage=com.kungfu.panda -DarchetypeGroupId=com.shedhack.tool -DarchetypeArtifactId=spring-boost -DarchetypeVersion=3.0.0 -DinteractiveMode=false -q`

This will create a project folder locally. Open that folder and the try to run the shell script, run.sh (you might need to chmod it, e.g. chmod u+x run.sh). The application should now run.

- **GlobalBusinessCodes** - a list of business codes that have been defined. This is simply an implementation of BusinessCode.

Please note that the rest module also contains an exception controller.

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

#### Exception chain

![alt tag](https://github.com/imamchishty/spring-boost/blob/master/resources/exception-flow.jpg?raw=true "Exception Chain")


#### Client Model for exceptions 

__Attributes:__

![alt tag](https://github.com/imamchishty/spring-boost/blob/master/resources/client-model.jpg?raw=true "Client Model for exceptions")

#### /admin/exceptions

![alt tag](https://github.com/imamchishty/spring-boost/blob/master/resources/client-model2.jpg?raw=true "/admin/exceptions")

### Rest Module properties

- **bootstrap.yml**

- **application.yml** 

- **git-build.properties**

- **log4j2.xml**

### Application.java - configuration

All configuration for the rest-module is done from within __Application__. The default config includes

- **Swagger** - swagger is configured using both  __@EnableSwagger2__ and some bean definitions with the __Application__ class.

- [**Request Trace**](https://github.com/imamchishty/trace-request-filter) - Enabled using the __@EnableTraceRequestJpa__ annotation. The filter is also configured as a bean. You can add other interceptors if you choose.

- [**Thread Context**](https://github.com/imamchishty/thread-context-aspect) -  @EnableThreadContextAspect, see project for details.

- [**Global Exception Handling**](https://github.com/imamchishty/exception-controller-spring) - @EnableExceptionController, see project for details.

- [**Custom Actuators**](https://github.com/imamchishty/spring-actuator) - @EnableActuatorsAndInterceptors, see project for details.

- [**Netflix**] - @EnableDiscoveryClient, @EnableFeignClients, @EnableHystrix, @EnableHystrixDashboard, refer to spring boot docs.

### Swagger

Swagger available at /api/docs.

### Thread Context

[__@ThreadContext__](https://github.com/imamchishty/thread-context-aspect) sets the thread name with mutliple params, please refer to the project [here]((https://github.com/imamchishty/thread-context-aspect)). 

__Key features:__

- Great for seeing the thread meta-data.

- Very useful for debugging, e.g. blocked thread.

- Could be used for auditing.

- Used for debugging applications.

- Sets the context (time, method, params, requestId etc) to the executing thread name.

- @ThreadContext, simple annotation at the method level.

- @Ignore – used to protect specific params

- Threads can be monitored with tools such as JMC.

### Custom actuators

[**Custom Actuators**](https://github.com/imamchishty/spring-actuator) available at `/admin/health`, `/admin/requests`, `/admin/exceptions`

### Netflix

A Feign example has been added to the PingController, /api/accounts, please take a look at the `.../feign` package in the rest module.

![alt tag](https://github.com/imamchishty/spring-boost/blob/master/resources/feign.jpg?raw=true "Feign example, /api/accounts")

### Tracing requests

Please note that the original mechanism of tracing requests has been replaced by Spring Sleuth. All endpoints (/admin/requests, /admin/exceptions) are still valid as well as zipkin support.

#### /admin/requests

You can view requests at this end-point. The following properties currently set in the application.yml file can be modified accordingly:

    trace.interceptor.queue.size: 50
    trace.interceptor.endpoint: requests


#### Visualise requests (requires another project)

![alt tag](https://github.com/imamchishty/spring-boost/blob/master/resources/trace-viewer.jpg?raw=true "Trace Requests Viewer")

### API endpoints

API end points are managed in a simple Java Constants file, __ApiConstants__.

#### /api

By default anything under /api is where your clients interact with your services. /api/** should be permitted via the API Gateway (see the boostrap.yml in cloud-zuul for details).

| URI                        | Description                                                            |
| -------------------------- |----------------------------------------------------------------------|
| /api/ping                  |  200 OK, end point to check service is running.                        |
| /api/help                  | returns a list of all business codes as well as all HTTP ones.         |
| /api/docs                  | Swagger API is available from this URI.                                |
| /api/accounts              | TEST endpoint to show how to work with Feign, Hystrix and Eureka.      |
| /api/accounts/{id}/balance | TEST endpoint to show how to work with Feign, Hystrix and Eureka.      |
| /api/accounts/problems     | TEST endpoint to show how to exceptions are handled.                   |

#### Example images from /api/...

![alt tag](https://github.com/imamchishty/spring-boost/blob/master/resources/api-help.jpg?raw=true "/api/help")

![alt tag](https://github.com/imamchishty/spring-boost/blob/master/resources/api-docs.jpg?raw=true "/api/docs")

#### /admin

All actuators are under /admin. These endpoints shouldn't be available to clients and it would be a good idea to block them from the API gateway (already done in cloud-zuul via bootstrap.yml).

| URI                        | Description                                                            |
| -------------------------- |----------------------------------------------------------------------|
| /admin/help |  Lists all admin endpoints. |
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

#### Example images from /admin/...

![alt tag](https://github.com/imamchishty/spring-boost/blob/master/resources/admin-requests.jpg?raw=true "/admin/requests")

![alt tag](https://github.com/imamchishty/spring-boost/blob/master/resources/admin-exceptions.jpg?raw=true "/admin/exceptions")

![alt tag](https://github.com/imamchishty/spring-boost/blob/master/resources/admin-health.jpg?raw=true "/admin/health")


### Testing

Three types of tests are included (all within the rest module).

- **Unit Tests** - your typical mocked tests, executed via mvn test

- **Integration Tests** -  run with the spring context initialised, ignored with mvn test, executed via mvn failsafe:integration-test. This only includes files with a specific naming convention, please see the parent-pom for more details.

- **Stress Testing** - JMeter has been added with a single test for the /api/ping end point. This should provide an example from which you can add more rest tests. The jmeter test is found in the rest module/src/test/jmeter/Microservice test plan.jmx. If you have JMeter locally then you can open the file and run it. Alternatively you can run this via mvn jmeter:jmeter. Please note that it'll expect the app to be running on localhost:8080, but you can change the location for example, `mvn clean jmeter:jmeter -Dhost=localhost -Dport=8080`.

![alt tag](https://github.com/imamchishty/spring-boost/blob/master/resources/jmeter.jpg?raw=true "JMeter")


In the future cucumber will also be added.

### Logging

Logging is done via log4j2. A default config file is available in the rest module/src/main/resources folder. 
Please modify the file as required, especially the LOG_PATH property - currently set to the target/logs folder. This would be a good candidate for using a system property.
If you look at the log4j2.xml file you'll see that it creates four files:

- **xxx-error.log** - all exceptions thrown/handled by the application (via the exception controller mentioned earlier) are logged here. 

- **xxx.log** - all application logs (non error).

- **xxx-third-party.log** - third party logs, I like to keep 'my' logs separate to prevent pollution.

- **xxx-trace-requests.log** - all handled requests by the application, refer to trace requests from earlier for more details.

In the future a xxx-audit.log will be added which will be filled via the `@ThreadContext`.

### Running the application

`./run.sh development skip`

`mvn spring-boot:run`

### FIX_ME's

Search for FIXME in the parent pom.

## Delivery Pipeline

Please note that you'll need to make some changes to the parent pom, just search for FIXME. Some of things that need changing are the CI link.

You can use the goals and plugins defined to create a pipeline. For example:

- mvn clean compile
- mvn test
- mvn failsafe:integration-test
- mvn jmeter:jmeter
- mvn findbugs:findbugs
- mvn pmd:pmd
- mvn site

## Maven site

By default each module has a maven site. Just look at the src/site folder. In the parents src folder you'll also find a changes folder, this contains a simple changes file which can be used as a releases journal. This gets added to the maven site when created. You can also deploy the maven site to your github pages although the default behaviour is to use a repo such as Nexus. Details and help for this is in the parent pom.

## Cloud Services

Bundled in the cloud-servers folder you'll see the following servers (each with a run.sh script):

- **cloud-config** - spring cloud config, runs on port 8070, please see the application.yml for some useful tips.

- **cloud-eureka** - netflix eureka (service registration & discovery), run on port 8071, the application.yml in rest module points to this.

- **cloud-turbine** - netflix central dashboard, runs on port 8073.

- **cloud-zuul** - API gateway running on port 8072, see the bootstrap.yml file (in the cloud-zuul folder) for examples. By default it blocks /admin and permits /api/**.

- **cloud-zipkin** - Zipkin to trace requests via slueth, already setup.

- **cloud-graylog2** - Logging, this requires you to use the graylog log4j2 file under resources. Rename this to log4j2 and remove the other one. You will need to setup the server/port details. 

The preferred way to start them all is using docker-compose, the file is located in the cloud servers directory.
For further details: https://github.com/imamchishty/cloud-servers

## Docker?

A Dockerfile is generated dynamically (see the rest pom file). This can be used for integration testing (although you'd need to add the execution goals accordingly).

1. mvn package docker:build
2. docker:run
3. docker:stop

You can add multiple images, e.g. for a backing service if required.

Using docker with jmeter (host setting in this example is using docker machine):

`mvn package -DskipTests docker:build docker:run -Ddocker.follow=false jmeter:jmeter -Dhost=192.168.99.100 -Dport=8080 docker:stop`


