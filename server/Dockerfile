FROM amazoncorretto:11-alpine-jdk
RUN apk add --no-cache tzdata
ENV TZ=Europe/Moscow
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:9091
COPY target/*.jar server.jar
ENTRYPOINT ["java","-jar","/server.jar"]