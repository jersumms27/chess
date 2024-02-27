package dataAccess;

import chess.*;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    private final HashMap<Integer, GameData> games = new HashMap<>();
    private int id = 0;

    @Override
    public void clear() {
        games.clear();
    }

    @Override
    public GameData createGame(String name) {
        GameData newGame = new GameData(id++, null, null, name, new ChessGame());
        games.put(newGame.gameID(), newGame);
        return newGame;
    }

    @Override
    public GameData getGame(int id) throws DataAccessException {
        if (games.containsKey(id)) {
            return games.get(id);
        }
        throw new DataAccessException("Game not found");
    }

    @Override
    public Collection<GameData> listGames() {
        return games.values();
    }

    // Updates a chess game.
    // It should replace the chess game string corresponding to a given gameID.
    // This is used when players join a game or when a move is made.
    @Override
    public void updateGame(GameData data) throws DataAccessException {
        GameData game = getGame(data.gameID());

        games.put(game.gameID(), new GameData(game.gameID(), data.whiteUsername(), data.blackUsername(), data.gameName(), data.game()));
    }

    @Override
    public void verifyColor(int id, String color) throws DataAccessException {
        String username;
        if (color.equals("WHITE")) {
            username = games.get(id).whiteUsername();
        } else {
            username = games.get(id).blackUsername();
        }

        if (username != null) {
            throw new DataAccessException("Color not available");
        }
    }
}
