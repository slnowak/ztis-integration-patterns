package pl.edu.agh.ztis.orchestrator.websockets;


import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.util.ConcurrentHashSet;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.eclipse.jetty.websocket.api.WebSocketListener;

import java.util.Set;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class JettyWebsocketServer implements WebSocketsServer, WebSocketListener {

    private final Set<Session> sessions = new ConcurrentHashSet<>();
    private final Gson encoder;

    static JettyWebsocketServer create() {
        return new JettyWebsocketServer(new Gson());
    }

    @Override
    public <T> void publish(T message) {
        final String serializedMessage = encoder.toJson(message);
        sessions.forEach(session -> sendMessage(session, serializedMessage));
    }

    private void sendMessage(Session session, String serializedMessage) {
        try {
            session.getRemote().sendStringByFuture(serializedMessage);
        } catch (WebSocketException ex) {
            ex.printStackTrace();
            // ignoring exceptions - best practices, highly recommended
        }
    }

    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len) {
    }

    @Override
    public void onWebSocketText(String message) {
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        performCleanup();
    }

    @Override
    public void onWebSocketConnect(Session session) {
        sessions.add(session);
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        performCleanup();
    }

    private boolean performCleanup() {
        return sessions.removeIf(session -> !session.isOpen());
    }
}
