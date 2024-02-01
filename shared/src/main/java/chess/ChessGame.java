package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

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

    private void changeTeamTurn() {
        if (teamTurn == TeamColor.BLACK) {
            setTeamTurn(TeamColor.WHITE);
        } else {
            setTeamTurn(TeamColor.BLACK);
        }
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
        TeamColor color = piece.getTeamColor();

        //illegal if piece cannot move there
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> legalMoves = new ArrayList<>();

        //illegal if move puts king in check
        for (ChessMove move: moves) {
            ChessPiece captured = board.getPiece(move.getEndPosition());
            board.movePiece(move);
            if (!isInCheck(color)) {
                legalMoves.add(move);
            }
            board.movePiece(move.getReverseMove());
            board.addPiece(move.getEndPosition(), captured);
        }
        return legalMoves;
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
        if (piece == null || piece.getTeamColor() != teamTurn || !validMoves(startPos).contains(move)) {
            throw new InvalidMoveException("Invalid move");
        }

        //make the actual move
        board.movePiece(move);
        changeTeamTurn();
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

            if (row == 8 && col == 8) {
                return false;
            }
        }

        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                pos = new ChessPosition(r, c);
                piece = board.getPiece(pos);
                if (piece != null && piece.getTeamColor() != teamColor){
                    for (ChessMove move: piece.pieceMoves(board, pos)) {
                        if (Objects.equals(move.getEndPosition(), kingPos)){
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
        return (isInCheck(teamColor) && isInStalemate(teamColor));
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        //get position of king
        ChessPosition kingPos = null;

        ChessPosition pos;
        ChessPiece piece = null;

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

        for (ChessMove move: piece.pieceMoves(board, kingPos)) {
            ChessPiece captured = board.getPiece(move.getEndPosition());
            board.movePiece(move);
            if (!isInCheck(teamColor)) {
                board.movePiece(move.getReverseMove());
                board.addPiece(move.getEndPosition(), captured);
                return false;
            }
            board.movePiece(move.getReverseMove());
            board.addPiece(move.getEndPosition(), captured);
        }

        return true;
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
