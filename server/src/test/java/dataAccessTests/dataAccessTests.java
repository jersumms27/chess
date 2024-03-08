package dataAccessTests;

import dataAccess.*;
import org.junit.jupiter.api.Test;
import service.ClearService;

public class dataAccessTests {
    AuthDAO authDAO = new SQLAuthDAO();
    UserDAO userDAO = new SQLUserDAO();
    GameDAO gameDAO = new SQLGameDAO();

    @Test
    public void gameClear() throws DataAccessException {
        gameDAO.clear();
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

    }

    @Test
    public void getAuthTest() throws DataAccessException {

    }

    @Test
    public void deleteAuthTest() throws DataAccessException {

    }

    @Test
    public void createGameTest() throws DataAccessException {

    }

    @Test
    public void getGameTest() throws DataAccessException {

    }

    @Test
    public void listGamesTest() throws DataAccessException {

    }

    @Test
    public void updateGameTest() throws DataAccessException {

    }

    @Test
    public void verifyColorTest() throws DataAccessException {

    }

    @Test
    public void createUserTest() throws DataAccessException {

    }

    @Test
    public void getUserTest() throws DataAccessException {

    }

    @Test
    public void checkPasswordTest() throws DataAccessException {

    }

}
