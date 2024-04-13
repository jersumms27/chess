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

    public void add(String playerName, Session session, int gameID) {
        Connection connection = new Connection(playerName, session, gameID);
        connections.put(playerName, connection);
    }

    public void remove(String playerName) {
        connections.remove(playerName);
    }

    public void broadcastToEveryone(String rootPlayer, ServerMessage message) throws IOException {
        //broadcast(new ArrayList<>(connections.values()), message);
        ArrayList<Connection> include = new ArrayList<>();
        System.out.println("broadcasting to everyone, root player name: " + rootPlayer + ", id is " + connections.get(rootPlayer).gameID);
        System.out.println(connections.get(rootPlayer));
        int gameID = connections.get(rootPlayer).gameID;
        for (Connection con: connections.values()) {
            if (con.gameID == gameID) {
                System.out.println("will broadcast to " + con.playerName);
                include.add(con);
            } else {
                System.out.println("will not broadcast to " + con.playerName + " because their id is " + con.gameID);
            }
        }

        broadcast(include, message);
    }

    public void broadcastExcludingRoot(String rootPlayer, ServerMessage message) throws IOException {
        ArrayList<Connection> include = new ArrayList<>();
        int gameID = connections.get(rootPlayer).gameID;
        //for (Connection connection: connections.values()) {
        //    if (!connection.playerName.equals(rootPlayer)) {
        //        include.add(connection);
        //    }
        //}
        for (String playerName: connections.keySet()) {
            if (!playerName.equals(rootPlayer) && connections.get(playerName).gameID == gameID) {
                include.add(connections.get(playerName));
            }
        }

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
                connection.send(gson.toJson(message));
            } else {
                removeList.add(connection);
            }
        }

        //for (Connection connection: connections.values()) {
        //    if (!includedPlayers.contains(connection) || removeList.contains(connection)) {
        //        connections.remove(connection.playerName);
        //    }
        //}
    }
}
