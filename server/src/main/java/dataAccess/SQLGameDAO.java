package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class SQLGameDAO implements GameDAO {
    int id;
    public SQLGameDAO() {
        try {
            DatabaseManager.createDatabase();
            DatabaseManager.createGameTable();
            id = nextAvailableID();
        } catch (DataAccessException | SQLException ex) {
            id = 0;
        }
    }

    public int nextAvailableID() throws DataAccessException, SQLException {
        String statement = "SELECT MAX(id) FROM game;";
        try (ResultSet resultSet = DatabaseManager.executeQuery(statement)) {
            if (resultSet.next()) {
                int maxID = resultSet.getInt(1);
                return maxID + 1;
            } else {
                return 1;
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error getting id");
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
        GameData data = new GameData(id++, null, null, name, newChessGame);
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
        String statement = getVerifyStatement(color);

        try (ResultSet resultSet = DatabaseManager.executeQuery(statement, id)) {
            if ((color.equals("WHITE") && resultSet.next() && resultSet.getString("whiteUsername") != null)
            || color.equals("BLACK") && resultSet.next() && resultSet.getString("blackUsername") != null) {
                throw new DataAccessException("Color not available");
            }
        } catch (SQLException ex) {
            throw new DataAccessException("User not found");
        }
    }

    @Override
    public void verifyColor(int id, String color, String username) throws DataAccessException {
        String statement = getVerifyStatement(color);

        try (ResultSet resultSet = DatabaseManager.executeQuery(statement, id)) {
            if ((color.equals("WHITE") && resultSet.next() && !Objects.equals(resultSet.getString("whiteUsername"), username))
                    || color.equals("BLACK") && resultSet.next() && !Objects.equals(resultSet.getString("blackUsername"), username)) {
                throw new DataAccessException("Color not available");
            }
        } catch (SQLException ex) {
            throw new DataAccessException("User not found");
        }
    }

    private static String getVerifyStatement(String color) throws DataAccessException {
        String statement;
        if (color.equals("WHITE")) {
            statement = """
                    SELECT whiteUsername
                    FROM `game`
                    WHERE `id` = ?;
                    """;
        } else if (color.equals("BLACK")) {
            statement = """
                    SELECT blackUsername
                    FROM `game`
                    WHERE `id` = ?;
                    """;
        } else {
            throw new DataAccessException("Invalid color");
        }
        return statement;
    }
}
