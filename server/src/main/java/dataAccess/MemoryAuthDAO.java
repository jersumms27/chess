package dataAccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryAuthDAO implements AuthDAO {
    private final Collection<AuthData> auths = new ArrayList<>();

    @Override
    public void clear() {
        auths.clear();
    }

    @Override
    public AuthData createAuth(String username) {
        String token = "";
        AuthData newData = new AuthData(token, username);
        auths.add(newData);

        return newData;
    }

    @Override
    public AuthData getAuth(AuthData data) {
        return data;
    }

    @Override
    public void deleteAuth(AuthData data) {
        auths.remove(data);
    }
}
