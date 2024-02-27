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
        clearHandler.clear();
        return "{}";
    }

    private Object register(Request request, Response response) {
        String registerResponse = userHandler.register(request.body());
        String message = (new Gson()).fromJson(registerResponse, RegisterResponse.class).message();
        if (message.isEmpty()) {
            response.status(200);
            return JsonParser.parseString(registerResponse).getAsJsonObject();
        } else {
            response.status(getErrorCode(message));
            System.out.println("register");
            return createJsonMessage(message);
        }
    }

    private Object login(Request request, Response response) {
        String loginResponse = userHandler.login(request.body());
        String message = (new Gson()).fromJson(loginResponse, LoginResponse.class).message();
        if (message.isEmpty()) {
            response.status(200);
            return JsonParser.parseString(loginResponse).getAsJsonObject().toString();
        } else {
            response.status(getErrorCode(message));
            System.out.println("login");
            return createJsonMessage(message);
        }
    }

    private Object logout(Request request, Response response) {
        String logoutResponse = userHandler.logout(request.headers("authorization"));
        String message = (new Gson()).fromJson(logoutResponse, LogoutResponse.class).message();
        if (message.isEmpty()) {
            response.status(200);
            return JsonParser.parseString(logoutResponse).getAsJsonObject().toString();
        } else {
            response.status(getErrorCode(message));
            System.out.println("logout");
            return createJsonMessage(message);
        }
    }

    private Object listGames(Request request, Response response) {
        String listGamesResponse = gameHandler.listGames(request.headers("authorization"));
        String message = (new Gson()).fromJson(listGamesResponse, ListGamesResponse.class).message();
        if (message.isEmpty()) {
            response.status(200);
            return JsonParser.parseString(listGamesResponse).getAsJsonObject().toString();
        } else {
            response.status(getErrorCode(message));
            System.out.println("listGames");
            return createJsonMessage(message);
        }
    }

    private Object createGame(Request request, Response response) {
        String createGameResponse = gameHandler.createGame(request.headers("authorization"), request.body());
        String message = (new Gson()).fromJson(createGameResponse, CreateGameResponse.class).message();
        if (message.isEmpty()) {
            response.status(200);
            return JsonParser.parseString(createGameResponse).getAsJsonObject().toString();
        } else {
            response.status(getErrorCode(message));
            System.out.println("createGame");
            return createJsonMessage(message);
        }
    }

    private Object joinGame(Request request, Response response) {
        String joinGameResponse = gameHandler.joinGame(request.headers("authorization"), request.body());
        String message = (new Gson()).fromJson(joinGameResponse, JoinGameResponse.class).message();
        if (message.isEmpty()) {
            response.status(200);
            return JsonParser.parseString(joinGameResponse).getAsJsonObject().toString();
        } else {
            response.status(getErrorCode(message));
            System.out.println("joinGame");
            return createJsonMessage(message);
        }
    }

    private String createJsonMessage(String message) {
        return "{ \"message\": \"" + message + "\" }";
    }

    private int getErrorCode(String message) {
        return switch (message) {
            case "Error: bad request" -> 400;
            case "Error: already taken" -> 403;
            case "Error: unauthorized" -> 401;
            case "Error: description" -> 500;
            default -> 0;
        };
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
