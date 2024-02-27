package server;

import service.*;
import spark.*;
import handler.*;
import com.google.gson.*;

public class Server {
    ClearHandler clearHandler = new ClearHandler();
    UserHandler userHandler = new UserHandler();
    GameHandler gameHandler = new GameHandler();


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear); // clear application
        Spark.post("/user", this::register); // register
        Spark.post("/session", this::login); // login
        Spark.delete("/session", this::logout); // logout
        Spark.get("/game", this::listGames); // list games
        Spark.post("/game", this::createGame); // create game
        Spark.put("/game", this::joinGame); // join game

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object clear(Request request, Response response) {
        response.status(200);
        return clearHandler.clear();
    }

    private Object register(Request request, Response response) {
        String registerResponse = userHandler.register(request.body());
        String message = (new Gson()).fromJson(registerResponse, RegisterResponse.class).message();
        if (message == null) {
            response.status(200);
            return JsonParser.parseString(registerResponse).getAsJsonObject().remove("message").toString();
        } else {
            response.status(getErrorCode(message));
            return createJsonMessage(message);
        }
    }

    private Object login(Request request, Response response) {
        String loginResponse = userHandler.register(request.body());
        String message = (new Gson()).fromJson(loginResponse, RegisterResponse.class).message();
        if (message == null) {
            response.status(200);
            return JsonParser.parseString(loginResponse).getAsJsonObject().remove("message").toString();
        } else {
            response.status(getErrorCode(message));
            return createJsonMessage(message);
        }
    }

    private Object logout(Request request, Response response) {
        String logoutResponse = userHandler.register(request.body());
        String message = (new Gson()).fromJson(logoutResponse, RegisterResponse.class).message();
        if (message == null) {
            response.status(200);
            return JsonParser.parseString(logoutResponse).getAsJsonObject().remove("message").toString();
        } else {
            response.status(getErrorCode(message));
            return createJsonMessage(message);
        }
    }

    private Object listGames(Request request, Response response) {
        String listGamesResponse = userHandler.register(request.body());
        String message = (new Gson()).fromJson(listGamesResponse, RegisterResponse.class).message();
        if (message == null) {
            response.status(200);
            return JsonParser.parseString(listGamesResponse).getAsJsonObject().remove("message").toString();
        } else {
            response.status(getErrorCode(message));
            return createJsonMessage(message);
        }
    }

    private Object createGame(Request request, Response response) {
        String createGameResponse = userHandler.register(request.body());
        String message = (new Gson()).fromJson(createGameResponse, RegisterResponse.class).message();
        if (message == null) {
            response.status(200);
            return JsonParser.parseString(createGameResponse).getAsJsonObject().remove("message").toString();
        } else {
            response.status(getErrorCode(message));
            return createJsonMessage(message);
        }
    }

    private Object joinGame(Request request, Response response) {
        String joinGameResponse = userHandler.register(request.body());
        String message = (new Gson()).fromJson(joinGameResponse, RegisterResponse.class).message();
        if (message == null) {
            response.status(200);
            return JsonParser.parseString(joinGameResponse).getAsJsonObject().remove("message").toString();
        } else {
            response.status(getErrorCode(message));
            return createJsonMessage(message);
        }
    }

    private String createJsonMessage(String message) {
        return "{ \"message\": \"" + message + "\" }";
    }

    private int getErrorCode(String message) {
        if (message.equals("Error: bad request")) {
            return 400;
        } else if (message.equals("Error: already taken")) {
            return 403;
        } else if (message.equals("Error: unauthorized")) {
            return 401;
        } else if (message.equals("Error: description")) {
            return 500;
        }
        return 0;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
