package server.webSocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String playerName;
    public Session session;
    public int gameID;

    public Connection(String playerName, Session session, int gameID) {
        this.playerName = playerName;
        this.session = session;
        this.gameID = gameID;
    }

    public void send(String message) throws IOException {
        //System.out.println("sending message " + message + " to " + playerName);
        session.getRemote().sendString(message);
    }
}
