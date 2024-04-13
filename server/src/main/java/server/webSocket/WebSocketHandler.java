package server.webSocket;

import chess.*;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import dataAccess.DataAccessException;
import handler.GameHandler;
import handler.Handler;
import handler.UserHandler;
import model.GameData;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.server.HttpChannelState;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import response.ListGamesResponse;
import service.GameService;
import webSocketMessages.userCommands.*;
import webSocketMessages.serverMessages.*;

import javax.swing.*;
import java.io.IOException;
import java.util.Collection;

// SERVER sends messages and receives commands from CLIENT
@WebSocket
public class WebSocketHandler implements Handler {
    ConnectionManager connections;

    public WebSocketHandler() {
        connections = new ConnectionManager();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        Gson gson = new Gson();
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
        UserGameCommand.CommandType type = command.getCommandType();

        if (type.equals(UserGameCommand.CommandType.JOIN_PLAYER)) {
            JoinGameCommand joinGameCommand = gson.fromJson(message, JoinGameCommand.class);
            joinPlayer(joinGameCommand, session);
        } else if (type.equals(UserGameCommand.CommandType.JOIN_OBSERVER)) {
            JoinObserverCommand joinObserverCommand = gson.fromJson(message, JoinObserverCommand.class);
            joinObserver(joinObserverCommand, session);
        } else if (type.equals(UserGameCommand.CommandType.MAKE_MOVE)) {
            MakeMoveCommand makeMoveCommand = gson.fromJson(message, MakeMoveCommand.class);
            makeMove(makeMoveCommand);
        } else if (type.equals(UserGameCommand.CommandType.LEAVE)) {
            LeaveCommand leaveCommand = gson.fromJson(message, LeaveCommand.class);
            leave(leaveCommand);
        } else if (type.equals(UserGameCommand.CommandType.RESIGN)) {
            ResignCommand resignCommand = gson.fromJson(message, ResignCommand.class);
            resign(resignCommand);
        }

        //switch (command.getCommandType()) {
        //    case JOIN_PLAYER -> joinPlayer(command, session);
        //    case JOIN_OBSERVER -> joinObserver((JoinObserverCommand) command, session);
        //    case MAKE_MOVE -> makeMove((MakeMoveCommand) command);
        //    case LEAVE -> leave((LeaveCommand) command);
        //    case RESIGN -> resign((ResignCommand) command);
        //}
    }

    private void joinPlayer(JoinGameCommand command, Session session) throws IOException {
        int gameID = command.getGameID();
        String auth = command.getAuthString();
        ChessGame.TeamColor playerColor = command.getPlayerColor();
        String playerName = command.getPlayerName();

        ChessGame game = getGameFromID(gameID, auth);

        connections.add(playerName, session, gameID);

        // server sends LOAD_GAME message back to root client
        ServerMessage loadGame = new LoadGameMessage(game);
        connections.broadcastToRoot(playerName, loadGame);

        // server sends NOTIFICATION to all other clients informing that root client has joined
        String message = playerName + " joined game as " + playerColor.toString();
        ServerMessage notification = new NotificationMessage(message);
        connections.broadcastExcludingRoot(playerName, notification);
    }

    private void joinObserver(JoinObserverCommand command, Session session) throws IOException {
        int gameID = command.getGameID();
        String auth = command.getAuthString();
        String playerName = command.getPlayerName();

        ChessGame game = getGameFromID(gameID, auth);

        connections.add(playerName, session, gameID);

        // server sends LOAD_GAME message back to root client
        ServerMessage loadGame = new LoadGameMessage(game);
        connections.broadcastToRoot(playerName, loadGame);

        // server sends NOTIFICATION to all other clients informing that root client has joined
        String message = playerName + " joined game as an observer";
        ServerMessage notification = new NotificationMessage(message);
        connections.broadcastExcludingRoot(playerName, notification);
    }

    private void makeMove(MakeMoveCommand command) throws IOException, InvalidMoveException, DataAccessException {
        int gameID = command.getGameID();
        ChessMove move = command.getMove();
        String auth = command.getAuthString();
        String playerName = command.getPlayerName();

        ChessGame game = getGameFromID(gameID, auth);
        game.makeMove(move);
        gameService.updateGame(game, gameID, "");

        // server sends LOAD_GAME message to all clients
        ServerMessage loadGame = new LoadGameMessage(game);
        connections.broadcastToEveryone(playerName, loadGame);

        // server sends NOTIFICATION to all other clients informing what move was made
        String message = playerName + " made the move " + command.getMoveStr();
        ServerMessage notification = new NotificationMessage(message);
        connections.broadcastExcludingRoot(playerName, notification);
    }

    private void leave(LeaveCommand command) throws IOException, DataAccessException {
        int gameID = command.getGameID();
        String auth = command.getAuthString();
        String playerName = command.getPlayerName();

        ChessGame game = getGameFromID(gameID, auth);
        gameService.updateGame(game, gameID, playerName);

        //connections.remove(playerName);

        // servers sends NOTIFICATION to all other clients that root client has left
        String message = playerName + " left the game";
        ServerMessage notification = new NotificationMessage(message);
        connections.broadcastExcludingRoot(playerName, notification);
        connections.remove(playerName);

    }

    private void resign(ResignCommand command) throws IOException {
        int gameID = command.getGameID();
        String playerName = command.getPlayerName();

        //TODO: server marks game as over, update game in database

        String message = playerName + " resigned from the game";
        ServerMessage notification = new NotificationMessage(message);
        connections.broadcastToEveryone(playerName, notification);
    }

    private ChessGame getGameFromID(int gameID, String auth) {
        //ListGamesResponse response = gson.fromJson(gameService.listGames(auth), ListGamesResponse.class);
        ListGamesResponse response = gameService.listGames(auth);

        Collection<GameData> games = response.games();
        for (GameData game: games) {
            if (game.gameID() == gameID) {
                return game.game();
            }
        }

        return null;
    }
}
