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
    public void createUser(UserData data) {
        users.put(data.username(), data);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        if(users.containsKey(username)) {
            return users.get(username);
        }
        throw new DataAccessException("User not found");
    }
}
