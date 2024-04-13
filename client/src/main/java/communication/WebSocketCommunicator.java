package communication;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.userCommands.*;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

// WebSocket endpoint annotation
@WebSocket
public class WebSocketCommunicator {
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

            gson = new Gson();
            notifHandler = new NotificationHandler();

            WebSocketClient client = new WebSocketClient();
            client.start();
            client.connect(this, socketURI);

        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        this.session = session;
    }

    @OnWebSocketError
    public void onError(Throwable cause) {
        // Handle error
    }

    public void joinPlayer(String auth, int gameID, ChessGame.TeamColor playerColor) throws IOException {
        JoinGameCommand command = new JoinGameCommand(auth, gameID, playerColor);
        session.getRemote().sendString(gson.toJson(command));
    }

    public void joinObserver(String auth, int gameID) throws IOException {
        JoinObserverCommand command = new JoinObserverCommand(auth, gameID);
        this.session.getRemote().sendString(gson.toJson(command));
    }

    public void makeMove(String auth, int gameID, ChessMove move, String moveStr) throws IOException {
        MakeMoveCommand command = new MakeMoveCommand(auth, gameID, move, moveStr);
        this.session.getRemote().sendString(gson.toJson(command));
    }

    public void leave(String auth, int gameID) throws IOException {
        LeaveCommand command = new LeaveCommand(auth, gameID);
        this.session.getRemote().sendString(gson.toJson(command));
    }

    public void resign(String auth, int gameID) throws IOException {
        ResignCommand command = new ResignCommand(auth, gameID);
        this.session.getRemote().sendString(gson.toJson(command));
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
