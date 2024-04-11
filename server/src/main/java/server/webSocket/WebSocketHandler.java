package server.webSocket;

import chess.*;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import dataAccess.GameDAO;
import handler.GameHandler;
import handler.UserHandler;
import model.GameData;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.server.HttpChannelState;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import response.ListGamesResponse;
import webSocketMessages.userCommands.*;
import webSocketMessages.serverMessages.*;

import javax.swing.*;
import java.io.IOException;
import java.util.Collection;

@WebSocket
public class WebSocketHandler {
    ConnectionManager connections;
    GameHandler gameHandler;
    Gson gson;

    public WebSocketHandler(GameHandler gameHandler) {
        connections = new ConnectionManager();
        this.gameHandler = gameHandler;

        gson = new Gson();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer((JoinGameCommand) command, session);
            case JOIN_OBSERVER -> joinObserver((JoinObserverCommand) command, session);
            case MAKE_MOVE -> makeMove((MakeMoveCommand) command);
            case LEAVE -> leave((LeaveCommand) command);
            case RESIGN -> resign((ResignCommand) command);
        }
    }

    private void joinPlayer(JoinGameCommand command, Session session) throws IOException {
        int gameID = command.getGameID();
        String auth = command.getAuthString();
        ChessGame.TeamColor playerColor = command.getPlayerColor();
        String playerName = command.getPlayerName();

        ListGamesResponse response = gson.fromJson(gameHandler.listGames(auth), ListGamesResponse.class);
        ChessGame game = getGameFromID(gameID, auth);

        connections.add(playerName, session);

        // server sends LOAD_GAME message back to root client
        ServerMessage loadGame = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
        connections.broadcastToRoot(playerName, loadGame);

        // server sends NOTIFICATION to all other clients informing that root client has joined
        String message = playerName + " joined game as " + playerColor.toString();
        ServerMessage notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcastExcludingRoot(playerName, notification);
    }

    private void joinObserver(JoinObserverCommand command, Session session) throws IOException {
        int gameID = command.getGameID();
        String auth = command.getAuthString();
        String playerName = command.getPlayerName();

        ChessGame game = getGameFromID(gameID, auth);

        connections.add(playerName, session);

        // server sends LOAD_GAME message back to root client
        ServerMessage loadGame = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
        connections.broadcastToRoot(playerName, loadGame);

        // server sends NOTIFICATION to all other clients informing that root client has joined
        String message = playerName + " joined game as an observer";
        ServerMessage notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcastExcludingRoot(playerName, notification);
    }

    private void makeMove(MakeMoveCommand command) throws IOException, InvalidMoveException {
        int gameID = command.getGameID();
        ChessMove move = command.getMove();
        String auth = command.getAuthString();
        String playerName = command.getPlayerName();

        ChessGame game = getGameFromID(gameID, auth);
        game.makeMove(move);

        // server sends LOAD_GAME message to all clients
        ServerMessage loadGame = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
        connections.broadcastToEveryone(loadGame);

        // server sends NOTIFICATION to all other clients informing what move was made
        String message = playerName + " made the move " + move.toString();
        ServerMessage notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcastExcludingRoot(playerName, notification);
    }

    private void leave(LeaveCommand command) throws IOException {
        int gameID = command.getGameID();
        String auth = command.getAuthString();
        String playerName = command.getPlayerName();

        //TODO: update game in database
        ChessGame game = getGameFromID(gameID, auth);

        connections.remove(playerName);

        // servers sends NOTIFICATION to all other clients that root client has left
        String message = playerName + " left the game";
        ServerMessage notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcastToEveryone(notification);
    }

    private void resign(ResignCommand command) throws IOException {
        int gameID = command.getGameID();
        String playerName = command.getPlayerName();

        //TODO: server marks game as over, update game in database

        String message = playerName + " resigned from the game";
        ServerMessage notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcastToEveryone(notification);
    }

    private ChessGame getGameFromID(int gameID, String auth) {
        ListGamesResponse response = gson.fromJson(gameHandler.listGames(auth), ListGamesResponse.class);

        Collection<GameData> games = response.games();
        for (GameData game: games) {
            if (game.gameID() == gameID) {
                return game.game();
            }
        }

        return null;
    }
}
