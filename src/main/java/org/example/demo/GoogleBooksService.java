package org.example.demo;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GoogleBooksService {

    private static final String API_KEY = "AIzaSyA7HL8OtLP5Hl43OJs4Xo1hiGo3rwnta6Q";

    public JsonObject searchBooks(String query) {
        try {

            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String url = "https://www.googleapis.com/books/v1/volumes?q=" + encodedQuery + "&key=" + API_KEY;


            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpGet request = new HttpGet(url);
                try (CloseableHttpResponse response = httpClient.execute(request)) {

                    String jsonResponse = EntityUtils.toString(response.getEntity());
                    JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
                    return jsonObject;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
