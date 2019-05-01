FROM java:8
EXPOSE 8803:8803
RUN mkdir -p /app/
ADD build/libs/gateway-server.jar /app/gateway-server.jar
RUN echo "deb [check-valid-until=no] http://archive.debian.org/debian jessie-backports main" > /etc/apt/sources.list.d/jessie-backports.list
RUN sed -i '/deb http:\/\/deb.debian.org\/debian jessie-updates main/d' /etc/apt/sources.list
RUN apt-get -o Acquire::Check-Valid-Until=false update \
    && DEBIAN_FRONTEND=noninteractive apt-get install -y net-tools
RUN apt-get -o Acquire::Check-Valid-Until=false update \
  && DEBIAN_FRONTEND=noninteractive apt-get install -y netcat