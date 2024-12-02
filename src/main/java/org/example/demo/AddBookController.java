package org.example.demo;

import com.google.gson.JsonObject;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class AddBookController {

    private Stage stage;
    private final GoogleBooksService googleBooksService = new GoogleBooksService();
    private final ExecutorService executorService = Executors.newCachedThreadPool(); // Quản lý luồng

    @FXML
    private Label successLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private Label logLabel;

    @FXML
    private Label searchFailLabel;

    @FXML
    private Pane pane;

    @FXML
    private TextField searchTextField;
    @FXML
    private ListView<Book> bookListView; // Sử dụng ListView để hiển thị danh sách sách
    @FXML
    private ProgressIndicator loadingIndicator;

    // Đặt stage cho cửa sổ hiện tại
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        // Ẩn các thành phần ban đầu
        loadingIndicator.setVisible(false);
        pane.setVisible(false);

        // Set factory cho ListView để hiển thị sách
        bookListView.setCellFactory(param -> new AddBookCellFactory());


        // Xử lý tìm kiếm khi nhấn Enter
        searchTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                searchBook();
            }
        });

    }


    // Phương thức tìm kiếm sách
    @FXML
    private void searchBook() {
        String query = searchTextField.getText();
        if (query != null && !query.isEmpty()) {
            loadingIndicator.setVisible(true); // Hiển thị trạng thái đang tải

            // Tìm kiếm sách trong background
            executorService.submit(() -> {
                try {
                    JsonObject response = googleBooksService.searchBooks(query);
                    Platform.runLater(() -> {
                        updateBookList(response);
                        loadingIndicator.setVisible(false); // Ẩn trạng thái đang tải
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater(() -> loadingIndicator.setVisible(false));
                }
            });
        }
    }

    // Cập nhật danh sách sách vào ListView
    private void updateBookList(JsonObject response) {
        List<Book> books = new ArrayList<>();
        if (response != null && response.has("items") && response.getAsJsonArray("items").size() > 0) {
            for (int i = 0; i < response.getAsJsonArray("items").size(); i++) {
                JsonObject item = response.getAsJsonArray("items").get(i).getAsJsonObject();
                JsonObject volumeInfo = item.getAsJsonObject("volumeInfo");

                String title = volumeInfo.has("title") ? volumeInfo.get("title").getAsString() : "Không có tên sách";
                String authors = volumeInfo.has("authors") ? volumeInfo.getAsJsonArray("authors").get(0).getAsString() : "Không có tác giả";
                String publisher = volumeInfo.has("publisher") ? volumeInfo.get("publisher").getAsString() : "Không có nhà xuất bản";
                String publishedDate = volumeInfo.has("publishedDate") ? volumeInfo.get("publishedDate").getAsString() : "Không có ngày xuất bản";
                String genre = volumeInfo.has("categories") ? volumeInfo.getAsJsonArray("categories").get(0).getAsString() : "Không có thể loại";
                String isbn = volumeInfo.has("industryIdentifiers") ? volumeInfo.getAsJsonArray("industryIdentifiers").get(0).getAsJsonObject().get("identifier").getAsString() : "Không có ISBN";
                String description = volumeInfo.has("description") ? volumeInfo.get("description").getAsString() : "Không có mô tả";
                String imageUrl = volumeInfo.has("imageLinks") ? volumeInfo.getAsJsonObject("imageLinks").get("thumbnail").getAsString() : null;

                // Tải ảnh và chuyển thành byte[]
                byte[] image = null;
                if (imageUrl != null) {
                    image = downloadImage(imageUrl);
                }
                // Tạo đối tượng Book từ thông tin nhận được từ API
                Book book = new Book(0,title, authors, publisher, publishedDate, genre, 5, isbn, image, description);  // Thêm mô tả vào constructor
                books.add(book);
            }

            // Cập nhật danh sách sách trong ListView
            bookListView.getItems().setAll(books);

            bookListView.setCellFactory(param -> {
                AddBookCellFactory cellFactory = new AddBookCellFactory();
                return cellFactory;
            });
        } else {
            bookListView.getItems().clear();  // Xóa danh sách sách cũ
            resetUI();
            searchFailLabel.setVisible(true);
        }
    }


    public byte[] downloadImage(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            InputStream inputStream = url.openStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(ImageIO.read(inputStream), "jpg", byteArrayOutputStream); // Giả sử ảnh là JPG
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Nếu có lỗi, trả về null
        }
    }

    @FXML
    private void handleAddBook(ActionEvent event) {
        // Lấy sách được chọn từ ListView
        Book selectedBook = bookListView.getSelectionModel().getSelectedItem();
        

        // Kiểm tra nếu có sách được chọn
        if (selectedBook != null) {
            if(!(Database.isBookExistByIsbn(selectedBook.getIsbn(), selectedBook.getTitle()))) {
                // Gọi hàm insertBook để thêm sách vào cơ sở dữ liệu
                Database.insertBook(
                        selectedBook.getTitle(),
                        selectedBook.getAuthor(),
                        selectedBook.getPublisher(),
                        selectedBook.getPublishedDate(),
                        selectedBook.getGenre(),
                        selectedBook.getQuantity(),
                        selectedBook.getIsbn(),
                        selectedBook.getDescription(),  // Truyền mô tả sách
                        selectedBook.getImage()          // Truyền ảnh sách
                );
                resetUI();
                successLabel.setVisible(true);
            } else {
                resetUI();
                errorLabel.setVisible(true);
            }
        } else {
            resetUI();
            logLabel.setVisible(true);
            // Nếu không có sách nào được chọn, hiển thị thông báo
            System.out.println("Vui lòng chọn một sách để thêm.");
        }
    }

    private void resetUI() {
        errorLabel.setVisible(false);
        successLabel.setVisible(false);
        logLabel.setVisible(false);
        searchFailLabel.setVisible(false);
    }

    private void loadPage(String fxmlFileName) {
        try {
            // Load FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent root = loader.load();

            // Xử lý chuyển cảnh và cấu hình controller
            pane.setVisible(true);
            pane.getChildren().clear();
            pane.getChildren().setAll(root);
            pane.setBackground(new Background(new BackgroundFill(Color.web("F4F4F4"), null, null)));

            FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), root);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setToValue(1.0);
            fadeTransition.play();

            // Xử lý controller
            Object controller = loader.getController();
            if (controller != null) {
                if (controller instanceof ListReaderController) {
                    AddBookController addBookController = (AddBookController) controller;
                    addBookController.setStage((Stage) pane.getScene().getWindow());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showBookListView() { loadPage("bookList-view.fxml"); }

    }
