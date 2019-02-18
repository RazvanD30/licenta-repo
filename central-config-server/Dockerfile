FROM java:8
EXPOSE 8801:8801
RUN mkdir -p /app/
RUN apt-get update
RUN apt-get install -y curl
ADD build/libs/central-config-server.jar /app/central-config-server.jar
ENTRYPOINT ["java", "-jar", "/app/central-config-server.jar"]