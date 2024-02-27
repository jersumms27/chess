package dataAccess;

import model.*;

public interface AuthDAO {
    void clear();
    AuthData createAuth(String username) throws DataAccessException;
    String getAuth(String data) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    String getUser(String authToken) throws DataAccessException;
}
