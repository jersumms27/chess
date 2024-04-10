package server.webSocket;

import chess.*;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import dataAccess.GameDAO;
import handler.GameHandler;
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
            case MAKE_MOVE -> makeMove((MakeMoveCommand) command, session);
            case LEAVE -> leave((LeaveCommand) command, session);
            case RESIGN -> resign((ResignCommand) command, session);
        }
    }

    private void joinPlayer(JoinGameCommand command, Session session) {
        int gameID = command.getGameID();
        String auth = command.getAuthString();
        ChessGame.TeamColor playerColor = command.getPlayerColor();

        ListGamesResponse response = gson.fromJson(gameHandler.listGames(auth), ListGamesResponse.class);
        ChessGame game = getGameFromID(gameID, auth, response);

        String message = "joinPlayer with gameID " + gameID + " and color " + playerColor;
        ServerMessage serverMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
    }

    private void joinObserver(JoinObserverCommand command, Session session) {
        int gameID = command.getGameID();
        String auth = command.getAuthString();

        ListGamesResponse response = gson.fromJson(gameHandler.listGames(auth), ListGamesResponse.class);
        ChessGame game = getGameFromID(gameID, command.getAuthString(), response);

        String message = "joinObserver with gameID " + gameID;
        ServerMessage serverMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
    }

    private void makeMove(MakeMoveCommand command, Session session) {
        int gameID = command.getGameID();
        ChessMove move = command.getMove();
        String auth = command.getAuthString();

        ListGamesResponse response = gson.fromJson(gameHandler.listGames(auth), ListGamesResponse.class);
        ChessGame game = getGameFromID(gameID, command.getAuthString(), response);

        String message = "makeMove with gameID " + gameID + " and move " + move.toString();
        ServerMessage serverMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
    }

    private void leave(LeaveCommand command, Session session) {
        int gameID = command.getGameID();

        String message = "leave with gameID " + gameID;
    }

    private void resign(ResignCommand command, Session session) {
        int gameID = command.getGameID();

        String message = "resign with gameID " + gameID;
    }

    private ChessGame getGameFromID(int gameID, String auth, ListGamesResponse response) {
        Collection<GameData> games = response.games();
        for (GameData game: games) {
            if (game.gameID() == gameID) {
                return game.game();
            }
        }

        return null;
    }
}
