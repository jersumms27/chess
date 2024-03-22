package handler;

import response.ClearResponse;

public class ClearHandler implements Handler {
    public String clear() {
        ClearResponse response = clearService.clear();
        return serializer.toJson(response);
    }
}
