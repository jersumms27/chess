package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage {
    String errorMessage;

    public ErrorMessage(String errorMessage) {
        super();
        this.errorMessage = errorMessage;
        this.serverMessageType = ServerMessageType.ERROR;
    }
}
