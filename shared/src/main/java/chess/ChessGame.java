package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor teamTurn;

    public ChessGame() {
        teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
       return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);

        //illegal if piece cannot move there
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);

        //illegal if move puts king in check
        ChessBoard futureBoard = board.clone();

        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPos = move.getStartPosition();
        ChessPiece piece = board.getPiece(startPos);

        //check if invalid move
        if (piece == null || piece.getTeamColor() != teamTurn) {
            throw new InvalidMoveException("Invalid move");
        }
        else if (!validMoves(startPos).contains(move)) {
            throw new InvalidMoveException("Invalid move");
        }

        //make the actual move
        ChessPosition movePos = move.getEndPosition();
        board.addPiece(movePos, piece);
        board.addPiece(startPos, null);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //get position of king
        ChessPosition kingPos = null;
        HashMap<ChessPosition, ChessPiece> enemyPieces = new HashMap<>();

        ChessPosition pos;
        ChessPiece piece;

        int row = 1;
        int col = 1;

        while (kingPos == null) {
            pos = new ChessPosition(row, col);
            piece = board.getPiece(pos);
            if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                kingPos = pos;
            }

            if (col == 8) {
                row += 1;
                col = 1;
            } else {
                col ++;
            }
        }

        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                pos = new ChessPosition(r, c);
                piece = board.getPiece(pos);
                if (piece != null && piece.getTeamColor() != teamColor){
                    for (ChessMove move: piece.pieceMoves(board, pos)) {
                        if (move.getEndPosition() == kingPos){
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
