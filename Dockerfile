FROM openjdk:17-oracle
EXPOSE 8083
ADD target/k8s-springbootapp-monitoring-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]