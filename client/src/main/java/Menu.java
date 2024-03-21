import java.util.Scanner;

public class Menu {
    private boolean loggedIn;
    private boolean quit;
    Scanner scanner;
    ServerFacade serverFacade;

    public Menu() throws Exception {
        loggedIn = false;
        quit = false;
        scanner = new Scanner(System.in);
        serverFacade = new ServerFacade();

        System.out.println("Welcome to chess");
        helpPrelogin();
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

    public void postloginMenu() {
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

        serverFacade.performAction("http://localhost:8080/session", "POST", "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\" }");
        postloginMenu();
    }

    private void register() throws Exception {
        System.out.println("Enter username, password, email:");
        String input = scanner.nextLine();
        String[] arguments = input.split(" ");
        String username = arguments[0];
        String password = arguments[1];
        String email = arguments[2];

        serverFacade.performAction("http://localhost:8080/user", "POST", "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\", \"email\": " + email + "\" }");
    }

    private void helpPostlogin() {
        System.out.println("HELP - Displays text informing what actions can be taken");
        System.out.println("LOGOUT - Logs out user");
        System.out.println("CREATE - Prompts for name of new game");
        System.out.println("LIST - Lists all currently existing games");
        System.out.println("JOIN - Prompts for which game to join, which color to join as");
        System.out.println("OBSERVE - Prompts for which game to observe");
    }

    private void logout() {
        loggedIn = false;
    }

    private void createGame() {

    }

    private void listGames() {

    }

    private void joinGame() {

    }

    private void observeGame() {

    }
}
