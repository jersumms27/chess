package chess;

import java.util.ArrayList;
import java.util.Collection;

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
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();

        if(type == PieceType.KING){

        }
        else if(type == PieceType.QUEEN){

        }
        else if(type == PieceType.BISHOP){
            boolean[] blocked = new boolean[4];
            ChessPosition movePosition;
            for(int i = 1; i < 8; i++){
                //Direction up/right
                movePosition = new ChessPosition(myRow + i, myCol + i);
                if(!blocked[0] && !outOfBounds(movePosition)) {
                    if(board.getPiece(movePosition) != null){
                        blocked[0] = true;
                    }
                    if(board.getPiece(movePosition) == null || board.getPiece(movePosition).getTeamColor() != pieceColor){
                        System.out.println("adding row: " + movePosition.getRow() + ", col: " + movePosition.getColumn());
                        moves.add(new ChessMove(myPosition, movePosition, null));
                    }
                }
                //Direction up/left
                movePosition = new ChessPosition(myRow + i, myCol - i);
                if(!blocked[1] && !outOfBounds(movePosition)){
                    if(board.getPiece(movePosition) != null){
                        blocked[1] = true;
                    }
                    if(board.getPiece(movePosition) == null || board.getPiece(movePosition).getTeamColor() != pieceColor){
                        System.out.println("adding row: " + movePosition.getRow() + ", col: " + movePosition.getColumn());
                        moves.add(new ChessMove(myPosition, movePosition, null));
                    }
                }
                //Direction down/left
                movePosition = new ChessPosition(myRow - i, myCol - i);
                if(!blocked[2] && !outOfBounds(movePosition)){
                    if(board.getPiece(movePosition) != null){
                        blocked[2] = true;
                    }
                    if(board.getPiece(movePosition) == null || board.getPiece(movePosition).getTeamColor() != pieceColor){
                        System.out.println("adding row: " + movePosition.getRow() + ", col: " + movePosition.getColumn());
                        moves.add(new ChessMove(myPosition, movePosition, null));
                    }
                }
                //Direction down/right
                movePosition = new ChessPosition(myRow - i, myCol + i);
                if(!blocked[3] && !outOfBounds(movePosition)){
                    if(board.getPiece(movePosition) != null){
                        blocked[3] = true;
                    }
                    if(board.getPiece(movePosition) == null || board.getPiece(movePosition).getTeamColor() != pieceColor){
                        System.out.println("adding row: " + movePosition.getRow() + ", col: " + movePosition.getColumn());
                        moves.add(new ChessMove(myPosition, movePosition, null));
                    }
                }
            }
        }
        else if(type == PieceType.KNIGHT){

        }
        else if(type == PieceType.ROOK){

        }
        else if(type == PieceType.PAWN){

        }

        return moves;
    }

    private boolean outOfBounds(ChessPosition pos){
        if(pos.getRow() < 1 || pos.getRow() > 8 || pos.getColumn() < 1 || pos.getColumn() > 8){
            return true;
        }

        return false;
    }
}
