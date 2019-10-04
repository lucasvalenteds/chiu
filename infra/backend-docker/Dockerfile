FROM adoptopenjdk:8-jre-hotspot
RUN mkdir /opt/chiu
COPY build/libs/chiu-backend-all.jar /opt/chiu
EXPOSE 8080
CMD ["java", "-jar", "/opt/chiu/chiu-backend-all.jar"]
