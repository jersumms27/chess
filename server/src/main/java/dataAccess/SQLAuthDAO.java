package dataAccess;

import model.AuthData;

import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO {
    @Override
    public void clear() {

    }

    @Override
    public String createAuth(String username) throws DataAccessException {
        String token = UUID.randomUUID().toString();
        String statement = """
                INSERT INTO `chess`.`auth` (`token`, `username`)
                VALUES (?, ?);
                """;
        try {
            DatabaseManager.executeUpdate(statement, token, username);
        } catch (DataAccessException ex) {
            throw new DataAccessException("Auth token not found");
        }

        return token;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
       String statement = """
               SELECT `token`, `username`
               FROM `chess`.`auth`
               WHERE `token` = ?;
               """;

       ResultSet resultSet = null;
       try {
           resultSet = DatabaseManager.executeQuery(statement, authToken);
           return new AuthData(resultSet.getString("token"), resultSet.getString("username"));
       } catch (DataAccessException | SQLException ex) {
           throw new DataAccessException("Auth token not found");
       }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        String statement = """
                DELETE FROM `chess`.`auth`
                WHERE `token` = ?
                """;
        try {
            DatabaseManager.executeUpdate(statement, authToken);
        } catch (DataAccessException ex) {
            throw new DataAccessException("Auth token not found");
        }
    }

    @Override
    public String getUser(String authToken) throws DataAccessException {
        String statement = """
               SELECT `token`, `username`
               FROM `chess`.`auth`
               WHERE `token` = ?;
               """;

        ResultSet resultSet = null;
        try {
            resultSet = DatabaseManager.executeQuery(statement, authToken);
            return resultSet.getString("user");
        } catch (DataAccessException | SQLException ex) {
            throw new DataAccessException("User not found");
        }
    }
}
