package passoffTests.serverTests;

import dataAccess.*;
import org.junit.jupiter.api.Test;
import service.ClearService;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {
    private static UserDAO userDAO = new MemoryUserDAO();
    private static AuthDAO authDAO = new MemoryAuthDAO();
    private static GameDAO gameDAO = new MemoryGameDAO();

    @Test
    public void clear() {
        ClearService clearService = new ClearService(authDAO, userDAO, gameDAO);
        clearService.clear();
    }

}