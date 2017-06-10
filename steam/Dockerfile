FROM maven:3.5.0-jdk-8-alpine
WORKDIR /opt/steam-connector
ADD . ./build/
RUN cd build && mvn package
RUN mv ./build/target/ztis-steam.jar . && rm -rf build
CMD ["/usr/bin/java", "-jar", "/opt/steam-connector/ztis-steam.jar"]