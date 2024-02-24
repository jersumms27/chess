package dataAccess;

import model.AuthData;
import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    private final HashMap<String, AuthData> auths = new HashMap<>();

    @Override
    public void clear() {
        auths.clear();
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        if(auths.containsKey(username)) {
            throw new DataAccessException("Auth token already created");
        }
        String token = "";
        AuthData newData = new AuthData(token, username);
        auths.put(username, newData);
        return newData;
    }

    @Override
    public String getAuth(String username) throws DataAccessException {
        if(!auths.containsKey(username)) {
            throw new DataAccessException("Auth token not yet created");
        }
        return auths.get(username).authToken();
    }

    @Override
    public void deleteAuth(AuthData data) throws DataAccessException {
        if(!auths.containsValue(data)) {
            throw new DataAccessException("Auth token not found");
        }
        auths.remove(data.username());
    }
}
