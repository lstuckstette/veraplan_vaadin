FROM tomee:latest

MAINTAINER me

# Add Warfile on creation:
# COPY <locationofapplication>/<warfile> /usr/local/tomee/webapps/<warfile>

# Add tomcat-users.xml to assign user for manager-gui
COPY tomcat-users.xml /usr/local/tomee//conf/tomcat-users.xml
# Add context.xml to disable local-access-only for manager-gui
COPY context.xml /usr/local/tomee//webapps/manager/META-INF/context.xml

CMD ["catalina.sh", "run"]

EXPOSE 8080
EXPOSE 8888