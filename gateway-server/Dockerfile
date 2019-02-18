FROM java:8
EXPOSE 8803:8803
RUN mkdir -p /app/
ADD build/libs/gateway-server.jar /app/gateway-server.jar
RUN apt-get update \
    && DEBIAN_FRONTEND=noninteractive apt-get install -y net-tools
RUN apt-get update \
  && DEBIAN_FRONTEND=noninteractive apt-get install -y netcat