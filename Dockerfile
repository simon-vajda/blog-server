FROM openjdk:11
ARG JAR_FILE=target/*.jar
ENV spring_profiles_active=prod
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080