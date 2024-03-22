import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class ClientCommunicator {
    public Object performAction(String url, String method, String body, Map<String, String> headers) throws Exception {
        HttpURLConnection http = sendRequest(url, method, body, headers);
        return receiveResponse(http);
    }

    private HttpURLConnection sendRequest(String url, String method, String body, Map<String, String> headers) throws Exception {
        URI uri = new URI(url);
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod(method);

        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry: headers.entrySet()) {
                http.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        writeRequestBody(body, http);
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

    private Object receiveResponse(HttpURLConnection http) throws Exception {
        int statusCode = http.getResponseCode();
        String statusMessage = http.getResponseMessage();

        return readResponseBody(http);
    }

    private Object readResponseBody(HttpURLConnection http) throws Exception {
        Object responseBody = "";
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            if (http.getRequestMethod().equals("GET")) {
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String str = "";
                while ((str = bufferedReader.readLine()) != null) {
                    responseBody += str;
                }
                return responseBody;
            } else {
                responseBody = new Gson().fromJson(inputStreamReader, Map.class);
            }
        }

        return (Map<String, String>) responseBody;
    }
}
