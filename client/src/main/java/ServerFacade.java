import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class ServerFacade {
    public void performAction(String url, String method, String body) throws Exception {
        HttpURLConnection http = sendRequest(url, method, body);
        receiveResponse(http);
    }

    private HttpURLConnection sendRequest(String url, String method, String body) throws Exception {
        URI uri = new URI(url);
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod(method);
        writeRequestBody(body, http);
        System.out.println("Made body");
        http.connect();

        return http;
    }

    private void writeRequestBody(String body, HttpURLConnection http) throws Exception {
        if (!body.isEmpty()) {
            http.setDoOutput(true);
            try (var outputStream = http.getOutputStream()) {
                outputStream.write(body.getBytes());
            }
        }
    }

    private void receiveResponse(HttpURLConnection http) throws Exception {
        int statusCode = http.getResponseCode();
        String statusMessage = http.getResponseMessage();

        Object responseBody = readResponseBody(http);
        System.out.println("Status code: " + statusCode);
        System.out.println("Status message" + statusMessage);
        System.out.println("Response body: " + responseBody);
    }

    private Object readResponseBody(HttpURLConnection http) throws Exception {
        Object responseBody = "";
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            responseBody = new Gson().fromJson(inputStreamReader, Map.class);
        }

        return responseBody;
    }
}
