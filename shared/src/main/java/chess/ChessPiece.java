package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor color;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        color = pieceColor;
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
        return color;
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
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPos) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int[][] straightDirs = {{1, 0}, {0, -1}, {-1, 0}, {0, 1}};
        int[][] diagonalDirs = {{1, 1}, {1, -1}, {-1, -1}, {-1, 1}};
        int[][] straightAndDiagonalDirs = {{1, 0}, {0, -1}, {-1, 0}, {0, 1}, {1, 1}, {1, -1}, {-1, -1}, {-1, 1}};
        int[][] knightDirs = {{2, 1}, {2, -1}, {1, -2}, {-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}, {1, 2}};

        if (type == PieceType.ROOK){
            moves.addAll(calculateMoves(board, myPos, straightDirs, 8));
        }
        else if (type == PieceType.KNIGHT){
            moves.addAll(calculateMoves(board, myPos, knightDirs, 1));
        }
        else if (type == PieceType.BISHOP){
            moves.addAll(calculateMoves(board, myPos, diagonalDirs, 8));
        }
        else if (type == PieceType.QUEEN){
            moves.addAll(calculateMoves(board, myPos, straightAndDiagonalDirs, 8));
        }
        else if (type == PieceType.KING){
            moves.addAll(calculateMoves(board, myPos, straightAndDiagonalDirs, 1));
        }
        else if (type == PieceType.PAWN){
            moves.addAll(movePawn(board, myPos));
        }

        return moves;
    }

    private Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPos, int[][] dirs, int numMoves){
        int myRow = myPos.getRow();
        int myCol = myPos.getColumn();
        int moveRow;
        int moveCol;
        ChessPosition movePos;
        ChessPiece piece;
        ArrayList<ChessMove> moves = new ArrayList<>();

        int numDirs = dirs.length;

        for (int d = 0; d < numDirs; d++){
            for (int m = 1; m <= numMoves; m++){
                moveRow = myRow + m * dirs[d][0];
                moveCol = myCol + m * dirs[d][1];

                if (!outOfBounds(moveRow, moveCol)){
                    movePos = new ChessPosition(moveRow, moveCol);
                    piece = board.getPiece(movePos);

                    if (piece == null || piece.getTeamColor() != color){
                        moves.add(new ChessMove(myPos, movePos, null));
                    }
                    if (piece != null){
                        break;
                    }
                }
            }
        }

        return moves;
    }

    private Collection<ChessMove> movePawn(ChessBoard board, ChessPosition myPosition) {
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();
        int moveRow;
        int moveCol;
        ChessPosition movePos;
        ArrayList<ChessMove> moves = new ArrayList<>();

        int promoteRow;
        int forward;
        if (color == ChessGame.TeamColor.WHITE){
            forward = 1;
            promoteRow = 7;
        }
        else{
            forward = -1;
            promoteRow = 2;
        }

        //One forward
        moveRow = myRow + forward;
        moveCol = myCol;
        movePos = new ChessPosition(moveRow, moveCol);

        if (board.getPiece(movePos) == null){
            moves = addPawnMoves(myPosition, movePos, myRow, promoteRow, moves);

            //Two forward
            if ((color == ChessGame.TeamColor.WHITE && myRow == 2) || (color == ChessGame.TeamColor.BLACK && myRow == 7)) {
                moveRow += forward;
                movePos = new ChessPosition(moveRow, moveCol);

                if (board.getPiece(movePos) == null){
                    moves.add(new ChessMove(myPosition, movePos, null));
                }
            }
        }

        //Forward/right
        moveRow = myRow + forward;
        moveCol = myCol + 1;
        movePos = new ChessPosition(moveRow, moveCol);

        if (!outOfBounds(moveRow, moveCol) && board.getPiece(movePos) != null && board.getPiece(movePos).getTeamColor() != color){
            moves = addPawnMoves(myPosition, movePos, myRow, promoteRow, moves);
        }

        //Forward/left
        moveCol = myCol - 1;
        movePos= new ChessPosition(moveRow, moveCol);
        if (!outOfBounds(moveRow, moveCol) && board.getPiece(movePos) != null && board.getPiece(movePos).getTeamColor() != color){
            moves = addPawnMoves(myPosition, movePos, myRow, promoteRow, moves);
        }

        return moves;
    }

    private ArrayList<ChessMove> addPawnMoves(ChessPosition myPos, ChessPosition movePos, int myRow, int promoteRow, ArrayList<ChessMove> moves) {
        if (myRow == promoteRow) {
            moves.add(new ChessMove(myPos, movePos, PieceType.ROOK));
            moves.add(new ChessMove(myPos, movePos, PieceType.KNIGHT));
            moves.add(new ChessMove(myPos, movePos, PieceType.BISHOP));
            moves.add(new ChessMove(myPos, movePos, PieceType.QUEEN));
        } else {
            moves.add(new ChessMove(myPos, movePos, null));
        }
        return moves;
    }

    private boolean outOfBounds(int row, int col){
        return (row < 1 || row > 8 || col < 1 || col > 8);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return color == that.color && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }
}