FROM openjdk:11-jdk

VOLUME /tmp
ADD target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]