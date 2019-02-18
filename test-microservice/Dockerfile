FROM java:8
EXPOSE 8810:8810
RUN mkdir -p /app/
ADD build/libs/test-microservice.jar /app/test-microservice.jar
RUN apt-get update \
    && DEBIAN_FRONTEND=noninteractive apt-get install -y net-tools
RUN apt-get update \
  && DEBIAN_FRONTEND=noninteractive apt-get install -y netcat