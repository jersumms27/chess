package webSocketMessages.userCommands;

public class JoinObserverCommand extends UserGameCommand {
    private final int gameID;

    public JoinObserverCommand(String authToken, String playerName, int gameID) {
        super(authToken, playerName);
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }
}
