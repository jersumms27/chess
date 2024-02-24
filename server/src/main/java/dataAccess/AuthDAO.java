package dataAccess;

import model.*;

public interface AuthDAO {
    void clear();
    AuthData createAuth(String username);
    AuthData getAuth(AuthData data);
    void deleteAuth(AuthData data);
}
