FROM openjdk:11
VOLUME /tmp
EXPOSE 8087
ADD ./target/MovePassive-0.0.1-SNAPSHOT.jar ms-movepassive.jar
ENTRYPOINT ["java", "-jar","/ms-movepassive.jar"]

