package communication;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import org.eclipse.jetty.client.HttpResponseException;
//import org.eclipse.jetty.websocket.api.Session;
import javax.management.Notification;
import javax.websocket.Session;

import com.google.gson.Gson;
//import exception.ResponseException;
import webSocketMessages.serverMessages.*;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

// CLIENT sends commands to SERVER
public class WebSocketCommunicator extends Endpoint {
    Session session;
    Gson gson;
    NotificationHandler notifHandler;
    ChessGame newGame;
    private boolean gameUpdated;
    private final Object lock;

    public WebSocketCommunicator(String url) throws Exception {
        newGame = null;
        lock = new Object();
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            //URI socketURI = new URI(url);

            gson = new Gson();
            notifHandler = new NotificationHandler();

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            // set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    //ServerMessage notification = gson.fromJson(message, ServerMessage.class);
                    ChessGame potentialNewGame = notifHandler.notify(message);
                    if (potentialNewGame != null) {
                        newGame = potentialNewGame;
                    }
                    synchronized (lock) {
                        gameUpdated = true;
                        lock.notifyAll();
                    }

                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public void onOpen(javax.websocket.Session session, EndpointConfig endpointConfig) {
    }

    public ChessGame joinPlayer(String auth, String playerName, int gameID, ChessGame.TeamColor playerColor) throws IOException {
        JoinGameCommand command = new JoinGameCommand(auth, playerName, gameID, playerColor);
        this.session.getBasicRemote().sendText(gson.toJson(command));

        return newGame;
    }

    public ChessGame joinObserver(String auth, String playerName, int gameID) throws IOException {
        JoinObserverCommand command = new JoinObserverCommand(auth, playerName, gameID);
        this.session.getBasicRemote().sendText(gson.toJson(command));

        return newGame;
    }

    public ChessGame makeMove(String auth, String playerName, int gameID, ChessMove move) throws IOException {
        MakeMoveCommand command = new MakeMoveCommand(auth, playerName, gameID, move);
        this.session.getBasicRemote().sendText(gson.toJson(command));

        return newGame;
    }

    public ChessGame leave(String auth, String playerName, int gameID) throws IOException {
        LeaveCommand command = new LeaveCommand(auth, playerName, gameID);
        this.session.getBasicRemote().sendText(gson.toJson(command));

        return newGame;
    }

    public ChessGame resign(String auth, String playerName, int gameID) throws IOException {
        ResignCommand command = new ResignCommand(auth, playerName, gameID);
        this.session.getBasicRemote().sendText(gson.toJson(command));

        return newGame;
    }

    public ChessGame getGame() throws InterruptedException {
        synchronized (lock) {
            while (!gameUpdated) {
                lock.wait();
            }
        }
        gameUpdated = false;

        return newGame;
    }
}
