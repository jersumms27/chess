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
    public AuthData createAuth() {
        return null;
    }

    @Override
    public AuthData getAuth(AuthData data) {
        return null;
    }

    @Override
    public void deleteAuth(AuthData data) {

    }
}
