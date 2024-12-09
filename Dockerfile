FROM ubuntu:24.04

RUN apt update -y && apt upgrade -y &&  apt install -y curl git openjdk-17-jdk maven python3 python3-pip nodejs npm

RUN curl -sSL https://install.python-poetry.org | python3 -

WORKDIR /app

COPY . /app

RUN cp /.env.spring /app/spring/.env || cp /etc/secrets/.env.spring /app/spring/.env || echo "No .env file found"

WORKDIR /app/spring

RUN mvn clean install

CMD ["mvn", "spring-boot:run"]