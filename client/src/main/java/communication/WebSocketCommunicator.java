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
    private GameMenu menu;

    public WebSocketCommunicator(String url, GameMenu menu) throws Exception {
        newGame = null;
        lock = new Object();
        this.menu = menu;
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
                    try {
                        setGame();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
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

    public void joinPlayer(String auth, int gameID, ChessGame.TeamColor playerColor) throws IOException {
        JoinGameCommand command = new JoinGameCommand(auth, gameID, playerColor);
        System.out.println(gson.toJson(command));
        this.session.getBasicRemote().sendText(gson.toJson(command));
    }

    public void joinObserver(String auth, int gameID) throws IOException {
        JoinObserverCommand command = new JoinObserverCommand(auth, gameID);
        this.session.getBasicRemote().sendText(gson.toJson(command));
    }

    public void makeMove(String auth, int gameID, ChessMove move, String moveStr) throws IOException {
        MakeMoveCommand command = new MakeMoveCommand(auth, gameID, move, moveStr);
        this.session.getBasicRemote().sendText(gson.toJson(command));
    }

    public void leave(String auth, int gameID) throws IOException {
        LeaveCommand command = new LeaveCommand(auth, gameID);
        this.session.getBasicRemote().sendText(gson.toJson(command));
    }

    public void resign(String auth, int gameID) throws IOException {
        ResignCommand command = new ResignCommand(auth, gameID);
        this.session.getBasicRemote().sendText(gson.toJson(command));
    }

    public void setGame() throws InterruptedException {
        synchronized (lock) {
            while (!gameUpdated) {
                lock.wait();
            }
        }
        gameUpdated = false;

        //return newGame;
        GameMenu.currentGame = newGame;
        menu.redrawChessBoard();
    }
}
