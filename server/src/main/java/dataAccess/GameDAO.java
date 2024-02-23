package dataAccess;

import model.*;

import java.util.Collection;

public interface GameDAO {
    void clear();
    void createGame(GameData data);
    GameData getGame(GameData data) throws DataAccessException;
    Collection<GameData> listGames();
    void updateGame(GameData data);
}
