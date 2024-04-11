package webSocketMessages.userCommands;

public class LeaveCommand extends UserGameCommand {
    private final int gameID;

    public LeaveCommand(String authToken, String playerName, int gameID) {
        super(authToken, playerName);
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }
}
