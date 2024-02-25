package dataAccess;

import model.*;

import java.util.Collection;

public interface GameDAO {
    void clear();
    GameData createGame(String name);
    GameData getGame(int ID) throws DataAccessException;
    Collection<GameData> listGames();
    void updateGame(GameData data);
}
