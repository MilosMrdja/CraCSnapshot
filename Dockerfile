FROM azul/zulu-openjdk:25-jdk-crac-latest
WORKDIR /opt/app
COPY target/spring-petclinic-4.0.0-SNAPSHOT.jar /opt/app/petclinic.jar
