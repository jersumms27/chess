package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.Test;
import response.CreateGameResponse;
import response.JoinGameResponse;
import response.ListGamesResponse;
import response.RegisterResponse;
import service.*;
import service.request.*;
//import service.response.*;

import static org.junit.jupiter.api.Assertions.*;
class GameServiceTest {
    AuthDAO authDAO = new MemoryAuthDAO();
    UserDAO userDAO = new MemoryUserDAO();
    GameDAO gameDAO = new MemoryGameDAO();
    GameService gameService = new GameService(authDAO, userDAO, gameDAO);
    UserService userService = new UserService(authDAO, userDAO);

    @Test
    void listGamesSuccess() {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        RegisterResponse registerResponse = userService.register(registerRequest);
        ListGamesResponse actual = gameService.listGames(registerResponse.authToken());
        //ListGamesResponse expected = new ListGamesResponse(gameDAO.listGames(), "");
        //assertEquals(expected, actual);
    }

    @Test
    void listGamesFail() {
        ListGamesResponse actual = gameService.listGames("fake");
        ListGamesResponse expected = new ListGamesResponse(null, "Error: unauthorized");
        assertEquals(expected, actual);
    }

    @Test
    void createGameSuccess() {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        RegisterResponse registerResponse = userService.register(registerRequest);
        CreateGameRequest createGameRequest = new CreateGameRequest("name");
        CreateGameResponse actual = gameService.createGame(registerResponse.authToken(), createGameRequest);
        CreateGameResponse expected = new CreateGameResponse(actual.gameID(), "");
        assertEquals(expected, actual);
    }

    @Test
    void createGameFailure() {

    }

    @Test
    void joinGameSuccess() {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        RegisterResponse registerResponse = userService.register(registerRequest);
        CreateGameRequest createGameRequest = new CreateGameRequest("name");
        CreateGameResponse createGameResponse = gameService.createGame(registerResponse.authToken(), createGameRequest);
        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", createGameResponse.gameID());
        JoinGameResponse actual = gameService.joinGame(registerResponse.authToken(), joinGameRequest);
        //JoinGameResponse expected = new JoinGameResponse("");
    }

    @Test
    void joinGameFailure() {

    }
}