package chess;

import java.util.HashMap;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private HashMap<String, ChessPiece> board;
    public ChessBoard() {
        board = new HashMap<>();
        for(int r = 1; r < 9; r++){
            for(int c = 1; c < 9; c++){
                String key = Integer.toString(r) + Integer.toString(c);
                board.put(key, null);
            }
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        String key = Integer.toString(position.getRow()) + Integer.toString(position.getColumn());
        board.put(key, piece);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        String key = Integer.toString(position.getRow()) + Integer.toString(position.getColumn());
        return board.get(key);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        throw new RuntimeException("Not implemented");
    }

    private String boardToString(){
        String str = "";
        ChessPosition pos;

        for(int r = 8; r > 0; r--){
            str += "|";
            for(int c = 1; c < 9; c++) {
                pos = new ChessPosition(r, c);
                if (getPiece(pos) == null) {
                    str += " ";
                } else {
                    str += "B";
                }
                str += "|";
            }
            str += "\n";
        }

        return str;
    }
}
