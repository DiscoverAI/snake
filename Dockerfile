FROM openjdk:11
COPY target/snake-deploy-standalone.jar /usr/src/snake/app.jar
WORKDIR /usr/src/snake
CMD ["java", "-jar", "/usr/src/snake/app.jar"]
