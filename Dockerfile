FROM openjdk:11
EXPOSE 5500
ADD build/libs/JavaDiplom-0.0.1-SNAPSHOT-plain.jar app.jar
CMD ["java","-jar","app.jar"]