package communication;

import chess.ChessBoard;
import com.google.gson.Gson;
import model.GameData;
import response.ListGamesResponse;
import ui.Board;

import java.util.*;

public class Menu {
    private boolean loggedIn;
    private boolean quit;
    private String authToken;
    Scanner scanner;
    ServerFacade serverFacade;

    public Menu() throws Exception {
        loggedIn = false;
        quit = false;
        authToken = "";
        scanner = new Scanner(System.in);
        serverFacade = new ServerFacade();
        System.out.println("Welcome to chess!");
        preloginMenu();
    }

    public static void main(String[] args) throws Exception {
        Menu menu = new Menu();
    }

    public void preloginMenu() throws Exception {
        boolean help = false;
        while (!quit) {
            if (!help) {
                preloginOptions();
            }
            help = false;
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("quit")) {
                quit();
            } else if (input.equalsIgnoreCase("login")) {
                login();
                if (loggedIn) {
                    postloginMenu();
                }
            } else if (input.equalsIgnoreCase("register")) {
                register();
                if (loggedIn) {
                    postloginMenu();
                }
            } else { // "help"
                help = true;
                helpPrelogin();
            }
        }
    }

    private void preloginOptions() {
        System.out.println("HELP\nQUIT\nLOGIN\nREGISTER");
    }

    public void postloginMenu() throws Exception {
        boolean help = false;
        while (loggedIn) {
            if (!help) {
                postloginOptions();
            }
            help = false;
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("logout")) {
                logout();
            } else if (input.equalsIgnoreCase("create")) {
                createGame();
            } else if (input.equalsIgnoreCase("list")) {
                listGames();
            } else if (input.equalsIgnoreCase("join")) {
                joinGame();
            } else if (input.equalsIgnoreCase("observe")) {
                observeGame();
            } else { // "help"
                help = true;
                helpPostlogin();
            }
        }
    }

    private void postloginOptions() {
        System.out.println("HELP\nLOGOUT\nCREATE\nLIST\nJOIN\nOBSERVE");
    }

    private void helpPrelogin() {
        System.out.println("HELP - Displays text informing what actions can be taken");
        System.out.println("QUIT - Exits program");
        System.out.println("LOGIN - Prompts for login information");
        System.out.println("REGISTER - Prompts for registration information");
    }

    private void quit() {
        System.out.println("Goodbye!");
        quit = true;
    }

    private void login() throws Exception {
        System.out.println("Enter username and password:");
        String input = scanner.nextLine();
        String[] arguments = input.split(" ");
        String[] bodyKeys = {"username", "password"};
        Map<String, String> response = new HashMap<>();
        String errorString = "";

        try {
            errorString = "Error: invalid input";
            String[] bodyValues = {arguments[0], arguments[1]};
            errorString = "Error: could not log in";
            response = (Map<String, String>) serverFacade.communicate("session", "POST", bodyKeys, bodyValues, authToken);
            authToken = response.get("authToken");
            loggedIn = true;
        } catch (Exception ex) {
            System.out.println(errorString);
        }
    }

    private void register() throws Exception {
        System.out.println("Enter username, password, email:");
        String input = scanner.nextLine();
        String[] arguments = input.split(" ");
        String[] bodyKeys = {"username", "password", "email"};
        String errorString = "";
        try {
            errorString = "Error: invalid input";
            String[] bodyValues = {arguments[0], arguments[1], arguments[2]};
            errorString = "Error: could not register";
            serverFacade.communicate("user", "POST", bodyKeys, bodyValues, null);
            // Login
            loggedIn = true;
            Map<String, String> response = (Map<String, String>) serverFacade.communicate("session", "POST", Arrays.copyOfRange(bodyKeys, 0, 2), Arrays.copyOfRange(bodyValues, 0, 2), null);
            authToken = response.get("authToken");
        } catch (Exception ex) {
            System.out.println(errorString);
        }

    }

    private void helpPostlogin() {
        System.out.println("HELP - Displays text informing what actions can be taken");
        System.out.println("LOGOUT - Logs out user");
        System.out.println("CREATE - Prompts for name of new game");
        System.out.println("LIST - Lists all currently existing games");
        System.out.println("JOIN - Prompts for which game to join, which color to join as");
        System.out.println("OBSERVE - Prompts for which game to observe");
    }

    private void logout() throws Exception {
        loggedIn = false;
        String[] empty = {};
        serverFacade.communicate("session", "DELETE", empty, empty, authToken);
    }

    private void createGame() throws Exception {
        System.out.println("Enter game name:");
        String name = scanner.nextLine();
        String[] bodyKeys = {"gameName"};
        String[] bodyValues = {name};
        try {
            serverFacade.communicate("game", "POST", bodyKeys, bodyValues, authToken);
        } catch (Exception ex) {
            System.out.println("Error: could not create game");
        }
    }

    private void listGames() throws Exception {
        String[] empty = {};
        String response = (String) serverFacade.communicate("game", "GET", empty, empty, authToken);
        Collection<GameData> games = new Gson().fromJson(response, ListGamesResponse.class).games();
        if (games.isEmpty()) {
            System.out.println("No games");
        } else {
            int index = 1;
            for (GameData game : games) {
                String output = "";
                output += "GAME " + index + ": name: " + game.gameName() + ", ID: " + game.gameID() + ", white: " + game.whiteUsername() + ", black: " + game.blackUsername();
                System.out.println(output);
                index ++;
            }
        }
    }


    private void joinGame() throws Exception {
        System.out.println("Enter color and game ID:");
        String input = scanner.nextLine();
        String[] arguments = input.split(" ");
        String color = arguments[0];
        String[] bodyKeys = {"playerColor", "gameID"};
        String errorString = "";
        try {
            if (!color.equalsIgnoreCase("white") && !color.equalsIgnoreCase("black")) {
                errorString = "Error: invalid color";
                throw new Exception();
            }
            errorString = "Error: invalid input";
            String[] bodyValues = {arguments[0].toUpperCase(), arguments[1]};
            errorString = "Error: could not join game";
            serverFacade.communicate("game", "PUT", bodyKeys, bodyValues, authToken);

            ChessBoard board = new ChessBoard();
            board.resetBoard();
            if (color.equalsIgnoreCase("black")) {
                Board.drawBoard(board, true);
            } else {
                Board.drawBoard(board, false);
            }
        } catch (Exception ex) {
            System.out.println(errorString);
        }
    }

    private void observeGame() throws Exception {
        System.out.println("Enter game ID:");
        String id = scanner.nextLine();
        String[] bodyKeys = {"playerColor", "gameID"};
        String[] bodyValues = {null, id};
        try {
            serverFacade.communicate("game", "PUT", bodyKeys, bodyValues, authToken);
            ChessBoard board = new ChessBoard();
            board.resetBoard();
            Board.drawBoard(board, true);
            System.out.println();
            Board.drawBoard(board, false);
        } catch (Exception ex) {
            System.out.println("Error: could not observe game");
        }
    }
}
