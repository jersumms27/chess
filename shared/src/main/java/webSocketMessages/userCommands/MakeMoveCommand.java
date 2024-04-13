package webSocketMessages.userCommands;

import chess.ChessGame;
import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    private final int gameID;
    private final ChessMove move;
    private final String moveStr;
    private final ChessGame.TeamColor playerColor;

    public MakeMoveCommand(String authToken, int gameID, ChessMove move, String moveStr, ChessGame.TeamColor playerColor) {
        super(authToken);
        this.gameID = gameID;
        this.move = move;
        this.commandType = CommandType.MAKE_MOVE;
        this.moveStr = moveStr;
        this.playerColor = playerColor;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessMove getMove() {
        return move;
    }

    public String getMoveStr() {
        return moveStr;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
}
