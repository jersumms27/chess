package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        HashMap<String, Boolean> directions = new HashMap<>();

        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();


        if (type == PieceType.KING) {
            moves.addAll(moveDiagonal(board, myPosition, moves, 1));
            moves.addAll(moveStraight(board, myPosition, moves, 1));
        } else if (type == PieceType.QUEEN) {
            moves.addAll(moveDiagonal(board, myPosition, moves, 8));
            moves.addAll(moveStraight(board, myPosition, moves, 8));
        } else if (type == PieceType.BISHOP) {
            moves.addAll(moveDiagonal(board, myPosition, moves, 8));
        } else if (type == PieceType.KNIGHT) {
            moves.addAll(moveKnight(board, myPosition, moves));
        } else if (type == PieceType.ROOK) {
            moves.addAll(moveStraight(board, myPosition, moves, 8));
        } else if (type == PieceType.PAWN) {
            int numMovesUp = 1;
            if ((pieceColor == ChessGame.TeamColor.WHITE && myPosition.getRow() == 2)
              || pieceColor == ChessGame.TeamColor.BLACK && myPosition.getRow() == 7){
                numMovesUp = 2;
            }
            moves.addAll(moveDiagonal(board, myPosition, moves, 1));
            moves.addAll(moveStraight(board, myPosition, moves, numMovesUp));
        }

        return moves;
    }

    private Collection<ChessMove> moveStraight(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int numMoves) {
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();

        int moveRow;
        int moveCol;

        boolean[] blocked = new boolean[4];
        if (type == PieceType.PAWN){
            blocked[1] = true;
            blocked[3] = true;
            if (pieceColor == ChessGame.TeamColor.WHITE){
                blocked[2] = true;
            }
            else{
                blocked[0] = true;
            }
        }

        ChessPosition movePosition;
        ChessPiece piece;
        ChessMove move;

        for (int i = 1; i <= numMoves; i++) {
            //Direction up
            moveRow = myRow + i;
            moveCol = myCol;
            if (moveRow < 9 && !blocked[0]) {
                movePosition = new ChessPosition(moveRow, moveCol);
                piece = board.getPiece(movePosition);
                move = new ChessMove(myPosition, movePosition, null);

                if (piece == null) {
                    moves.add(move);
                } else if (piece.getTeamColor() != pieceColor) {
                    moves.add(move);
                    blocked[0] = true;
                } else if (piece.getTeamColor() == pieceColor) {
                    blocked[0] = true;
                }
            }

            //Direction left
            moveRow = myRow;
            moveCol = myCol - i;
            if (moveCol > 0 && !blocked[1]) {
                movePosition = new ChessPosition(moveRow, moveCol);
                piece = board.getPiece(movePosition);
                move = new ChessMove(myPosition, movePosition, null);

                if (piece == null) {
                    moves.add(move);
                } else if (piece.getTeamColor() != pieceColor) {
                    moves.add(move);
                    blocked[1] = true;
                } else if (piece.getTeamColor() == pieceColor) {
                    blocked[1] = true;
                }
            }

            //Direction down
            moveRow = myRow - i;
            moveCol = myCol;
            if (moveRow > 0 && !blocked[2]) {
                movePosition = new ChessPosition(moveRow, moveCol);
                piece = board.getPiece(movePosition);
                move = new ChessMove(myPosition, movePosition, null);

                if (piece == null) {
                    moves.add(move);
                } else if (piece.getTeamColor() != pieceColor) {
                    moves.add(move);
                    blocked[2] = true;
                } else if (piece.getTeamColor() == pieceColor) {
                    blocked[2] = true;
                }
            }

            //Direction right
            moveRow = myRow;
            moveCol = myCol + i;
            if (moveCol < 9 && !blocked[3]) {
                movePosition = new ChessPosition(moveRow, moveCol);
                piece = board.getPiece(movePosition);
                move = new ChessMove(myPosition, movePosition, null);

                if (piece == null) {
                    moves.add(move);
                } else if (piece.getTeamColor() != pieceColor) {
                    moves.add(move);
                    blocked[3] = true;
                } else if (piece.getTeamColor() == pieceColor) {
                    blocked[3] = true;
                }
            }
        }

        return moves;
    }

    private Collection<ChessMove> moveDiagonal(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int numMoves) {
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();

        int moveRow;
        int moveCol;

        boolean[] blocked = new boolean[4];
        if (type == PieceType.PAWN){
            if (pieceColor == ChessGame.TeamColor.WHITE) {
                blocked[2] = true;
                blocked[3] = true;
            }
            else{
                blocked[0] = true;
                blocked[1] = true;
            }
        }

        ChessPosition movePosition;
        ChessPiece piece;
        ChessMove move;

        for (int i = 1; i <= numMoves; i++) {
            //Direction up/right
            moveRow = myRow + i;
            moveCol = myCol + i;
            if (moveRow < 9 && moveCol < 9 && !blocked[0]) {
                movePosition = new ChessPosition(moveRow, moveCol);
                piece = board.getPiece(movePosition);
                move = new ChessMove(myPosition, movePosition, null);

                if (piece == null) {
                    if (type != PieceType.PAWN) {
                        moves.add(move);
                    }
                } else if (piece.getTeamColor() != pieceColor) {
                    moves.add(move);
                    blocked[0] = true;
                } else if (piece.getTeamColor() == pieceColor) {
                    blocked[0] = true;
                }
            }

            //Direction up/left
            moveCol = myCol - i;
            if (moveRow < 9 && moveCol > 0 && !blocked[1]) {
                movePosition = new ChessPosition(moveRow, moveCol);
                piece = board.getPiece(movePosition);
                move = new ChessMove(myPosition, movePosition, null);

                if (piece == null) {
                    if (type != PieceType.PAWN) {
                        moves.add(move);
                    }
                } else if (piece.getTeamColor() != pieceColor) {
                    moves.add(move);
                    blocked[1] = true;
                } else if (piece.getTeamColor() == pieceColor) {
                    blocked[1] = true;
                }
            }

            //Direction down/left
            moveRow = myRow - i;
            if (moveRow > 0 && moveCol > 0 && !blocked[2]) {
                movePosition = new ChessPosition(moveRow, moveCol);
                piece = board.getPiece(movePosition);
                move = new ChessMove(myPosition, movePosition, null);

                if (piece == null) {
                    moves.add(move);
                } else if (piece.getTeamColor() != pieceColor) {
                    moves.add(move);
                    blocked[2] = true;
                } else if (piece.getTeamColor() == pieceColor) {
                    blocked[2] = true;
                }
            }

            //Direction down/right
            moveCol = myCol + i;
            if (moveRow > 0 && moveCol < 9 && !blocked[3]) {
                movePosition = new ChessPosition(moveRow, moveCol);
                piece = board.getPiece(movePosition);
                move = new ChessMove(myPosition, movePosition, null);

                if (piece == null) {
                    moves.add(move);
                } else if (piece.getTeamColor() != pieceColor) {
                    moves.add(move);
                    blocked[3] = true;
                } else if (piece.getTeamColor() == pieceColor) {
                    blocked[3] = true;
                }
            }
        }

        return moves;
    }

    private Collection<ChessMove> moveKnight(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves) {
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();
        int moveRow;
        int moveCol;
        ChessPiece piece;
        ChessPosition movePosition;

        int[][] directions = {{2, 1}, {2, -1}, {1, -2}, {-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}, {1, 2}};

        for (int i = 0; i < 8; i++){
            moveRow = myRow + directions[i][0];
            moveCol = myCol + directions[i][1];
            if (moveRow > 0 && moveRow < 9 && moveCol > 0 && moveCol < 9) {
                movePosition = new ChessPosition(moveRow, moveCol);
                piece = board.getPiece(movePosition);
                if (piece == null || piece.getTeamColor() != pieceColor){
                    moves.add(new ChessMove(myPosition, movePosition, null));
                }
            }
        }

        return moves;
    }
}