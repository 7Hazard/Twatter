FROM openjdk:16
ADD build/libs/Twatter-0.0.1-SNAPSHOT.jar Twatter-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT java -jar -Dspring.profiles.active=dev Twatter-0.0.1-SNAPSHOT.jar
