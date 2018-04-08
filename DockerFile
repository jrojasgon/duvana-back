FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
MAINTAINER johannarojasgon@gmail.com
ADD assets/duvana-back.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]