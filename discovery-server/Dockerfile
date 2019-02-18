FROM java:8
EXPOSE 8802:8802
RUN mkdir -p /app/
ADD build/libs/discovery-server.jar /app/discovery-server.jar
RUN apt-get update \
    && DEBIAN_FRONTEND=noninteractive apt-get install -y net-tools
RUN apt-get update \
  && DEBIAN_FRONTEND=noninteractive apt-get install -y netcat
