package webSocketMessages.userCommands;

public class ResignCommand extends UserGameCommand {
    private final int gameID;

    public ResignCommand(String authToken, String playerName, int gameID) {
        super(authToken, playerName);
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }
}
