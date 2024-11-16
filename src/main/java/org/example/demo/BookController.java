package org.example.demo;

import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.scene.control.ProgressIndicator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookController {

    @FXML
    private TextField searchTextField;
    @FXML
    private Label bookTitleLabel;
    @FXML
    private Label authorLabel;
    @FXML
    private Label publishedDateLabel;
    @FXML
    private Label publisherLabel;
    @FXML
    private Label genreLabel;
    @FXML
    private ImageView bookImageView;
    @FXML
    private Region bookInfoContainer;
    @FXML
    private Button addButton;
    @FXML
    private Label isbnLabel;
    @FXML
    private Label bookError;
    @FXML
    private Label addSuccessLabel;
    @FXML
    private ProgressIndicator loadingIndicator;

    private final GoogleBooksService googleBooksService = new GoogleBooksService();
    private final ExecutorService executorService = Executors.newCachedThreadPool(); // Quản lý luồng

    @FXML
    private void initialize() {
        // Ẩn các thành phần ban đầu
        resetUI();

        // Xử lý tìm kiếm khi nhấn Enter
        searchTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                searchBook();
            }
        });
    }

    @FXML
    private void searchBook() {
        String query = searchTextField.getText();
        if (query != null && !query.isEmpty()) {
            resetUI(); // Đặt lại UI trước khi tìm kiếm
            loadingIndicator.setVisible(true); // Hiển thị trạng thái đang tải

            executorService.submit(() -> {
                try {
                    JsonObject response = googleBooksService.searchBooks(query);
                    Platform.runLater(() -> {
                        updateBookInfo(response);
                        loadingIndicator.setVisible(false); // Ẩn trạng thái đang tải
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater(() -> loadingIndicator.setVisible(false));
                }
            });
        }
    }

    private void updateBookInfo(JsonObject response) {
        if (response != null && response.has("items")) {
            JsonObject item = response.getAsJsonArray("items").get(0).getAsJsonObject();
            JsonObject volumeInfo = item.getAsJsonObject("volumeInfo");

            String title = volumeInfo.has("title") ? volumeInfo.get("title").getAsString() : null;
            String authors = volumeInfo.has("authors") ? volumeInfo.getAsJsonArray("authors").get(0).getAsString() : null;
            String publisher = volumeInfo.has("publisher") ? volumeInfo.get("publisher").getAsString() : null;
            String publishedDate = volumeInfo.has("publishedDate") ? volumeInfo.get("publishedDate").getAsString() : null;
            String genre = volumeInfo.has("categories") ? volumeInfo.getAsJsonArray("categories").get(0).getAsString() : null;
            String imageUrl = volumeInfo.has("imageLinks") ? volumeInfo.getAsJsonObject("imageLinks").get("thumbnail").getAsString() : null;
            String isbn = volumeInfo.has("industryIdentifiers") ? volumeInfo.getAsJsonArray("industryIdentifiers").get(0).getAsJsonObject().get("identifier").getAsString() : null;

            setLabel(bookTitleLabel, "Tên sách: ", title);
            setLabel(authorLabel, "Tác giả: ", authors);
            setLabel(publisherLabel, "Nhà xuất bản: ", publisher);
            setLabel(publishedDateLabel, "Ngày xuất bản: ", publishedDate);
            setLabel(genreLabel, "Thể loại: ", genre);
            setLabel(isbnLabel, "ISBN: ", isbn);
            addButton.setVisible(true);

            // Cập nhật ảnh
            if (imageUrl != null) {
                bookImageView.setImage(new Image(imageUrl));
            } else {
                bookImageView.setImage(new Image(getClass().getResource("/media/no_image.jpg").toExternalForm()));
            }

            bookInfoContainer.setVisible(true); // Hiển thị vùng chứa thông tin sách
        } else {
            resetUI(); // Đặt lại UI nếu không có kết quả
        }
    }

    @FXML
    private void addBook() {
        String title = bookTitleLabel.getText().replace("Tên sách: ", "");
        String author = authorLabel.getText().replace("Tác giả: ", "");
        String publisher = publisherLabel.getText().replace("Nhà xuất bản: ", "");
        String publishedDate = publishedDateLabel.getText().replace("Ngày xuất bản: ", "");
        String genre = genreLabel.getText().replace("Thể loại: ", "");
        String isbn = isbnLabel.getText().replace("ISBN: ", "");

        java.sql.Date sqlDate = parsePublishedDate(publishedDate);

        if (Database.isBookExistByIsbn(isbn, title)) {
            bookError.setVisible(true);
            addSuccessLabel.setVisible(false);
        } else {
            Database.insertBook(title, author, publisher, sqlDate, genre, 5, isbn);
            bookError.setVisible(false);
            addSuccessLabel.setVisible(true);
        }
    }

    private java.sql.Date parsePublishedDate(String publishedDate) {
        if (publishedDate == null || publishedDate.isEmpty()) {
            return null;
        }
        try {
            if (publishedDate.length() == 4) {
                publishedDate += "-01-01"; // Thêm ngày mặc định
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = format.parse(publishedDate);
            return new java.sql.Date(utilDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void resetUI() {
        bookInfoContainer.setVisible(false);
        bookTitleLabel.setVisible(false);
        authorLabel.setVisible(false);
        publishedDateLabel.setVisible(false);
        publisherLabel.setVisible(false);
        genreLabel.setVisible(false);
        isbnLabel.setVisible(false);
        addButton.setVisible(false);
        bookError.setVisible(false);
        addSuccessLabel.setVisible(false);
        bookImageView.setImage(null); // Làm trống ảnh
        loadingIndicator.setVisible(false);
    }

    private void setLabel(Label label, String prefix, String value) {
        if (value != null && !value.isEmpty()) {
            label.setText(prefix + value);
            label.setVisible(true);
        } else {
            label.setVisible(false);
        }
    }
}
