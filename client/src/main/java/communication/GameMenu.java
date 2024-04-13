package communication;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
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
    public static ChessGame currentGame;
    private int gameID;

    public GameMenu(String url, boolean observer, String auth, String playerName, ChessGame.TeamColor playerColor, int gameID, ChessGame game) throws Exception {
        quit = false;
        this.observer = observer;
        this.playerName = playerName;
        this.playerColor = playerColor;
        this.auth = auth;
        this.gameID = gameID;
        currentGame = game;

        scanner = new Scanner(System.in);
        communicator = new WebSocketCommunicator(url, this);

        if (observer) {
            communicator.joinObserver(auth, gameID);
        } else {
            communicator.joinPlayer(auth, gameID, playerColor);
        }
        menu();
    }

    public void menu() throws IOException, InterruptedException {
        boolean help = false;
        while (!quit) {
            if (!help) {
                menuOptions();
            }
            help = false;
            String input = scanner.nextLine();
            input = input.toLowerCase();

            if (input.contains("draw") || input.contains("chess") || input.contains("board")) {
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
        System.out.println("MAKE MOVE - allows you to enter which move to make\n(ex. a7 a8 q)");
        System.out.println("RESIGN - you forfeit the game, causing the game to be over");
        System.out.println("HIGHLIGHT LEGAL MOVES - enter a piece to see its possible moves\n(ex. b2)");
    }

    public void redrawChessBoard() {
        Board.drawBoard(currentGame, playerColor.equals(ChessGame.TeamColor.BLACK), false, null);
    }

    public void leave() throws IOException {
        quit = true;
        communicator.leave(auth, gameID);
    }

    public void makeMove() {
        System.out.println("Enter start position followed by end position, followed by promotion piece if applicable:");
        String input = scanner.nextLine();
        String[] arguments = input.split(" ");
        String errorString = "";
        try {
            errorString = "Error: invalid input";
            int startCol = columnConversion(arguments[0].substring(0, 1));
            int startRow = Integer.parseInt(arguments[0].substring(1));
            int endCol = columnConversion(arguments[1].substring(0, 1));
            int endRow = Integer.parseInt(arguments[1].substring(1));
            ChessPiece.PieceType promPiece = null;
            if (arguments.length == 3) {
                promPiece = pieceConversion(arguments[2]);
            }

            ChessPosition startPos = new ChessPosition(startRow, startCol);
            ChessPosition endPos = new ChessPosition(endRow, endCol);
            ChessMove move = new ChessMove(startPos, endPos, promPiece);

            errorString = "Error: illegal move";
            currentGame.makeMove(move, playerColor);
            communicator.makeMove(auth, gameID, move, input, playerColor);
        } catch (Exception ex) {
            System.out.println(errorString);
        }
    }

    public void resign() throws IOException, InterruptedException {
        communicator.resign(auth, gameID);
    }

    public void highlightLegalMoves() {
        System.out.println("Enter position of piece:");
        String input = scanner.nextLine();
        try {
            int row = Integer.parseInt(input.substring(1));
            int col = columnConversion(input.substring(0, 1));

            ChessPosition pos = new ChessPosition(row, col);
            Board.drawBoard(currentGame, playerColor.equals(ChessGame.TeamColor.BLACK), true, pos);
        } catch (Exception ex) {
            System.out.println("Error: invalid input");
        }
    }

    private int columnConversion(String letter) {
        int num = 0;
        switch (letter) {
            case "a":
                num = 1;
                break;
            case "b":
                num =  2;
                break;
            case "c":
                num =  3;
                break;
            case "d":
                num =  4;
                break;
            case "e":
                num =  5;
                break;
            case"f":
                num =  6;
                break;
            case "g":
                num =  7;
                break;
            case"h":
                num =  8;
                break;
        }

        if (playerColor.equals(ChessGame.TeamColor.WHITE)) {
            return num;
        }
        return 9 - num;
    }

    private ChessPiece.PieceType pieceConversion(String p) {
        ChessPiece.PieceType type = null;
        switch (p) {
            case "q":
                type = ChessPiece.PieceType.QUEEN;
                break;
            case "r":
                type = ChessPiece.PieceType.ROOK;
                break;
            case "k", "n":
                type = ChessPiece.PieceType.KNIGHT;
                break;
            case "b":
                type = ChessPiece.PieceType.BISHOP;
                break;
        }

        return type;
    }
}
