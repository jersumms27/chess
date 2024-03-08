package dataAccess;

import model.*;

public interface AuthDAO {
    void clear() throws DataAccessException;
    String createAuth(String username) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    String getUser(String authToken) throws DataAccessException;
}
