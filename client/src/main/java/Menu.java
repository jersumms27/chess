import chess.ChessBoard;
import ui.Board;

import java.io.PrintStream;
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
        while (!quit) {
            //System.out.println("HELP\nQUIT\nLOGIN\nREGISTER");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("quit")) {
                quit();
            } else if (input.equalsIgnoreCase("login")) {
                login();
            } else if (input.equalsIgnoreCase("register")) {
                register();
            } else { // "help"
                helpPrelogin();
            }
        }
    }

    public void postloginMenu() throws Exception {
        while (loggedIn) {
            //System.out.println("HELP\nLOGOUT\nCREATE\nLIST\nJOIN\nOBSERVE");
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
                helpPostlogin();
            }
        }

    }

    private void helpPrelogin() {
        System.out.println("HELP - Displays text informing what actions can be taken");
        System.out.println("QUIT - Exits program");
        System.out.println("LOGIN - Prompts for login information");
        System.out.println("REGISTER - Prompts for registration information");
    }

    private void quit() {
        quit = true;
    }

    private void login() throws Exception {
        System.out.println("Enter username and password:");
        String input = scanner.nextLine();
        String[] arguments = input.split(" ");
        String[] bodyKeys = {"username", "password"};
        Map<String, String> response = new HashMap<>();

        try {
            String[] bodyValues = {arguments[0], arguments[1]};
            response = serverFacade.communicate("session", "POST", bodyKeys, bodyValues, authToken);
            loggedIn = true;
        } catch (Exception ex) {
            System.out.println("Error: could not log in");
        }
        authToken = response.get("authToken");
        postloginMenu();
    }

    private void register() throws Exception {
        System.out.println("Enter username, password, email:");
        String input = scanner.nextLine();
        String[] arguments = input.split(" ");
        String[] bodyKeys = {"username", "password", "email"};
        try {
            String[] bodyValues = {arguments[0], arguments[1], arguments[2]};
            serverFacade.communicate("user", "POST", bodyKeys, bodyValues, null);
            // Login
            loggedIn = true;
            Map<String, String> response = serverFacade.communicate("session", "POST", Arrays.copyOfRange(bodyKeys, 0, 2), Arrays.copyOfRange(bodyValues, 0, 2), null);
            authToken = response.get("authToken");
            postloginMenu();
        } catch (Exception ex) {
            System.out.println("Error: could not register");
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
        Map<String, String> response = serverFacade.communicate("game", "GET", empty, empty, authToken);
        int index = 0;
        for (Map.Entry<String, String> entry: response.entrySet()) {
            if (index > 0) {
                break;
            }
            System.out.println(parseGame(entry.toString()));
            index ++;
        }
    }

    private String parseGame(String entry) {
        int startIndex = 0;
        int endIndex = "gameID".length();
        String output = "";
        ArrayList<String> ids = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        String keyWord = "gameID";
        while (endIndex < entry.length()) {
            if (entry.substring(startIndex, endIndex).equals(keyWord)) {
                if (keyWord.equals("gameID")) {
                    ids.add(getGameValue(entry.substring(startIndex)));
                    keyWord = "gameName";
                } else {
                    names.add(getGameValue(entry.substring(startIndex)));
                    keyWord = "gameID";
                }
                startIndex = endIndex + 1;
                endIndex = startIndex + keyWord.length();
            } else {
                startIndex++;
                endIndex++;
            }
        }

        for (int i = 0; i < names.size(); i++) {
            output += "name: " + names.get(i) + ", ID: " + ids.get(i) + "\n";
        }

        return output;
    }

    private String getGameValue(String entry) {
        String output = "";
        int startIndex = 0;
        while (entry.charAt(startIndex) != '=') {
            startIndex ++;
        }
        startIndex ++;
        int endIndex = startIndex + 1;
        while (entry.charAt(endIndex) != ',' && entry.charAt(endIndex) != '}') {
            endIndex ++;
        }
        return entry.substring(startIndex, endIndex);
    }

    private void joinGame() throws Exception {
        System.out.println("Enter color and game ID:");
        String input = scanner.nextLine();
        String[] arguments = input.split(" ");
        String color = arguments[0];
        String[] bodyKeys = {"playerColor", "gameID"};
        try {
            if (!color.equalsIgnoreCase("white") && !color.equalsIgnoreCase("black")) {
                throw new Exception();
            }
            String[] bodyValues = {arguments[0].toUpperCase(), arguments[1]};
            serverFacade.communicate("game", "PUT", bodyKeys, bodyValues, authToken);

            ChessBoard board = new ChessBoard();
            board.resetBoard();
            if (color.equalsIgnoreCase("white")) {
                Board.drawBoard(board, true);
            } else {
                Board.drawBoard(board, false);
            }
        } catch (Exception ex) {
            System.out.println("Error: could not join game");
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
            Board.drawBoard(board, false);
        } catch (Exception ex) {
            System.out.println("Error: could not observe game");
        }
    }
}
