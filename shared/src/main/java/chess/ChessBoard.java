package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    //private HashMap<ChessPosition, ChessPiece> board;
    private ChessPiece[][] board;
    public ChessBoard() {
        board = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int row = position.getRow() - 1;
        int col = position.getColumn() - 1;

        board[row][col] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        int row = position.getRow() - 1;
        int col = position.getColumn() - 1;

        return board[row][col];
    }

    public void movePiece(ChessMove move) {
        ChessPosition startPos = move.getStartPosition();
        ChessPosition movePos = move.getEndPosition();
        ChessPiece piece = getPiece(startPos);
        if (move.getPromotionPiece() != null) {
            piece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
        }

        addPiece(movePos, piece);
        addPiece(startPos, null);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        //pawns
        for (int p = 1; p <= 8; p++){
            addPiece(new ChessPosition(2, p), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(7, p), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }

        //rooks
        addPiece(new ChessPosition(1, 1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(1, 8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8, 1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8, 8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));

        //knights
        addPiece(new ChessPosition(1, 2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1, 7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8, 2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8, 7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));

        //bishops
        addPiece(new ChessPosition(1, 3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1, 6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8, 3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8, 6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));

        //queens
        addPiece(new ChessPosition(1, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(8, 4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));

        //kings
        addPiece(new ChessPosition(1, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(8, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));


    }

    public String toString() {
        String nullChar = " ";
        HashMap<ChessPiece.PieceType, String> blackSymbols = new HashMap<>();
        blackSymbols.put(ChessPiece.PieceType.PAWN, "p");
        blackSymbols.put(ChessPiece.PieceType.ROOK, "r");
        blackSymbols.put(ChessPiece.PieceType.KNIGHT, "n");
        blackSymbols.put(ChessPiece.PieceType.BISHOP, "b");
        blackSymbols.put(ChessPiece.PieceType.QUEEN, "q");
        blackSymbols.put(ChessPiece.PieceType.KING, "k");

        HashMap<ChessPiece.PieceType, String> whiteSymbols = new HashMap<>();
        whiteSymbols.put(ChessPiece.PieceType.PAWN, "P");
        whiteSymbols.put(ChessPiece.PieceType.ROOK, "R");
        whiteSymbols.put(ChessPiece.PieceType.KNIGHT, "N");
        whiteSymbols.put(ChessPiece.PieceType.BISHOP, "B");
        whiteSymbols.put(ChessPiece.PieceType.QUEEN, "Q");
        whiteSymbols.put(ChessPiece.PieceType.KING, "K");

        String output = "";
        for (int r = 8; r >= 1; r--) {
            output += "|";
            for (int c = 1; c <= 8; c++) {
                ChessPosition pos = new ChessPosition(r, c);
                ChessPiece piece = getPiece(pos);
                if (piece == null) {
                    output += nullChar;
                }
                else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                    output += blackSymbols.get(piece.getPieceType());
                }
                else if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    output += whiteSymbols.get(piece.getPieceType());
                }
                output += "|";
            }
            output += "\n";
        }

        return output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board);
    }
}
