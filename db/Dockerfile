FROM mysql:8.0.37

ENV MYSQL_DATABASE=ita_dbprog
ENV MYSQL_ALLOW_EMPTY_PASSWORD=true

COPY ./conf/* /etc/mysql/conf.d
COPY ./docker-entrypoint-initdb/* /docker-entrypoint-initdb.d/  