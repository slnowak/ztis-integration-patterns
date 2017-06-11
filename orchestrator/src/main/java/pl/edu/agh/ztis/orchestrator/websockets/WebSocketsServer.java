package pl.edu.agh.ztis.orchestrator.websockets;

public interface WebSocketsServer {

    <T> void publish(T message);
}
