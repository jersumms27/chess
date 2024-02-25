package serviceTests;

import dataAccess.*;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.*;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @BeforeEach
    void setUp() {
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
    }

    @Test
    void login() throws DataAccessException {
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        LoginRequest request = new LoginRequest("LilTreat", "12345");
        UserService userService = new UserService(authDAO, userDAO);

        assertEquals(new LoginResponse(null, null, "Error: unauthorized"), userService.login(request));

        userDAO.createUser(new UserData("LilTreat", "12345", "liltreat@gmail.com"));

        try {
            authDAO.createAuth("LilTreat");
        } catch (DataAccessException ex) {
            throw new DataAccessException("sus");
        }
        assertEquals(new LoginResponse("LilTreat", authDAO.getAuth("LilTreat"), null), userService.login(request));
    }
}