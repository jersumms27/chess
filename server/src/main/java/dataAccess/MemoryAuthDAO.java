package dataAccess;

import model.AuthData;

import java.util.ArrayList;
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
        auths.put(token, newData);
        return token;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        if (auths.containsKey(authToken)) {
            return auths.get(authToken);
        }
        throw new DataAccessException("Auth token not found");
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        if (!auths.isEmpty() && auths.containsKey(authToken)) {
            auths.remove(authToken);
        } else {
            throw new DataAccessException("Auth token not found");
        }
    }

    @Override
    public String getUser(String authToken) throws DataAccessException {
        if (!auths.containsKey(authToken)) {
            throw new DataAccessException("Auth token not found");
        }
        return auths.get(authToken).username();
    }
}
