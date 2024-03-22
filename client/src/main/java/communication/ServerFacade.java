package communication;

import communication.ClientCommunicator;

import java.util.*;

public class ServerFacade {
    ClientCommunicator communicator;
    int port = 8080;
    public ServerFacade() {
        communicator = new ClientCommunicator();
    }

    public ServerFacade(int port) {
        communicator = new ClientCommunicator();
        this.port = port;
    }

    public Object communicate(String path, String method, String[] bodyKeys, String[] bodyValues, String authToken) throws Exception {
        Object response;

        String url = makePath(path);
        String body = createJson(new ArrayList<>(Arrays.asList(bodyKeys)), new ArrayList<>(Arrays.asList(bodyValues)));
        Map<String, String> header = new HashMap<>();
        if (bodyKeys.length == 0 && Objects.equals(authToken, "")) {
            response = communicator.performAction(url, method, "{}", null);
        } else if (bodyKeys.length == 0) {
            header.put("authorization", authToken);
            response = communicator.performAction(url, method, "", header);
        } else if (Objects.equals(authToken, "")) {
            response = communicator.performAction(url, method, body, null);
        } else {
            header.put("authorization", authToken);
            response = communicator.performAction(url, method, body, header);
        }
        return response;

    }

    private String makePath(String path) {
        return "http://localhost:" + port + "/" + path;
    }

    private String createJson(ArrayList<String> keys, ArrayList<String> values) {
        String body = "";
        if (keys.isEmpty()) {
            return "{}";
        }

        body += "{ ";
        for (int i = 0; i < keys.size(); i++) {
            body += "\"" + keys.get(i) + "\": \"" + values.get(i) + "\", ";
        }
        body = body.substring(0, body.length() - 2);
        body += " }";

        return body;
    }
}