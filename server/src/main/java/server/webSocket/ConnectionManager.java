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

    public void broadcastToEveryone(ServerMessage message) throws IOException {
        broadcast(new ArrayList<>(connections.values()), message);
    }

    public void broadcastExcludingRoot(String rootPlayer, ServerMessage message) throws IOException {
        ArrayList<Connection> include = new ArrayList<>();
        for (Connection connection: connections.values()) {
            if (!connection.playerName.equals(rootPlayer)) {
                include.add(connection);
            }
        }

        broadcast(include, message);
    }

    public void broadcastToRoot(String rootPlayer, ServerMessage message) throws IOException {
        ArrayList<Connection> include = new ArrayList<>();
        include.add(connections.get(rootPlayer));

        broadcast(include, message);
    }

    private void broadcast(ArrayList<Connection> includedPlayers, ServerMessage message) throws IOException {
        ArrayList<Connection> removeList = new ArrayList<>();
        for (Connection connection: includedPlayers) {
            if (connection.session.isOpen()) {
                connection.send(message.toString());
            } else {
                removeList.add(connection);
            }
        }

        for (Connection connection: connections.values()) {
            if (!includedPlayers.contains(connection) || removeList.contains(connection)) {
                connections.remove(connection.playerName);
            }
        }
    }
}
