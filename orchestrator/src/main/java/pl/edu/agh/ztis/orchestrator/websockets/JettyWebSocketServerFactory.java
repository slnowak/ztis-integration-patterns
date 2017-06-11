package pl.edu.agh.ztis.orchestrator.websockets;

import spark.Spark;

public class JettyWebSocketServerFactory {

    public static WebSocketsServer create(WebSocketProperties.WebSocketPropertiesBuilder props) {
        final WebSocketsServer server = JettyWebsocketServer.create();
        final WebSocketProperties serverProperties = props.build();
        Spark.port(serverProperties.port);
        Spark.webSocket(serverProperties.path, server);
        Spark.init();

        return server;
    }
}
