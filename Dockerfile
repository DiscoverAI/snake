FROM openjdk:11
COPY target/snake-deploy-standalone.jar /usr/src/snake/app.jar
WORKDIR /usr/src/snake
HEALTHCHECK --interval=10s --timeout=10s --start-period=5s --retries=10 CMD ["curl", "localhost:8080/health"]
CMD ["java", "-jar", "/usr/src/snake/app.jar"]
