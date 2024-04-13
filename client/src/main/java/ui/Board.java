package ui;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

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

    //    drawBoard(board, false);
    //    out.println();
    //    drawBoard(board, true);
    //}

    public static void drawBoard(ChessGame game, boolean inverted, boolean highlight, ChessPosition start) { // default is white's perspective
        ChessBoard board = game.getGameBoard();

        String[] regularRowHeader = {"a", "b", "c", "d", "e", "f", "g", "h"};
        String[] invertedRowHeader = {"h", "g", "f", "e", "d", "c", "b", "a"};
        String[] regularColHeader = {"8", "7", "6", "5", "4", "3", "2", "1"};
        String[] invertedColHeader = {"1", "2", "3", "4", "5", "6", "7", "8"};
        //String[] colHeader = {"8", "7", "6", "5", "4", "3", "2", "1"};
        String[] colHeader = {"1", "2", "3", "4", "5", "6", "7", "8"};

        String[] rowHeader;
        //String[] colHeader;
        if (inverted) {
            rowHeader = invertedRowHeader;
            //colHeader = invertedColHeader;
        } else {
            rowHeader = regularRowHeader;
            //colHeader = regularColHeader;
        }

        drawHeader(rowHeader);
        System.out.println();
        //int startRow = 0;
        //int endRow = NUM_SQUARES;
        //int incrementRow = 1;
        //if (inverted) {
        //    startRow = NUM_SQUARES - 1;
        //    endRow = -1;
        //    incrementRow = -1;
        //}
        int startRow = NUM_SQUARES - 1;
        int endRow = -1;
        int incrementRow = -1;
        if (inverted) {
            startRow = 0;
            endRow = NUM_SQUARES;
            incrementRow = 1;
        }

        int actualRow = 1;
        for (int r = startRow; (r - endRow) * (r - endRow) != 0; r += incrementRow) {
            ChessPiece[] pieces = new ChessPiece[NUM_SQUARES];
            int startCol = startRow;
            int endCol = endRow;
            int incrementCol = incrementRow;
            int c = 0;
            for (int p = startCol; (p - endCol) * (p - endCol) != 0; p += incrementCol) {
                pieces[c] = board.getPiece(new ChessPosition(r + 1, p + 1));
                c ++;
            }
            drawRow(pieces, colHeader[r], r + 1, inverted, game, highlight, start);
            actualRow ++;
            System.out.println();
        }
        drawHeader(rowHeader);
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.println();
    }

    private static void drawHeader(String[] header) {
        System.out.print(SET_BG_COLOR_LIGHT_GREY);
        System.out.print(" ");
        for (int h = 0; h < NUM_SQUARES; h++) {
            System.out.print(SET_TEXT_COLOR_BLACK);
            System.out.print(WIDTH_SPACE + header[h] + WIDTH_SPACE);
        }
        System.out.print(" ");
        System.out.print(SET_BG_COLOR_DARK_GREY);
    }

    private static void drawRow(ChessPiece[] pieces, String header, int rowNumber, boolean inverted, ChessGame game, boolean highlight, ChessPosition start) {
        int squareNum = rowNumber;
        if (!inverted) {
            squareNum -= 1;
        }
        Collection<ChessPosition> validEndPositions = new ArrayList<>();
        if (highlight) {
            Collection<ChessMove> validMoves = game.validMoves(start);
            for (ChessMove move : validMoves) {
                validEndPositions.add(move.getEndPosition());
            }
        }

        for (int c = 0; c < NUM_SQUARES + 2; c++) {
            if (c == 0 || c == NUM_SQUARES + 1) {
                System.out.print(SET_TEXT_COLOR_BLACK);
                System.out.print(SET_BG_COLOR_LIGHT_GREY);
                System.out.print(header);
            } else {
                if (highlight && Objects.equals(start, new ChessPosition(rowNumber, c))) {
                    System.out.print(SET_BG_COLOR_YELLOW);
                } else if (squareNum % 2 == c % 2) {
                    if (highlight && validEndPositions.contains(new ChessPosition(rowNumber, c))) {
                        System.out.print(SET_BG_COLOR_GREEN);
                    } else {
                        System.out.print(SET_BG_COLOR_WHITE);
                    }
                } else {
                    if (highlight && validEndPositions.contains(new ChessPosition(rowNumber, c))) {
                        System.out.print(SET_BG_COLOR_DARK_GREEN);
                    } else {
                        System.out.print(SET_BG_COLOR_BLACK);
                    }
                }
                System.out.print(pieceToString(pieces[8 - c]));

            }
        }
        System.out.print(SET_BG_COLOR_DARK_GREY);
        //System.out.print(SET_TEXT_COLOR_WHITE);
    }

    private static String pieceToString(ChessPiece piece) {
        if (piece == null) {
            return WIDTH_SPACE + " " + WIDTH_SPACE;
        }
        String str = "";
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            System.out.print(SET_TEXT_COLOR_BLUE);
        } else {
            System.out.print(SET_TEXT_COLOR_RED);
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