package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    private final int gameID;
    private final ChessMove move;

    public MakeMoveCommand(String authToken, String playerName, int gameID, ChessMove move) {
        super(authToken, playerName);
        this.gameID = gameID;
        this.move = move;
        this.commandType = CommandType.MAKE_MOVE;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessMove getMove() {
        return move;
    }
}
