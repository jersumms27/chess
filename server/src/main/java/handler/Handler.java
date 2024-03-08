package handler;

import com.google.gson.*;
import dataAccess.*;
import service.*;

public interface Handler {
    AuthDAO authDAO = new SQLAuthDAO();

    UserDAO userDAO = new SQLUserDAO();
    GameDAO gameDAO = new MemoryGameDAO();

    ClearService clearService = new ClearService(authDAO, userDAO, gameDAO);
    UserService userService = new UserService(authDAO, userDAO);
    GameService gameService = new GameService(authDAO, userDAO, gameDAO);

    Gson serializer = new Gson();
}
