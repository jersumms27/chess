package communication;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import ui.Board;

import java.io.IOException;
import java.util.Scanner;

public class GameMenu {
    private WebSocketCommunicator communicator;
    private boolean quit;
    private boolean observer;
    private String playerName;
    private ChessGame.TeamColor playerColor;
    private String auth;
    private Scanner scanner;
    private ChessGame currentGame;
    private int gameID;

    public GameMenu(String url, boolean observer, String auth, String playerName, ChessGame.TeamColor playerColor, int gameID) throws Exception {
        quit = false;
        this.observer = observer;
        this.playerName = playerName;
        this.playerColor = playerColor;
        this.auth = auth;
        this.gameID = gameID;
        currentGame = new ChessGame();

        scanner = new Scanner(System.in);
        communicator = new WebSocketCommunicator(url);

        if (observer) {
            communicator.joinObserver(auth, playerName, gameID);
        } else {
            System.out.println("attempting to join");
            communicator.joinPlayer(auth, playerName, gameID, playerColor);
            System.out.println("finished joining");
        }
        menu();
    }

    public void menu() throws IOException {
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
        Board.drawBoard(currentGame, playerColor.equals(ChessGame.TeamColor.WHITE), false, null);
    }

    public void leave() throws IOException {
        quit = true;
        communicator.leave(auth, playerName, gameID);
    }

    public void makeMove() {
        System.out.println("Enter start position followed by end position (row, then column):");
        String input = scanner.nextLine();
        String[] arguments = input.split(" ");
        String errorString = "";
        try {
            errorString = "Error: invalid input";
            int startRow = Integer.parseInt(arguments[0].substring(0, 1));
            int startCol = Integer.parseInt(arguments[0].substring(1));
            int endRow = Integer.parseInt(arguments[1].substring(0, 1));
            int endCol = Integer.parseInt(arguments[1].substring(1));

            ChessPosition startPos = new ChessPosition(startRow, startCol);
            ChessPosition endPos = new ChessPosition(endRow, endCol);
            ChessMove move = new ChessMove(startPos, endPos, null);

            errorString = "Error: illegal move";
            currentGame.makeMove(move);

            communicator.makeMove(auth, playerName, gameID, move);
        } catch (Exception ex) {
            System.out.println(errorString);
        }
    }

    public void resign() throws IOException {
        communicator.resign(auth, playerName, gameID);
    }

    public void highlightLegalMoves() {
        System.out.println("Enter position of piece (row, then column):");
        String input = scanner.nextLine();
        String[] arguments = input.split(" ");
        try {
            ChessPosition pos = new ChessPosition(Integer.parseInt(arguments[0]), Integer.parseInt(arguments[1]));
            Board.drawBoard(currentGame, playerColor.equals(ChessGame.TeamColor.WHITE), true, pos);
        } catch (Exception ex) {
            System.out.println("Error: invalid input");
        }
    }
}
