package org.example.demo;

import com.google.gson.JsonObject;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AddBookController {

    private final GoogleBooksService googleBooksService = new GoogleBooksService();
    private final ExecutorService executorService = Executors.newCachedThreadPool(); 
    private Stage stage;
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
    private ListView<Book> bookListView; 
    @FXML
    private ProgressIndicator loadingIndicator;

    
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        
        loadingIndicator.setVisible(false);
        pane.setVisible(false);

        
        bookListView.setCellFactory(param -> new AddBookCellFactory());


        
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
            loadingIndicator.setVisible(true); 

            
            executorService.submit(() -> {
                try {
                    JsonObject response = googleBooksService.searchBooks(query);
                    Platform.runLater(() -> {
                        updateBookList(response);
                        loadingIndicator.setVisible(false); 
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater(() -> loadingIndicator.setVisible(false));
                }
            });
        }
    }

    
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

                
                byte[] image = null;
                if (imageUrl != null) {
                    image = downloadImage(imageUrl);
                }
                
                Book book = new Book(0, title, authors, publisher, publishedDate, genre, 5, isbn, image, description);  
                books.add(book);
            }

            
            bookListView.getItems().setAll(books);

            bookListView.setCellFactory(param -> {
                AddBookCellFactory cellFactory = new AddBookCellFactory();
                return cellFactory;
            });
        } else {
            bookListView.getItems().clear();  
            resetUI();
            searchFailLabel.setVisible(true);
        }
    }


    public byte[] downloadImage(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            InputStream inputStream = url.openStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(ImageIO.read(inputStream), "jpg", byteArrayOutputStream); 
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null; 
        }
    }

    @FXML
    private void handleAddBook(ActionEvent event) {
        
        Book selectedBook = bookListView.getSelectionModel().getSelectedItem();


        
        if (selectedBook != null) {
            if (!(MySQLDatabase.getBookDatabase().isBookExistByIsbn(selectedBook.getIsbn(), selectedBook.getTitle()))) {
                
                MySQLDatabase.getBookDatabase().insertBook(
                        selectedBook.getTitle(),
                        selectedBook.getAuthor(),
                        selectedBook.getPublisher(),
                        selectedBook.getPublishedDate(),
                        selectedBook.getGenre(),
                        selectedBook.getQuantity(),
                        selectedBook.getIsbn(),
                        selectedBook.getDescription(),  
                        selectedBook.getImage()          
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
            
            System.out.println("Please select a book to add!");
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
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent root = loader.load();

            
            pane.setVisible(true);
            pane.getChildren().clear();
            pane.getChildren().setAll(root);
            pane.setBackground(new Background(new BackgroundFill(Color.web("F4F4F4"), null, null)));

            FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), root);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setToValue(1.0);
            fadeTransition.play();

            
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
    public void showBookListView() {
        loadPage("bookList-view.fxml");
    }

}
