package pl.edu.agh.ztis.orchestrator.websockets;

import lombok.Builder;

@Builder
public class WebSocketProperties {

    @Builder.Default
    int port = 8080;
    @Builder.Default
    String path = "/recommendations";
}
