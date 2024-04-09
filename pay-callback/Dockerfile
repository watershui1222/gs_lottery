FROM openjdk
EXPOSE 9550
VOLUME /tmp
ADD pay-callback/target/pay-callback.jar /app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Duser.timezone=GMT+8","-jar","/app.jar"]
