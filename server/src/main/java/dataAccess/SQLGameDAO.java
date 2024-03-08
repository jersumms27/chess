package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;
import service.response.CreateGameResponse;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        String statement = """
                SELECT *
                FROM `game`
                WHERE `id` = ?;
                """;

        try {
            try (ResultSet resultSet = DatabaseManager.executeQuery(statement, id)) {
                if (resultSet.next()) {
                    int id2 = resultSet.getInt("id");
                    String whiteUsername = resultSet.getString("whiteUsername");
                    String blackUsername = resultSet.getString("blackUsername");
                    String gameName = resultSet.getString("name");
                    String chessGameString = resultSet.getString("game");

                    ChessGame chessGameObject = (new Gson()).fromJson(chessGameString, ChessGame.class);
                    return new GameData(id2, whiteUsername, blackUsername, gameName, chessGameObject);
                } else {
                    throw new DataAccessException("Game not found");
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Game not found");
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        ArrayList<GameData> listOfData = new ArrayList<>();
        String statement = """
                SELECT *
                FROM `game`;
                """;

        try (ResultSet resultSet = DatabaseManager.executeQuery(statement)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String whiteUsername = resultSet.getString("whiteUsername");
                String blackUsername = resultSet.getString("blackUsername");
                String gameName = resultSet.getString("name");
                String chessGameString = resultSet.getString("game");

                ChessGame chessGameObject = (new Gson()).fromJson(chessGameString, ChessGame.class);
                listOfData.add(new GameData(id, whiteUsername, blackUsername, gameName, chessGameObject));
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Game not found");
        }

        return listOfData;
    }

    @Override
    public void updateGame(GameData data) throws DataAccessException {
        String deleteStatement = """
                DELETE FROM `game`
                WHERE `id` = ?;
                """;
        try {
            DatabaseManager.executeUpdate(deleteStatement, data.gameID());
        } catch (DataAccessException ex) {
            throw new DataAccessException("Game not found");
        }

        String statement = """
                INSERT INTO `game` (`id`, `whiteUsername`, `blackUsername`, `name`, `game`)
                VALUES (?, ?, ?, ?, ?);
                """;

        try {
            String chessGameString = (new Gson()).toJson(data.game());
            DatabaseManager.executeUpdate(statement, data.gameID(), data.whiteUsername(), data.blackUsername(), data.gameName(), chessGameString);
        } catch (DataAccessException ex2) {
            throw new DataAccessException("Game already created");
        }
    }

    @Override
    public void verifyColor(int id, String color) throws DataAccessException {
        String statement;
        if (color.equals("WHITE")) {
            statement = """
                    SELECT whiteUsername
                    FROM `game`
                    WHERE `id` = ?;
                    """;
        } else {
            statement = """
                    SELECT blackUsername
                    FROM `game`
                    WHERE `id` = ?;
                    """;
        }

        try (ResultSet resultSet = DatabaseManager.executeQuery(statement, id)) {
            if ((color.equals("WHITE") && resultSet.next() && resultSet.getString("whiteUsername") != null)
            || color.equals("BLACK") && resultSet.next() && resultSet.getString("blackUsername") != null) {
                throw new DataAccessException("Color not available");
            }
        } catch (SQLException ex) {
            throw new DataAccessException("User not found");
        }
    }
}
