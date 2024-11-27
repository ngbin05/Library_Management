package org.example.demo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ListBookController {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

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

    private ObservableList<Book> bookList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        rectangle.setVisible(false);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colPublishedDate.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));


        addButtonToTable();
        loadBookData();

        bookTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                openBookDetailsWindow(newValue);
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

    private void loadBookData() {
        try {
            bookList.clear();
            bookList.addAll(Database.getBooks());
            bookTable.setItems(bookList); // Gắn dữ liệu vào TableView
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openBookDetailsWindow(Book book) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("bookDetail-view.fxml"));
            Parent bookDetailsParent = fxmlLoader.load();  // Load FXML cho cửa sổ chi tiết sách
            BookDetailsController controller = fxmlLoader.getController();
            Scene bookDetailsScene = new Scene(bookDetailsParent);
            Stage bookDetailsStage = new Stage();
            bookDetailsStage.initModality(Modality.APPLICATION_MODAL);  // Đảm bảo cửa sổ này là modal
            bookDetailsStage.initOwner(stage);
            bookDetailsScene.setFill(Color.TRANSPARENT);
            bookDetailsStage.initStyle(StageStyle.TRANSPARENT);
            controller.setStage(bookDetailsStage);
            Platform.runLater(() ->
            {
                double mainStageX = stage.getX();
                double mainStageY = stage.getY();
                bookDetailsStage.show();
                double x = mainStageX + stage.getWidth() - rectangle.getWidth();
                double y = mainStageY + stage.getHeight() - rectangle.getHeight();
                bookDetailsStage.setX(x);
                bookDetailsStage.setY(y);
            });

            // Hiển thị thông tin chi tiết sách
            controller.displayBookDetails(book);

            bookDetailsStage.setScene(bookDetailsScene);
            bookDetailsStage.show();
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
            ObservableList<Book> searchResults = Database.getBooksByNameOrAuthor(searchTitle, searchAuthor);

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

                // Gọi phương thức xóa trong Database
                Database.deleteBook(book.getId());

                // Xóa khỏi danh sách hiển thị
                bookList.remove(book);

                // Cập nhật lại bảng
                bookTable.setItems(bookList);

                System.out.println("Người đọc đã bị xóa: " + book.getTitle());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleAddBook() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("book-view.fxml"));
            Parent addBookParent = fxmlLoader.load();  // Load FXML cho cửa sổ Add Reader
            BookController controller = fxmlLoader.getController();
            Scene addReaderScene = new Scene(addBookParent);  // Thay bằng FXML tương ứng
            Stage addBookStage = new Stage();
            addBookStage.initModality(Modality.APPLICATION_MODAL);  // Đảm bảo cửa sổ này là modal
            addBookStage.initOwner(stage);
            addBookStage.initStyle(StageStyle.TRANSPARENT);
            rectangle.setVisible(true);
            addReaderScene.setFill(Color.TRANSPARENT);
            addBookStage.setScene(addReaderScene);
            controller.setStage(addBookStage);
            Platform.runLater(() ->
            {
                double mainStageX = stage.getX();
                double mainStageY = stage.getY();
                addBookStage.show();
                double x = mainStageX + stage.getWidth() - rectangle.getWidth();
                double y = mainStageY + stage.getHeight() - rectangle.getHeight();
                addBookStage.setX(x);
                addBookStage.setY(y);


                addBookStage.setOnHidden(e -> rectangle.setVisible(false));
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}