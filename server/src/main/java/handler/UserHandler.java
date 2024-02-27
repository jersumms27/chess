package handler;

import com.google.gson.*;
import service.*;

public class UserHandler implements Handler {

    // parameter: username, password, email
    // return: username, authToken
    public String register(String requestString) {
        RegisterRequest requestObject = serializer.fromJson(requestString, RegisterRequest.class);
        RegisterResponse responseObject = userService.register(requestObject);
        return serializer.toJson(responseObject);
    }

    // parameter: username, password
    // return: username, authToken
    public String login(String requestString) {
        LoginRequest requestObject = serializer.fromJson(requestString, LoginRequest.class);
        LoginResponse responseObject = userService.login(requestObject);
        return serializer.toJson(responseObject);
    }

    // parameter: authToken
    // return:
    public String logout(String requestString) {
        LogoutRequest requestObject = serializer.fromJson(requestString, LogoutRequest.class);
        LogoutResponse responseObject = userService.logout(requestObject);
        return serializer.toJson(responseObject);
    }
}
