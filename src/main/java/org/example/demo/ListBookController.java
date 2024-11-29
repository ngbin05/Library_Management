package org.example.demo;

import javafx.animation.FadeTransition;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
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

    @FXML
    private Pane pane;

    private ObservableList<Book> bookList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        rectangle.setVisible(false);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colPublishedDate.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        pane.setVisible(false);


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
            controller.setBook(book);

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

//    @FXML
//    private void handleAddBook() {
//        try {
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("book-view.fxml"));
//            Parent addBookParent = fxmlLoader.load(); // Load FXML cho cửa sổ thêm sách
//            BookController controller = fxmlLoader.getController();
//
//            // Tạo Scene cho cửa sổ phụ
//            Scene addBookScene = new Scene(addBookParent);
//            Stage addBookStage = new Stage();
//
//            // Đảm bảo cửa sổ không có viền
//            addBookStage.initModality(Modality.APPLICATION_MODAL); // Đặt cửa sổ phụ là modal
//            addBookStage.initOwner(stage); // Gắn cửa sổ phụ vào cửa sổ chính
//            addBookStage.initStyle(StageStyle.UNDECORATED); // Bỏ viền cửa sổ
//            addBookScene.setFill(Color.TRANSPARENT); // Đảm bảo nền trong suốt nếu cần
//
//            // Gắn Scene vào Stage của cửa sổ phụ
//            addBookStage.setScene(addBookScene);
//
//            // Truyền Stage cho controller của cửa sổ phụ
//            controller.setStage(addBookStage);
//
//            // Tính toán kích thước và vị trí cho cửa sổ phụ để khớp với màn hình chính
//            Platform.runLater(() -> {
//                double mainStageX = stage.getX(); // Vị trí X của cửa sổ chính
//                double mainStageY = stage.getY(); // Vị trí Y của cửa sổ chính
//                double mainStageWidth = stage.getWidth(); // Chiều rộng cửa sổ chính
//                double addBookStageWidth = 800; // Chiều rộng của cửa sổ phụ
//                double addBookStageHeight = 600; // Chiều cao của cửa sổ phụ
//
//                // Tính toán vị trí cửa sổ phụ sát vào bên phải cửa sổ chính
//                double newStageX = mainStageX + mainStageWidth - addBookStageWidth;
//                double newStageY = mainStageY;
//
//                // Đặt vị trí và kích thước cho cửa sổ phụ
//                addBookStage.setX(newStageX);
//                addBookStage.setY(newStageY);
//                addBookStage.setWidth(addBookStageWidth);
//                addBookStage.setHeight(addBookStageHeight);
//
//                // Hiển thị cửa sổ phụ
//                addBookStage.show();
//
//                // Xử lý sự kiện khi đóng cửa sổ phụ
//                addBookStage.setOnHidden(e -> rectangle.setVisible(false));
//            });
//
//            // Hiển thị hiệu ứng Rectangle để báo hiệu màn hình chuyển
//            rectangle.setVisible(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

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
    public void showBookView() { loadPage("book-view.fxml"); }

}