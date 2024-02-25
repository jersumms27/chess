package handler;

import com.google.gson.JsonObject;
import service.*;

public class ClearHandler implements Handler {
    public String clear() {
        ClearResponse response = clearService.clear();
        return serializer.toJson(response);
    }
}
