package org.example.demo;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GoogleBooksService {

    private static final String API_KEY = "AIzaSyA7HL8OtLP5Hl43OJs4Xo1hiGo3rwnta6Q";  // Thay API key của bạn ở đây

    // Phương thức tìm kiếm sách
    public JsonObject searchBooks(String query) {
        try {
            // Mã hóa query để tránh ký tự đặc biệt trong URL
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
            String url = "https://www.googleapis.com/books/v1/volumes?q=" + encodedQuery + "&key=" + API_KEY;

            // Tạo HTTP client và gửi yêu cầu GET
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpGet request = new HttpGet(url);
                try (CloseableHttpResponse response = httpClient.execute(request)) {
                    // Đọc phản hồi từ API và chuyển thành chuỗi JSON
                    String jsonResponse = EntityUtils.toString(httpClient.execute(request).getEntity());
                    JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
                    return jsonObject;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Trả về null nếu có lỗi xảy ra
        }
    }
}
