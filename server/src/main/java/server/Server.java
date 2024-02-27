package server;

import spark.*;
import handler.*;

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
        response.status(204);
        return clearHandler.clear();
    }

    private Object register(Request request, Response response) {
        String registerResponse = userHandler.register(request.body());
        return null;
    }

    private Object login(Request request, Response response) {
        return null;
    }

    private Object logout(Request request, Response response) {
        return null;
    }

    private Object listGames(Request request, Response response) {
        return null;
    }

    private Object createGame(Request request, Response response) {
        return null;
    }

    private Object joinGame(Request request, Response response) {
        return null;
    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
