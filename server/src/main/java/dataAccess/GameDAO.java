package dataAccess;

import model.*;

import java.util.Collection;

public interface GameDAO {
    void clear();
    void createGame(GameData data);
    GameData getGame(int ID) throws DataAccessException;
    Collection<GameData> listGames();
    void updateGame(GameData data);
}
