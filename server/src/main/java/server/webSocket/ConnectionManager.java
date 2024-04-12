package server.webSocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
    Gson gson = new Gson();

    public void add(String playerName, Session session) {
        Connection connection = new Connection(playerName, session);
        connections.put(playerName, connection);
        System.out.println("added, so connections is now: " + connections);
    }

    public void remove(String playerName) {
        connections.remove(playerName);
        System.out.println("remove, so connections is now: " + connections);
    }

    public void broadcastToEveryone(ServerMessage message) throws IOException {
        //broadcast(new ArrayList<>(connections.values()), message);
        ArrayList<Connection> include = new ArrayList<>(connections.values());
        broadcast(include, message);
    }

    public void broadcastExcludingRoot(String rootPlayer, ServerMessage message) throws IOException {
        System.out.println("broadcastExcludingRoot");
        ArrayList<Connection> include = new ArrayList<>();
        //for (Connection connection: connections.values()) {
        //    if (!connection.playerName.equals(rootPlayer)) {
        //        include.add(connection);
        //    }
        //}
        System.out.println(connections);
        for (String playerName: connections.keySet()) {
            if (!playerName.equals(rootPlayer)) {
                include.add(connections.get(playerName));
            }
        }
        System.out.println(include);

        broadcast(include, message);
    }

    public void broadcastToRoot(String rootPlayer, ServerMessage message) throws IOException {
        ArrayList<Connection> include = new ArrayList<>();
        Connection rootConnection = connections.get(rootPlayer);
        //include.add(connections.get(rootPlayer));
        if (rootConnection != null) {
            include.add(rootConnection);
        }

        broadcast(include, message);
    }

    private void broadcast(ArrayList<Connection> includedPlayers, Object message) throws IOException {
        ArrayList<Connection> removeList = new ArrayList<>();
        for (Connection connection: includedPlayers) {
            if (connection.session.isOpen()) {
                System.out.println("sending " + message + " to " + connection.playerName);
                connection.send(gson.toJson(message));
            } else {
                removeList.add(connection);
            }
        }
        System.out.println("after broadcast, connections: " + connections);

        //for (Connection connection: connections.values()) {
        //    if (!includedPlayers.contains(connection) || removeList.contains(connection)) {
        //        connections.remove(connection.playerName);
        //    }
        //}

        System.out.println("after loop thing, connections: " + connections);
    }
}
