FROM openjdk:8-jdk-alpine
WORKDIR /opt/steam-connector
ADD . ./build/
RUN cd build && ./mvnw package
RUN mv ./build/target/*.jar ./ztis-steam.jar && rm -rf build
CMD ["/usr/bin/java", "-jar", "/opt/steam-connector/ztis-steam.jar"]