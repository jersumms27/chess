package dataAccess;

import model.UserData;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    private final HashMap<String, UserData> users = new HashMap<>();

    @Override
    public void clear() {
        users.clear();
    }

    @Override
    public void createUser(UserData data) throws DataAccessException {
        if (!users.containsKey(data.username())) {
            users.put(data.username(), data);
            return;
        }
        throw new DataAccessException("User already created");
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        if (users.containsKey(username)) {
            return users.get(username);
        }
        throw new DataAccessException("User not found");
    }

    @Override
    public void checkPassword(String username, String password) throws DataAccessException {
        UserData user = getUser(username);
        if (!user.password().equals(password)) {
            throw new DataAccessException("Wrong password");
        }
    }
}
