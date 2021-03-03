FROM openjdk:8-jre-alpine
ENV JAVA_OPTS="-XX:-UseGCOverheadLimit -Xmx16384m"
WORKDIR /gi-rakendus/backend
COPY target/backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["/usr/bin/java", "-jar", "app.jar"]