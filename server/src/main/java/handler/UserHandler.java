package handler;

import service.request.LoginRequest;
import service.request.RegisterRequest;
import response.LoginResponse;
import response.LogoutResponse;
import response.RegisterResponse;

public class UserHandler implements Handler {

    // parameter: username, password, email
    // return: username, authToken
    public String register(String requestString) {
        System.out.println("yeet");
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
    public String logout(String authToken) {
        LogoutResponse responseObject = userService.logout(authToken);
        return serializer.toJson(responseObject);
    }
}
