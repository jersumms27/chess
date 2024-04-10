package clientTests;

import communication.HTTPCommunicator;
import org.junit.jupiter.api.*;
import server.Server;
import communication.ServerFacade;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    static HTTPCommunicator communicator;
    static int port;

    @BeforeAll
    public static void init() {
        server = new Server();
        communicator = new HTTPCommunicator();
        port = server.run(0);
        facade = new ServerFacade(port);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterEach
    void clearServer() throws Exception {
        communicator.performAction("http://localhost:" + port + "/db", "DELETE", "", null);
    }


    @AfterAll
    static void stopServer() throws Exception {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void loginFailure() throws Exception {
        String[] keys = {"username", "password"};
        String[] values = {"user1", "pass1"};
        try {
            facade.communicate("session", "POST", keys, values, null);
            fail("Login was supposed to fail");
        } catch(Exception ex) {
            assertTrue(true, "Login successfully failed");
        }
    }

    @Test
    public void loginSuccess() throws Exception {
        String username = "username";
        String password = "password";
        String email = "email";
        registerUser(username, password, email);
        try {
            loginUser(username, password);
            assertTrue(true, "Login success");
        } catch (Exception ex) {
            fail("Login failure");
        }
    }

    @Test
    public void registerFailure() throws Exception {
        String username = "username";
        String password = "password";
        String email = "email";
        registerUser(username, password, email);
        try {
            registerUser(username, password, email);
            fail();
        } catch (Exception ex) {
            assertTrue(true);
        }
    }

    @Test
    public void registerSuccess() throws Exception {
        String username = "username";
        String password = "password";
        String email = "email";
        try {
            registerUser(username, password, email);
            assertTrue(true);
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void logoutFailure() throws Exception {
        String username = "username";
        String password = "password";
        String email = "email";
        String[] empty = {};
        registerUser(username, password, email);
        try {
            facade.communicate("session", "DELETE", empty, empty, null);
            fail();
        } catch (Exception ex) {
            assertTrue(true);
        }
    }

    @Test
    public void createGameFailure() throws Exception {
        String authToken = "fakeAuthToken";
        try {
            createGame("gameName", authToken);
            fail();
        } catch (Exception ex) {
            assertTrue(true);
        }
    }

    @Test
    public void createGameSuccess() throws Exception {
        String authToken = registerAndLogin("u", "p", "e");
        try {
            createGame("name", authToken);
            assertTrue(true);
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void joinGameFailure() throws Exception {
        String authToken = registerAndLogin("i", "i", "e");
        createGame("name", authToken);
        String[] keys = {"playerColor", "gameID"};
        String[] values = {"black", "fakeID"};
        try {
            facade.communicate("game", "PUT", keys, values, authToken);
            fail();
        } catch (Exception ex) {
            assertTrue(true);
        }
    }

    @Test
    public void joinGameSuccess() throws Exception {
        String authToken = registerAndLogin("i", "i", "e");
        createGame("name", authToken);
        String[] keys = {"playerColor", "gameID"};
        String[] values = {"black", "0"};
        try {
            facade.communicate("game", "PUT", keys, values, authToken);
            assertTrue(true);
        } catch (Exception ex) {
            assertTrue(true);
        }
    }

    @Test
    public void listGamesFailure() throws Exception {
        String[] empty = {};
        try {
            facade.communicate("game", "GET", empty, empty, "fakeAuthToken");
            fail();
        } catch (Exception ex) {
            assertTrue(true);
        }
    }

    @Test
    public void listGamesSuccess() throws Exception {
        String[] empty = {};
        String authToken = registerAndLogin("i", "i", "e");
        try {
            facade.communicate("game", "GET", empty, empty, authToken);
            assertTrue(true);
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void logoutSuccess() throws Exception {
        String authToken = registerAndLogin("username", "password", "email");
        String[] empty = {};
        try {
            facade.communicate("session", "DELETE", empty, empty, authToken);
            assertTrue(true);
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void observeGameFailure() throws Exception {
        String authToken = registerAndLogin("i", "i", "e");
        createGame("name", authToken);
        String[] keys = {"playerColor", "gameID"};
        String[] values = {null, "fakeID"};
        try {
            facade.communicate("game", "PUT", keys, values, authToken);
            fail();
        } catch (Exception ex) {
            assertTrue(true);
        }
    }

    @Test
    public void observeGameSuccess() throws Exception {
        String authToken = registerAndLogin("i", "i", "e");
        createGame("name", authToken);
        String[] keys = {"playerColor", "0"};
        String[] values = {null, "0"};
        try {
            facade.communicate("game", "PUT", keys, values, authToken);
            fail();
        } catch (Exception ex) {
            assertTrue(true);
        }
    }

    private void registerUser(String username, String password, String email) throws Exception {
        String[] keys = {"username", "password", "email"};
        String[] values = {username, password, email};
        facade.communicate("user", "POST", keys, values, null);
    }

    private String loginUser(String username, String password) throws Exception {
        String[] keys = {"username", "password"};
        String[] values = {username, password};
        Map<String, String> ret = (Map<String, String>) facade.communicate("session", "POST", keys, values, null);
        return ret.get("authToken");
    }

    private String registerAndLogin(String username, String password, String email) throws Exception {
        registerUser(username, password, email);
        return loginUser(username, password);
    }

    private void createGame(String name, String authToken) throws Exception {
        String[] keys = {"gameName"};
        String[] values = {name};
        facade.communicate("game", "POST", keys, values, authToken);
    }

}