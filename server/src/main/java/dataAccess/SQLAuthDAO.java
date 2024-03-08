package dataAccess;

import model.AuthData;
import org.springframework.security.core.parameters.P;

import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO {
    public SQLAuthDAO() {
        try {
            DatabaseManager.createDatabase();
            DatabaseManager.createAuthTable();
        } catch (DataAccessException ex) {
        }
    }
    @Override
    public void clear() throws DataAccessException {
        String statement = """
                DELETE FROM `chess`.`auth`;
                """;

        try {
            DatabaseManager.executeUpdate(statement);
        } catch (DataAccessException ex) {
            throw new DataAccessException("Auth token not found");
        }
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

       //ResultSet resultSet = null;
       try {
           System.out.println("starting select");
           try (ResultSet resultSet = DatabaseManager.executeQuery(statement, authToken)) {
               if (resultSet.next()) {
                   String token = resultSet.getString("token");
                   String username = resultSet.getString("username");
                   return new AuthData(token, username);
               } else {
                   throw new DataAccessException("Auth token not found");
               }
           }
       } catch (SQLException ex) {
           System.out.println(ex.getMessage());
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
            int rowsUpdated = DatabaseManager.executeUpdate(statement, authToken);
            if (rowsUpdated == 0) {
                throw new DataAccessException("Auth token not found");
            }
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

        try {
            try(ResultSet resultSet = DatabaseManager.executeQuery(statement, authToken)) {
                if (resultSet.next()) {
                    return resultSet.getString("username");
                } else {
                    throw new DataAccessException("User not found");
                }
            }
        } catch (DataAccessException | SQLException ex) {
            throw new DataAccessException("User not found");
        }
    }
}
