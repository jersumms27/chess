import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Menu {
    private boolean loggedIn;
    private boolean quit;
    private String authToken;
    private Map<String, String> authHeader;
    Scanner scanner;
    ServerFacade serverFacade;

    public Menu() throws Exception {
        loggedIn = false;
        quit = false;
        authToken = "";
        authHeader = new HashMap<>();
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
        loggedIn = true;
        System.out.println("Enter username and password:");
        String input = scanner.nextLine();
        String[] arguments = input.split(" ");
        String username = arguments[0];
        String password = arguments[1];

        Map<String, String> response = serverFacade.performAction("http://localhost:8080/session", "POST", "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\" }", null);
        authToken = response.get("authToken");
        authHeader.put("authorization", authToken);
        postloginMenu();
    }

    private void register() throws Exception {
        System.out.println("Enter username, password, email:");
        String input = scanner.nextLine();
        String[] arguments = input.split(" ");
        String username = arguments[0];
        String password = arguments[1];
        String email = arguments[2];

        serverFacade.performAction("http://localhost:8080/user", "POST", "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\", \"email\": " + email + "\" }", null);
        // Login
        Map<String, String> response = serverFacade.performAction("http://localhost:8080/session", "POST", "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\" }", null);
        authToken = response.get("authToken");
        authHeader.put("authorization", authToken);
        postloginMenu();

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
        serverFacade.performAction("http://localhost:8080/session", "DELETE", "{}", authHeader);
    }

    private void createGame() throws Exception {
        System.out.println("Enter game name:");
        String name = scanner.nextLine();
        serverFacade.performAction("http://localhost:8080/game", "POST", "{ \"gameName\": \"" + name + "\" }", authHeader);
    }

    private void listGames() throws Exception {
        serverFacade.performAction("http://localhost:8080/game", "GET", "{}", authHeader);
    }

    private void joinGame() throws Exception {
        System.out.println("Enter color and game ID:");
        String input = scanner.nextLine();
        String[] arguments = input.split(" ");
        String color = arguments[0];
        String id = arguments[1];

        serverFacade.performAction("http://localhost:8080/game", "PUT", "{ \"playerColor\": \"" + color + "\", \"gameID\": \"" + id + "\" }", authHeader);
    }

    private void observeGame() throws Exception {
        System.out.println("Enter game ID:");
        String id = scanner.nextLine();

        serverFacade.performAction("http://localhost:8080/game", "PUT", "{ \"playerColor\": \"" + "\", \"gameID\": \"" + id + "\" }", authHeader);
    }
}
