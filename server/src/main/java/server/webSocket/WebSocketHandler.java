package server.webSocket;

import chess.*;
import com.google.gson.Gson;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.server.HttpChannelState;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.userCommands.*;

import javax.swing.*;

@WebSocket
public class WebSocketHandler {
    ConnectionManager connections;

    public WebSocketHandler() {
        connections = new ConnectionManager();
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
        ChessGame.TeamColor playerColor = command.getPlayerColor();
    }

    private void joinObserver(JoinObserverCommand command, Session session) {
        int gameID = command.getGameID();
    }

    private void makeMove(MakeMoveCommand command, Session session) {
        int gameID = command.getGameID();
        ChessMove move = command.getMove();
    }

    private void leave(LeaveCommand command, Session session) {
        int gameID = command.getGameID();
    }

    private void resign(ResignCommand command, Session session) {
        int gameID = command.getGameID();
    }
}
