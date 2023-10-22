FROM ubuntu:20.04
VOLUME /tmp
EXPOSE 8080

ARG JAR_FILE=/target/reviewsbackend-0.0.1-SNAPSHOT.jar
ARG KEY=reviews-backend.pem

RUN apt-get update && \
    apt-get install -y openssh-client && \
    apt-get install -y openjdk-17-jdk

ADD ${JAR_FILE} app.jar
ADD ${KEY} key.pem

RUN echo "#!/bin/sh" > /entrypoint.sh && \
    echo "chmod 400 /key.pem" > /entrypoint.sh && \
    echo "ssh -oStrictHostKeyChecking=no -i key.pem -fN ec2-user@ec2-16-170-214-72.eu-north-1.compute.amazonaws.com -L 3000:reviews-database.cekkpqboucwz.eu-north-1.rds.amazonaws.com:3306 &" >> /entrypoint.sh && \
    echo "java -jar /app.jar" >> /entrypoint.sh


RUN chmod +x /entrypoint.sh

# Set the entry point to the shell script
ENTRYPOINT ["/entrypoint.sh"]