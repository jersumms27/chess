package dataAccess;

import model.*;

public interface UserDAO {
    public void clear();
    public void createUser(UserData data);
    public UserData getUser(String username) throws DataAccessException;
}
