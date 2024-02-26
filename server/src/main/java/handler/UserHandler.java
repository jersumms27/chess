package handler;

import com.google.gson.*;
import service.*;

public class UserHandler implements Handler {

    // parameter: username, password, email
    // return: username, authToken
    String register(String requestString) {
        RegisterRequest requestObject = serializer.fromJson(requestString, RegisterRequest.class);
        RegisterResponse responseObject = userService.register(requestObject);
        return serializer.toJson(responseObject);
    }

    // parameter: username, password
    // return: username, authToken
    String login(String requestString) {
        LoginRequest requestObject = serializer.fromJson(requestString, LoginRequest.class);
        LoginResponse responseObject = userService.login(requestObject);
        return serializer.toJson(responseObject);
    }

    // parameter: authToken
    // return:
    String logout(String requestString) {
        return null;
    }
}
