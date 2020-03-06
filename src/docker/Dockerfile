FROM openjdk:8
ADD cms.jar /app-server.jar
ENV JAVA_OPTS="-Xmx4g -Duser.timezone=GMT -Dfile.encoding=UTF-8 -Denvironment.type=production"
CMD exec java $JAVA_OPTS -jar /app-server.jar