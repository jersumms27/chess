package dataAccess;

import model.AuthData;
import model.GameData;
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
    public UserData getUser(String username) throws DataAccessException {
        for (UserData user: users) {
            if (username.equals(user.username())) {
                return user;
            }
        }
        throw new DataAccessException("User not found");
    }
}
