package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import service.response.CreateGameResponse;

import java.util.Collection;

public class SQLGameDAO implements GameDAO {
    int id;
    public SQLGameDAO() {
        id = 0;
        try {
            DatabaseManager.createDatabase();
            DatabaseManager.createGameTable();
        } catch (DataAccessException ex) {
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String statement = """
                DELETE FROM `game`;
                """;

        try {
            DatabaseManager.executeUpdate(statement);
        } catch (DataAccessException ex) {
            throw new DataAccessException("Game not found");
        }
    }

    @Override
    public GameData createGame(String name) throws DataAccessException {
        ChessGame newChessGame = new ChessGame();
        GameData data = new GameData(id++, null, null, name, new ChessGame());
        String chessGameString = (new Gson()).toJson(newChessGame);

        String statement = """
                INSERT INTO `game` (`id`, `whiteUsername`, `blackUsername`, `name`, `game`)
                VALUES (?, ?, ?, ?, ?);
                """;

        try {
            DatabaseManager.executeUpdate(statement, data.gameID(), data.whiteUsername(), data.blackUsername(), data.gameName(), chessGameString);
        } catch (DataAccessException ex) {
            throw new DataAccessException("Game already created");
        }

        return data;
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
