package dataAccess;

import model.UserData;

public class SQLUserDAO implements UserDAO {
    @Override
    public void clear() {

    }

    @Override
    public void createUser(UserData data) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void checkPassword(String username, String password) throws DataAccessException {

    }
}
