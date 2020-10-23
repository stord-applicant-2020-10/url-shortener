FROM adoptopenjdk/openjdk11
COPY . /opt/url
WORKDIR /opt/url
RUN chmod a+x ./sbt
RUN ./sbt clean stage
