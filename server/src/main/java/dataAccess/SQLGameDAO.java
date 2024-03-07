package dataAccess;

import model.GameData;

import java.util.Collection;

public class SQLGameDAO implements GameDAO {
    @Override
    public void clear() {

    }

    @Override
    public GameData createGame(String name) {
        return null;
    }

    @Override
    public GameData getGame(int id) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        return null;
    }

    @Override
    public void updateGame(GameData data) throws DataAccessException {

    }

    @Override
    public void verifyColor(int id, String color) throws DataAccessException {

    }
}
