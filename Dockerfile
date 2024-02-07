FROM openjdk:17
#VOLUME /tmp
EXPOSE 6410
#ARG JAR_FILE=target/contact-manager-docker-jenkins.jar
##ADD ${JAR_FILE} contact-manager-docker-jenkins.jar
ADD target/contact-manager-docker-jenkins.jar contact-manager-docker-jenkins.jar
ENTRYPOINT ["java","-jar","/contact-manager-docker-jenkins.jar"]