package webSocketMessages.serverMessages;

public class NotificationMessage extends ServerMessage {
    String message;

    public NotificationMessage(String message) {
        super();
        this.message = message;
        this.serverMessageType = ServerMessageType.NOTIFICATION;
    }

    public String getMessage() {
        return message;
    }
}
