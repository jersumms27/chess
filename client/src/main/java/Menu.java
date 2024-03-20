import java.util.Scanner;

public class Menu {
    private boolean loggedIn;
    private boolean quit;
    Scanner scanner;

    public Menu() {
        loggedIn = false;
        quit = false;
        scanner = new Scanner(System.in);

        System.out.println("Welcome to chess");
    }

    public void preloginMenu() {
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

    private void login() {

    }

    private void register() {

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
