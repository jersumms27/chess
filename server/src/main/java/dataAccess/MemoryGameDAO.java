package dataAccess;

import chess.*;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryGameDAO implements GameDAO {
    private final Collection<GameData> games = new ArrayList<>();

    @Override
    public void clear() {
        games.clear();
    }

    @Override
    public void createGame(GameData data) {
        games.add(data);
    }

    @Override
    public GameData getGame(int ID) throws DataAccessException {
        for (GameData game: games) {
            if (ID == game.gameID()) {
                return game;
            }
        }
        throw new DataAccessException("Game not found");
    }

    @Override
    public Collection<GameData> listGames() {
        return games;
    }

    @Override
    public void updateGame(GameData data) {

    }
}
