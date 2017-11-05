FROM timescale/timescaledb-postgis:0.6.0

MAINTAINER igrow-systems https://www.igrow-systems.com

ADD ./1-init-user-db.sh /docker-entrypoint-initdb.d
ADD ./2-init-observations-table.sql /docker-entrypoint-initdb.d
ADD ./3-init-devices-table.sql /docker-entrypoint-initdb.d

ENV POSTGRES_USER igrow-service-dev
ENV POSTGRES_PASSWORD igrow-service-dev
ENV POSTGRES_DB igrow-service-dev
