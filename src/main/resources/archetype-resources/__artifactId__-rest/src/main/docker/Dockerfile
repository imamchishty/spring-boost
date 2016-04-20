FROM java:8
VOLUME /tmp
ADD ${artifactId}-rest ${artifactId}-rest.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/${artifactId}-rest.jar"]
