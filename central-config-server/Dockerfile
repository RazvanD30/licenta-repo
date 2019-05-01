FROM java:8
EXPOSE 8801:8801
RUN mkdir -p /app/
RUN echo "deb [check-valid-until=no] http://archive.debian.org/debian jessie-backports main" > /etc/apt/sources.list.d/jessie-backports.list
RUN sed -i '/deb http:\/\/deb.debian.org\/debian jessie-updates main/d' /etc/apt/sources.list
RUN apt-get -o Acquire::Check-Valid-Until=false update
RUN apt-get install -y curl
ADD build/libs/central-config-server.jar /app/central-config-server.jar
ENTRYPOINT ["java", "-jar", "/app/central-config-server.jar"]