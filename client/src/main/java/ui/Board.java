package ui;

import chess.ChessBoard;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;

public class Board {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 3;


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
    }

    public void drawBoard(PrintStream out, ChessBoard board, boolean inverted) {
        String[] regularRowHeader = {"a", "b", "c", "d", "e", "f", "g", "h"};
        String[] invertedRowHeader = {"h", "g", "f", "e", "d", "c", "b", "a"};
        String[] regularColHeader = {"1", "2", "3", "4", "5", "6", "7", "8"};
        String[] invertedColHeader = {"8", "7", "6", "5", "4", "3", "2", "1"};

        String[] rowHeader;
        String[] colHeader;
        if (inverted) {
            rowHeader = invertedRowHeader;
            colHeader = invertedColHeader;
        } else {
            rowHeader = regularRowHeader;
            colHeader = regularColHeader;
        }
    }
}