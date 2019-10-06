FROM adoptopenjdk/openjdk11:latest

RUN mkdir /opt/app
ARG JAR_FILE
ADD target/${JAR_FILE} /opt/app/entrypoint.jar
CMD ["java", "-jar", "/opt/app/entrypoint.jar"]
