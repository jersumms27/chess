package dataAccess;

import model.AuthData;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {
    public SQLUserDAO() {
        try {
            DatabaseManager.createDatabase();
            DatabaseManager.createUserTable();
        } catch (DataAccessException ex) {
        }
    }
    @Override
    public void clear() throws DataAccessException {
        String statement = """
                DELETE FROM `user`;
                """;

        try {
            DatabaseManager.executeUpdate(statement);
        } catch (DataAccessException ex) {
            throw new DataAccessException("Auth token not found");
        }
    }

    @Override
    public void createUser(UserData data) throws DataAccessException {
        String statement = """
                INSERT INTO `user` (`username`, `password`, `email`)
                VALUES (?, ?, ?);
                """;
        try {
            DatabaseManager.executeUpdate(statement, data.username(), data.password(), data.email());
        } catch (DataAccessException ex) {
            throw new DataAccessException("User already created");
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        String statement = """
                SELECT *
                FROM `user`
                WHERE `username` = ?'
                """;

        try {
            try (ResultSet resultSet = DatabaseManager.executeQuery(statement, username)) {
                if (resultSet.next()) {
                    String name = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String email = resultSet.getString("email");
                    return new UserData(name, password, email);
                } else {
                    throw new DataAccessException("User not found");
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("User not found");
        }
    }

    @Override
    public void checkPassword(String username, String password) throws DataAccessException {

    }
}
