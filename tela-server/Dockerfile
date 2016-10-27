FROM java
COPY target/jar/tela.jar /usr/tela/tela.jar
WORKDIR /usr/tela

# Application port
ENV PORT 80
EXPOSE 80

# Configure redis
ENV CACHE_MODE redis
ENV REDIS_HOST redis
ENV REDIS_PORT 6379

# Configure OrientDB
ENV ORIENTDB_MODE remote
ENV ORIENTDB_HOST orientdb
ENV ORIENTDB_PORT 2480
ENV ORIENTDB_USER root
ENV ORIENTDB_PASSWORD tela
EXPOSE 2480

CMD java -jar tela.jar