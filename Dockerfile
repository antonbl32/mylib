########Maven build stage########
FROM openjdk:8 as maven_build
# author
ADD target/mylib.jar mylib.jar
EXPOSE 8080
MAINTAINER Shylau Anton
ENTRYPOINT ["java","-jar","mylib.jar"]
