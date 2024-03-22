package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import response.LoginResponse;
import response.LogoutResponse;
import response.RegisterResponse;
import service.*;
import service.request.*;
//import service.response.*;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    AuthDAO authDAO = new MemoryAuthDAO();
    UserDAO userDAO = new MemoryUserDAO();
    UserService userService = new UserService(authDAO, userDAO);

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        //authDAO.clear();
        //userDAO.clear();
    }

    @Test
    void loginSuccess() {
        RegisterRequest register = new RegisterRequest("username", "password", "email");
        userService.register(register);
        LoginRequest valid = new LoginRequest("username", "password");
        LoginResponse actual = userService.login(valid);
        LoginResponse expected = new LoginResponse("username", actual.authToken(), "");
        assertEquals(expected, actual);
    }

    @Test
    void loginFail() {
        LoginRequest invalid = new LoginRequest("username", "password");
        LoginResponse actual = userService.login(invalid);
        LoginResponse expected = new LoginResponse(null, null, "Error: unauthorized");
        assertEquals(expected, actual);
    }

    @Test
    void logoutSuccess() {
        RegisterRequest register = new RegisterRequest("username", "password", "email");
        userService.register(register);
        LoginRequest login = new LoginRequest("username", "password");
        LoginResponse response = userService.login(login);
        LogoutResponse actual = userService.logout(response.authToken());
        LogoutResponse expected = new LogoutResponse("");
        assertEquals(expected, actual);
    }

    @Test
    void logoutFail() {
        LogoutResponse expected = new LogoutResponse("Error: unauthorized");
        LogoutResponse actual = userService.logout("fake");
        assertEquals(expected, actual);
    }

    @Test
    void registerSuccess() {
        RegisterRequest request = new RegisterRequest("username", "password", "email");
        RegisterResponse actual = userService.register(request);
        RegisterResponse expected = new RegisterResponse("username", actual.authToken(), "");
        assertEquals(expected, actual);
    }

    @Test
    void registerFail() {
        RegisterRequest request = new RegisterRequest("username", "password", "");
        userService.register(request);
        RegisterResponse actual = userService.register(request);
        RegisterResponse expected = new RegisterResponse(null, null, "Error: already taken");
        assertEquals(expected, actual);
    }
}