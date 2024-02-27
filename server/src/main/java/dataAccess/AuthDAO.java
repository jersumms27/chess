package dataAccess;

import model.*;

public interface AuthDAO {
    void clear();
    String createAuth(String username);
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    String getUser(String authToken) throws DataAccessException;
}
