package org.example.demo;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ListBookController {
    private Stage stage;
    @FXML
    private TableView<Book> bookTable;
    @FXML
    private TableColumn<Book, Integer> colId;
    @FXML
    private TableColumn<Book, String> colTitle;
    @FXML
    private TableColumn<Book, String> colAuthor;
    @FXML
    private TableColumn<Book, String> colPublishedDate;
    @FXML
    private TableColumn<Book, Integer> colQuantity;
    @FXML
    private TableColumn<Book, Void> colAction;
    @FXML
    private TextField txtTitle;
    @FXML
    private TextField txtAuthor;
    @FXML
    private Rectangle rectangle;
    @FXML
    private Label dateTimeLabel;
    @FXML
    private Pane pane;
    @FXML
    private Label hi;
    private final ObservableList<Book> bookList = FXCollections.observableArrayList();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        sayHi();
        rectangle.setVisible(false);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colPublishedDate.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        pane.setVisible(false);


        addButtonToTable();
        loadBookData();

        bookTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
                if (selectedBook != null) {
                    openBookDetailsWindow(selectedBook);
                }
            }
        });

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            LocalDateTime now = LocalDateTime.now();
            dateTimeLabel.setText(now.format(formatter));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

    }

    private void sayHi() {
        String userName = MySQLDatabase.getUserDatabase().getFullName(LoginController.account.getUsername());
        if (userName != null) {
            hi.setText("Hi, " + userName);
        }
    }


    private void loadBookData() {
        try {
            bookList.clear();
            bookList.addAll(MySQLDatabase.getBookDatabase().getBooks());
            bookTable.setItems(bookList); 
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openBookDetailsWindow(Book book) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("bookDetail-view.fxml"));
            Parent bookDetailsParent = fxmlLoader.load();
            pane.setVisible(true);
            pane.getChildren().clear();
            pane.getChildren().setAll(bookDetailsParent);
            pane.setBackground(new Background(new BackgroundFill(Color.web("F4F4F4"), null, null)));

            FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), bookDetailsParent);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setToValue(1.0);
            fadeTransition.play();

            Object controller = fxmlLoader.getController();
            if (controller instanceof BookDetailsController bookDetailsController) {
                bookDetailsController.setStage((Stage) pane.getScene().getWindow());
                bookDetailsController.displayBookDetails(book);
                bookDetailsController.setBook(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addButtonToTable() {
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button btnDelete = new Button("Xóa");

            {

                Image trashIcon = new Image(getClass().getResourceAsStream("/media/trash.png"));
                ImageView imageView = new ImageView(trashIcon);
                imageView.setFitWidth(20);
                imageView.setFitHeight(20);

                btnDelete.setGraphic(imageView);
                btnDelete.getStylesheets().add((getClass().getResource("/CustomerStyle/deleteButton-style.css")).toExternalForm());
                btnDelete.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    handleDeleteBook(book);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnDelete);
                }
            }
        });
    }


    @FXML
    private void handleSearch() {
        String searchTitle = txtTitle.getText().trim();
        String searchAuthor = txtAuthor.getText().trim();

        if (searchTitle.isEmpty() && searchAuthor.isEmpty()) {
            loadBookData();
            return;
        }

        try {
            ObservableList<Book> searchResults = MySQLDatabase.getBookDatabase().getBooksByNameOrAuthor(searchTitle, searchAuthor);

            if (!searchResults.isEmpty()) {
                bookList.clear();
                bookList.addAll(searchResults);
                bookTable.setItems(bookList);
            } else {
                bookList.clear();
                bookTable.setItems(bookList);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void handleDeleteBook(Book book) {
        if (book != null) {
            try {
                boolean confirmed = ConfirmDialog.show("Xác nhận xóa",
                        "Bạn có chắc chắn muốn xóa sách này?");
                if (!confirmed) {
                    return;
                }

                
                MySQLDatabase.getBookDatabase().deleteBook(book.getId());

                
                bookList.remove(book);

                
                bookTable.setItems(bookList);

                System.out.println("Người đọc đã bị xóa: " + book.getTitle());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
    public void showBookView() {
        loadPage("book-view.fxml");
    }

}