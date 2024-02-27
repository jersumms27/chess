package dataAccess;

import model.*;

import java.util.Collection;

public interface GameDAO {
    void clear();
    GameData createGame(String name);
    GameData getGame(int id) throws DataAccessException;
    Collection<GameData> listGames();
    void updateGame(GameData data) throws DataAccessException;
    void verifyColor(int id, String color) throws DataAccessException;
}
