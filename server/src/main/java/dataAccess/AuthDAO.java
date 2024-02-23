package dataAccess;

import model.*;

public interface AuthDAO {
    void clear();
    AuthData createAuth();
    AuthData getAuth(AuthData data);
    void deleteAuth(AuthData data);
}
