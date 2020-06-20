FROM openjdk:14 AS builder
RUN java --version
RUN mkdir /application
WORKDIR /application
COPY ./ ./
RUN ./gradlew clean build
RUN ls -al build/libs/