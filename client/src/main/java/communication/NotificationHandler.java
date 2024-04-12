package communication;
import chess.ChessGame;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.*;

// CLIENT receives messages from SERVER
public class NotificationHandler {
    ChessGame notify(String message) {
        Gson gson = new Gson();
        ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
        ServerMessage.ServerMessageType type = serverMessage.getServerMessageType();
        ChessGame ret = null;

        if (type.equals(ServerMessage.ServerMessageType.LOAD_GAME)) {
            LoadGameMessage loadGameMessage = gson.fromJson(message, LoadGameMessage.class);
            ret = loadGame(loadGameMessage);
        } else if (type.equals(ServerMessage.ServerMessageType.NOTIFICATION)) {
            NotificationMessage notificationMessage = gson.fromJson(message, NotificationMessage.class);
            notification(notificationMessage);
        } else if (type.equals(ServerMessage.ServerMessageType.ERROR)) {
            ErrorMessage errorMessage = gson.fromJson(message, ErrorMessage.class);
            error(errorMessage);
        }

        return ret;
    }

    ChessGame loadGame(LoadGameMessage message) {
        return message.getGame();
    }

    void notification(NotificationMessage message) {
        System.out.println(message.getMessage());
    }

    void error(ErrorMessage message) {
        System.out.println(message.getErrorMessage());
    }
}
