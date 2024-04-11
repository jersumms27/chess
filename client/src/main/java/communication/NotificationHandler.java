package communication;
import webSocketMessages.serverMessages.*;

public class NotificationHandler {
    void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case ServerMessage.ServerMessageType.LOAD_GAME -> loadGame((LoadGameMessage) message);
            case ServerMessage.ServerMessageType.NOTIFICATION -> notification((NotificationMessage) message);
            case ServerMessage.ServerMessageType.ERROR -> error((ErrorMessage) message);
        }
    }

    void loadGame(LoadGameMessage message) {
        System.out.println(message.getGame());
        System.exit(0);
    }

    void notification(NotificationMessage message) {
        System.out.println(message.getMessage());
        System.exit(0);
    }

    void error(ErrorMessage message) {
        System.out.println(message.getErrorMessage());
        System.exit(0);
    }
}
