package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class Board {

    private static final int NUM_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 3;
    private static final String WIDTH_SPACE = "  ";


    //public static void main(String[] args) {
    //    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    //    out.print(ERASE_SCREEN);
    //    ChessBoard board = new ChessBoard();
    //    board.resetBoard();

    //    drawBoard(out, board, false);
    //    out.println();
    //    drawBoard(out, board, true);
    //}

    public static void drawBoard(PrintStream out, ChessBoard board, boolean inverted) {
        String[] regularRowHeader = {"a", "b", "c", "d", "e", "f", "g", "h"};
        String[] invertedRowHeader = {"h", "g", "f", "e", "d", "c", "b", "a"};
        String[] regularColHeader = {"8", "7", "6", "5", "4", "3", "2", "1"};
        String[] invertedColHeader = {"1", "2", "3", "4", "5", "6", "7", "8"};

        String[] rowHeader;
        String[] colHeader;
        if (inverted) {
            rowHeader = invertedRowHeader;
            colHeader = invertedColHeader;
        } else {
            rowHeader = regularRowHeader;
            colHeader = regularColHeader;
        }

        drawHeader(out, rowHeader);
        out.println();
        int startRow = 0;
        int endRow = NUM_SQUARES;
        int incrementRow = 1;
        if (inverted) {
            startRow = NUM_SQUARES - 1;
            endRow = -1;
            incrementRow = -1;
        }
        for (int r = startRow; (r - endRow) * (r - endRow) != 0; r += incrementRow) {
            ChessPiece[] pieces = new ChessPiece[NUM_SQUARES];
            int startCol = startRow;
            int endCol = endRow;
            int incrementCol = incrementRow;
            for (int p = startCol; (p - endCol) * (p - endCol) != 0; p += incrementCol) {
                pieces[p] = board.getPiece(new ChessPosition(r + 1, p + 1));
            }
            drawRow(out, pieces, colHeader[r], r);
            out.println();
        }
        drawHeader(out, rowHeader);
        out.println();
    }

    public static void drawHeader(PrintStream out, String[] header) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(" ");
        for (int h = 0; h < NUM_SQUARES; h++) {
            out.print(SET_TEXT_COLOR_BLACK);
            out.print(WIDTH_SPACE + header[h] + WIDTH_SPACE);
        }
    }

    public static void drawRow(PrintStream out, ChessPiece[] pieces, String header, int rowNumber) {
        for (int c = 0; c < NUM_SQUARES + 2; c++) {
            if (c == 0 || c == NUM_SQUARES + 1) {
                out.print(SET_TEXT_COLOR_BLACK);
                out.print(SET_BG_COLOR_LIGHT_GREY);
                out.print(header);
            } else {
                if (rowNumber % 2 != c % 2) {
                    out.print(SET_BG_COLOR_WHITE);
                } else {
                    out.print(SET_BG_COLOR_BLACK);
                }
                out.print(pieceToString(out, pieces[c - 1]));
            }
        }
    }

    private static String pieceToString(PrintStream out, ChessPiece piece) {
        if (piece == null) {
            return WIDTH_SPACE + " " + WIDTH_SPACE;
        }
        String str = "";
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            out.print(SET_TEXT_COLOR_BLUE);
        } else {
            out.print(SET_TEXT_COLOR_RED);
        }

        ChessPiece.PieceType type = piece.getPieceType();
        if (type == ChessPiece.PieceType.PAWN) {
            str = "P";
        } else if (type == ChessPiece.PieceType.ROOK) {
            str = "R";
        } else if (type == ChessPiece.PieceType.KNIGHT) {
            str = "N";
        } else if (type == ChessPiece.PieceType.BISHOP) {
            str = "B";
        } else if (type == ChessPiece.PieceType.QUEEN) {
            str = "Q";
        } else {
            str = "K";
        }

        return WIDTH_SPACE + str + WIDTH_SPACE;
    }
}