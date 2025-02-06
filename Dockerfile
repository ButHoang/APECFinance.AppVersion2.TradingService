FROM maven:3.8.1-jdk-11 as builder
ENV HOME=/home/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
COPY pom.xml $HOME/
COPY src $HOME/src
RUN mvn clean package -DskipTests

FROM openjdk:11-jre-slim
ENV HOME=/home/usr/app
WORKDIR $HOME
COPY --from=builder $HOME/target/*.jar app.jar
COPY --from=builder $HOME/src/main/resources/application.yml application.yml
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
