package communication;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.*;

public class NotificationHandler {
    void notify(String message) {
        Gson gson = new Gson();
        try {
            LoadGameMessage loadGameMessage = gson.fromJson(message, LoadGameMessage.class);
            loadGame(loadGameMessage);
        } catch (Exception ex) {
            try {
                NotificationMessage notificationMessage = gson.fromJson(message, NotificationMessage.class);
                notification(notificationMessage);
            } catch (Exception ex2) {
                try {
                    ErrorMessage errorMessage = gson.fromJson(message, ErrorMessage.class);
                    error(errorMessage);
                } catch (Exception ex3) {
                }
            }
        }
    }

    void loadGame(LoadGameMessage message) {
        System.out.println(message.getGame());
    }

    void notification(NotificationMessage message) {
        System.out.println(message.getMessage());
    }

    void error(ErrorMessage message) {
        System.out.println(message.getErrorMessage());
    }
}
