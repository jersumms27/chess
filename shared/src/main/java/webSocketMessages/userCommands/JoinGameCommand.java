package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinGameCommand extends UserGameCommand {
    private final int gameID;
    private final ChessGame.TeamColor playerColor;

    public JoinGameCommand(String authToken, String playerName, int gameID, ChessGame.TeamColor playerColor) {
        super(authToken, playerName);
        this.gameID = gameID;
        this.playerColor = playerColor;
        this.commandType = CommandType.JOIN_PLAYER;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
}
