FROM maven:3.5.0-jdk-8-alpine
WORKDIR /opt/orchestrator
ADD . ./build/
RUN cd build && mvn package
RUN mv ./build/target/ztis-orchestrator.jar . && rm -rf build
CMD ["/usr/bin/java", "-jar", "/opt/orchestrator/ztis-orchestrator.jar"]