package dataAccess;

import chess.*;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryGameDAO implements GameDAO {
    private final Collection<GameData> games = new ArrayList<>();
    private int id = 0;

    @Override
    public void clear() {
        games.clear();
    }

    @Override
    public GameData createGame(String name) {
        GameData newGame = new GameData(id++, null, null, name, new ChessGame());
        games.add(newGame);
        return newGame;
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
