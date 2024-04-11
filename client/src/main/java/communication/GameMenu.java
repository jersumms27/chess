package communication;

import chess.ChessGame;
import chess.ChessPosition;
import ui.Board;

import java.util.Scanner;

public class GameMenu {
    private WebSocketCommunicator communicator;
    private boolean quit;
    private boolean observer;
    private String playerName;
    private String playerColor;
    private Scanner scanner;
    private ChessGame game;

    public GameMenu(boolean observer, String playerName, String playerColor) {
        quit = false;
        this.observer = observer;
        this.playerName = playerName;
        this.playerColor = playerColor;
        scanner = new Scanner(System.in);

        if (observer) {

        } else {

        }
        menu();
    }

    public void menu() {
        boolean help = false;
        while (!quit) {
            if (!help) {
                menuOptions();
            }
            help = false;
            String input = scanner.nextLine();
            input = input.toLowerCase();

            if (input.contains("redraw") || input.contains("chess") || input.contains("board")) {
                redrawChessBoard();
            } else if (input.equals("leave")) {
                leave();
            } else if (input.contains("make") || input.contains("move")) {
                makeMove();
            } else if (input.equals("resign")) {
                resign();
            } else if (input.contains("highlight") || input.contains("legal") || input.contains("moves")) {
                highlightLegalMoves();
            } else { //"help"
                help = true;
                help();
            }
        }
    }

    private void menuOptions() {
        System.out.println("HELP\nREDRAW CHESS BOARD\nLEAVE\nMAKE MOVE\nRESIGN\nHIGHLIGHT LEGAL MOVES");
    }
    public void help() {
        System.out.println("HELP - displays information about options");
        System.out.println("REDRAW CHESS BOARD - redraws chess board");
        System.out.println("LEAVE - removes you from the game, takes you back to previous menu");
        System.out.println("MAKE MOVE - allows you to enter which move to make");
        System.out.println("RESIGN - you forfeit the game, causing the game to be over");
        System.out.println("HIGHLIGHT LEGAL MOVES - enter a piece to see its possible moves");
    }

    public void redrawChessBoard() {
        Board.drawBoard(game, playerColor.equals("white"), false, null);
    }

    public void leave() {
        quit = true;
        // TODO: websocket
    }

    public void makeMove() {
        //TODO: websocket
    }

    public void resign() {
        //TODO: websocket
    }

    public void highlightLegalMoves() {
        System.out.println("Enter position of piece");
        String input = scanner.nextLine();
        String[] arguments = input.split(" ");
        try {
            ChessPosition pos = new ChessPosition(Integer.parseInt(arguments[0]), Integer.parseInt(arguments[1]));
            Board.drawBoard(game, playerColor.equals("white"), true, pos);
        } catch (Exception ex) {
            System.out.println("Invalid input");
        }
    }
}
