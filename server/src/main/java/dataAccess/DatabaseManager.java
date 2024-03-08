package dataAccess;

import java.sql.*;
import java.util.Properties;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class DatabaseManager {
    private static final String databaseName;
    private static final String user;
    private static final String password;
    private static final String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) throw new Exception("Unable to laod db.properties");
                Properties props = new Properties();
                props.load(propStream);
                databaseName = props.getProperty("db.name");
                user = props.getProperty("db.user");
                password = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    static void createDatabase() throws DataAccessException {
        try {
            var statement = "CREATE SCHEMA IF NOT EXISTS " + databaseName;
            try (var conn = DriverManager.getConnection(connectionUrl, user, password);
                 var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DataAccessException(e.getMessage());
        }
    }

    static void createAuthTable() throws DataAccessException {
        try {
            String statement = """
                CREATE TABLE IF NOT EXISTS databaseName.`auth` (
                    `token` VARCHAR(255) NOT NULL,
                    `username` VARCHAR(255) NOT NULL,
                    PRIMARY KEY (`token`)
                );
                """;
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new DataAccessException(ex.getMessage());
        }
    }

    static void createUserTable() throws DataAccessException {
        try {
            String statement = """
                CREATE TABLE IF NOT EXISTS databaseName.`user` (
                    `username` VARCHAR(255),
                    `password` VARCHAR(255),
                    `email` VARCHAR(255),
                    PRIMARY KEY (`username`)
                );
                """;
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    static void createGameTable() throws DataAccessException {
        try {
            String statement = """
                CREATE TABLE IF NOT EXISTS databaseName.`auth` (
                    `id` INT NOT NULL AUTO_INCREMENT,
                    `whiteUsername` VARCHAR(255),
                    `blackUsername` VARCHAR(255),
                    `name` VARCHAR(255)
                    `game` VARCHAR(255)
                    PRIMARY KEY (`id`)
                );
                """;
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    protected static int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    var param = params[i];
                    switch (param) {
                        case String p -> ps.setString(i + 1, p);
                        case Integer p -> ps.setInt(i + 1, p);
                        case null -> ps.setNull(i + 1, NULL);
                        default -> {
                        }
                    }
                }
                return ps.executeUpdate();

                //var rs = ps.getGeneratedKeys();
                //if (rs.next()) {
                //    return rs.getInt(1);
                //}

                //return 0;
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, ex.getMessage()));
        }
    }

    protected static ResultSet executeQuery(String statement, Object... params) throws DataAccessException {
        try {
            var conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(statement);
            for (int i = 0; i < params.length; i++) {
                var param = params[i];
                switch (param) {
                    case String p -> ps.setString(i + 1, p);
                    case Integer p -> ps.setInt(i + 1, p);
                    case null -> ps.setNull(i + 1, NULL);
                    default -> {
                    }
                }
            }
            return ps.executeQuery();
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, ex.getMessage()));
        }
    }
}