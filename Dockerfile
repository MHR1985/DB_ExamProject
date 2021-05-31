FROM adoptopenjdk/openjdk11:latest
COPY ./target/thesocialnetwork-0.0.1-SNAPSHOT.jar thesocialnetwork-0.0.1-SNAPSHOT.jar
CMD ["java","-jar","thesocialnetwork-0.0.1-SNAPSHOT.jar"]