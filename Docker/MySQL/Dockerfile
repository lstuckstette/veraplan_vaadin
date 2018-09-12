FROM mysql:latest

MAINTAINER me

ENV MYSQL_DATABASE=vp_mysql \
    MYSQL_ROOT_PASSWORD=volavis

ADD initscript.sql /docker-entrypoint-initdb.d

EXPOSE 3306