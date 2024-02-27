package dataAccess;

import model.AuthData;
import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private final HashMap<String, AuthData> auths = new HashMap<>();

    @Override
    public void clear() {
        auths.clear();
    }

    @Override
    public String createAuth(String username) {
        String token = UUID.randomUUID().toString();
        AuthData newData = new AuthData(token, username);
        auths.put(username, newData);
        return token;
    }

    @Override
    public String getAuth(String username) throws DataAccessException {
        if (!auths.containsKey(username)) {
            throw new DataAccessException("Auth token not yet created");
        }
        return auths.get(username).authToken();
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        auths.remove(getUser(authToken));
    }

    @Override
    public String getUser(String authToken) throws DataAccessException {
        for (String key: auths.keySet()) {
            if (auths.get(key).authToken().equals(authToken)) {
                return auths.get(key).username();
            }
        }
        throw new DataAccessException("Auth token not found");
    }
}
