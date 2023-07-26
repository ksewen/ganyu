FROM maven:3.9.3-sapmachine-17 as builder

COPY ganyu /ganyu

WORKDIR /ganyu

ARG profile=test
ENV PROFILE=$profile

RUN mkdir out && \
    /ganyu/resources/scripts/build-app.sh

FROM openjdk:17-slim-buster

RUN mkdir /ganyu

COPY --from=builder /ganyu/target/ganyu-0.0.1-SNAPSHOT.jar /ganyu
COPY --from=builder /ganyu/resources/scripts/bootstrap.sh  /

VOLUME /tmp

WORKDIR /ganyu

EXPOSE 8087

CMD [ "/bootstrap.sh" ]