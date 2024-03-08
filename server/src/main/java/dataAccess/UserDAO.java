package dataAccess;

import model.*;

public interface UserDAO {
    public void clear() throws DataAccessException;
    public void createUser(UserData data) throws DataAccessException;
    public UserData getUser(String username) throws DataAccessException;
    public void checkPassword(String username, String password) throws DataAccessException;
}
