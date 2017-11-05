
FROM rabbitmq:3.6

MAINTAINER igrow-systems https://www.igrow-systems.com

COPY docker-entrypoint-hooks.sh /usr/local/bin
COPY on-startup.sh /hooks/on-startup.sh

ENV RABBITMQ_USER igrow-service-dev
ENV RABBITMQ_PASSWORD igrow-service-dev

ENTRYPOINT ["docker-entrypoint-hooks.sh"]

EXPOSE 4369 5671 5672 25672
