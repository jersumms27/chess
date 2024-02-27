package serviceTests;

import dataAccess.*;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.*;
import service.request.LoginRequest;
import service.response.LoginResponse;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    AuthDAO authDAO = new MemoryAuthDAO();
    UserDAO userDAO = new MemoryUserDAO();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        authDAO.clear();
        userDAO.clear();
    }

    @Test
    void login() throws DataAccessException {
        LoginRequest request = new LoginRequest("LilTreat", "12345");
        UserService userService = new UserService(authDAO, userDAO);

        assertEquals(new LoginResponse(null, null, "Error: unauthorized"), userService.login(request));

        userDAO.createUser(new UserData("LilTreat", "12345", "liltreat@gmail.com"));

        authDAO.createAuth("LilTreat");
        //assertEquals(new LoginResponse("LilTreat", authDAO.getAuth("LilTreat"), null), userService.login(request));
    }
}