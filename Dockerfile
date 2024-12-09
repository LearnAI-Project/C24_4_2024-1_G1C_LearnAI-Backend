FROM ubuntu:24.04

RUN apt update -y && apt upgrade -y &&  apt install -y curl git openjdk-17-jdk maven python3 python3-pip nodejs npm

RUN curl -sSL https://install.python-poetry.org | python3 -

WORKDIR /app

COPY . /app

WORKDIR /app/spring

RUN cp /etc/secrets/.env .env

RUN mvn clean install

CMD ["mvn", "spring-boot:run"]