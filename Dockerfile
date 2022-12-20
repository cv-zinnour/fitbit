FROM gradle:5.6.2-jdk11 as java-build
EXPOSE 8765
WORKDIR /opt/pod-isante/fitbit
COPY build/libs/fitbit-0.0.1-SNAPSHOT.jar fitbit-server.jar
CMD ["java","-jar","/opt/pod-isante/fitbit/fitbit-server.jar"]


#docker pull lahcenezinnour/fitbit-docker-img:latest
#docker run -p 8765:8765 -t lahcenezinnour/fitbit-docker-img:latest
#docker build -t fitbit-docker-img .
#docker tag fitbit-docker-img lahcenezinnour/fitbit-docker-img:latest
#docker push lahcenezinnour/fitbit-docker-img:latest