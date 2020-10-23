FROM adoptopenjdk/openjdk11
COPY . /opt/url
WORKDIR /opt/url
RUN ./sbt clean stage