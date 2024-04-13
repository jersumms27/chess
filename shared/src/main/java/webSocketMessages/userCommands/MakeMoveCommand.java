package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    private final int gameID;
    private final ChessMove move;
    private final String moveStr;

    public MakeMoveCommand(String authToken, String playerName, int gameID, ChessMove move, String moveStr) {
        super(authToken, playerName);
        this.gameID = gameID;
        this.move = move;
        this.commandType = CommandType.MAKE_MOVE;
        this.moveStr = moveStr;
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
}
