package server.webSocket;

import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String playerName, Session session) {
        Connection connection = new Connection(playerName, session);
        connections.put(playerName, connection);
    }

    public void remove(String playerName) {
        connections.remove(playerName);
    }

    public void broadcast(String rootPlayer, ServerMessage message) throws IOException {
        ArrayList<Connection> removeList = new ArrayList<>();
        for (Connection connection: connections.values()) {
            if (connection.session.isOpen()) {
                if (!connection.playerName.equals(rootPlayer)) {
                    connection.send(message.toString());
                }
            } else {
                removeList.add(connection);
            }
        }

        for (Connection connection: removeList) {
            connections.remove(connection.playerName);
        }
    }
}
