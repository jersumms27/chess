package dataAccessTests;

import dataAccess.*;
import model.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class dataAccessTests {
    static AuthDAO authDAO = new SQLAuthDAO();
    static UserDAO userDAO = new SQLUserDAO();
    static GameDAO gameDAO = new SQLGameDAO();

    @BeforeAll
    static void startUp() {
        try {
            authDAO.clear();
            userDAO.clear();
            gameDAO.clear();
        } catch (DataAccessException ex) {
        }

    }

    @Test
    public void gameClear() throws DataAccessException {
        gameDAO.clear();
        assertEquals(0, gameDAO.listGames().size());
    }

    @Test
    public void authClear() throws DataAccessException {
        authDAO.clear();
    }

    @Test
    public void userClear() throws DataAccessException {
        userDAO.clear();
    }

    @Test
    public void createAuthTest() throws DataAccessException {
        String username = "user";
        String authToken = authDAO.createAuth(username);
        AuthData authData = authDAO.getAuth(authToken);
        assertEquals(authToken, authData.authToken());
    }

    @Test
    public void getAuthTest() throws DataAccessException {
        String username = "user";
        String authToken = authDAO.createAuth(username);
        AuthData authData = authDAO.getAuth(authToken);
        assertEquals(authToken, authData.authToken());
    }

    @Test
    public void deleteAuthTest() throws DataAccessException {
        String username = "user";
        String authToken = authDAO.createAuth(username);
        authDAO.deleteAuth(authToken);
        assertThrows(DataAccessException.class, () -> authDAO.getAuth(authToken));
    }

    @Test
    public void createGameTest() throws DataAccessException {
        GameData gameData = gameDAO.createGame("game");
        assertNotNull(gameDAO.getGame(gameData.gameID()));
    }

    @Test
    public void getGameTest() throws DataAccessException {
        GameData gameData = gameDAO.createGame("game");
        assertNotNull(gameDAO.getGame(gameData.gameID()));
    }

    @Test
    public void listGamesTest() throws DataAccessException {
        //GameData gameData1 = gameDAO.createGame("game1");
        //GameData gameData2 = gameDAO.createGame("game2");
        //assertEquals(2, gameDAO.listGames().size());
    }

    @Test
    public void updateGameTest() throws DataAccessException {
    }

    @Test
    public void verifyColorTest() throws DataAccessException {
        GameData gameData = gameDAO.createGame("game");
    }

    @Test
    public void createUserTest() throws DataAccessException {
        UserData userData = new UserData("user", "password", "email");
        userDAO.createUser(userData);
        UserData retrievedUserData = userDAO.getUser(userData.username());
        assertEquals(userData.username(), retrievedUserData.username());
        assertEquals(userData.email(), retrievedUserData.email());
    }

    @Test
    public void getUserTest() throws DataAccessException {
    }

    @Test
    public void checkPasswordTest() throws DataAccessException {
    }
}
