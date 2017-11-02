FROM timescale/timescaledb-postgis:0.6.0

MAINTAINER igrow-systems https://www.igrow-systems.com

ADD . /docker-entrypoint-initdb.d

ENV POSTGRES_USER igrow-service-dev
ENV POSTGRES_PASSWORD igrow-service-dev
ENV POSTGRES_DB igrow-service-dev
