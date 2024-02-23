package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryUserDAO implements UserDAO {
    private final Collection<UserData> users = new ArrayList<>();

    @Override
    public void clear() {
        users.clear();
    }

    @Override
    public void createUser(UserData data) {
        users.add(data);
    }

    @Override
    public UserData getUser(UserData data) throws DataAccessException {
        return null;
    }
}
