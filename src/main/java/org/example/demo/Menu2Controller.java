package org.example.demo;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Menu2Controller {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private Pane pane;

    @FXML
    private Label dateTimeLabel;

    @FXML
    public void showReaderList(){
        loadPage("readers-view.fxml");
    }
    @FXML
    public void showBookList() {
        loadPage("bookList-view.fxml");
    }

    @FXML
    public void showProfile() { loadPage("profile-view.fxml"); }

    @FXML
    public void showBorrowMenu() { loadPage("borrow-view.fxml"); }


    @FXML
    private TableView<Borrowed> borrowTableView;

    @FXML
    private TableColumn<Borrowed, Integer> userIdColumn;

    @FXML
    private TableColumn<Borrowed, Integer> userNameColumn;

    @FXML
    private TableColumn<Borrowed, String> bookTitlesColumn; // Cột cho tên các cuốn sách

    @FXML
    private TableColumn<Borrowed, String> borrowDateColumn;

    @FXML
    private TableColumn<Borrowed, String> returnDateColumn;

    @FXML
    private TableColumn<Borrowed, String> statusColumn;

    // ObservableList chứa các đối tượng Borrowed
    private ObservableList<Borrowed> borrowList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        pane.setVisible(false);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            LocalDateTime now = LocalDateTime.now();
            dateTimeLabel.setText(now.format(formatter));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();



        try {
            Database.updateOverdueStatus();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Kết nối với CSDL và lấy dữ liệu
        loadBorrowData();

        // Cấu hình các cột trong TableView
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("user_name"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Tùy chỉnh cột bookTitlesColumn để hiển thị danh sách sách với số thứ tự và xuống dòng
        bookTitlesColumn.setCellFactory(column -> {
            return new TableCell<>() {
                private final Text text = new Text();

                {
                    text.setWrappingWidth(300); // Ensure the text wraps within the given width
                    text.setStyle("-fx-text-alignment: justify;"); // Align text to justify (optional)
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setGraphic(null); // No content if empty
                    } else {
                        // Get the current row's Borrowed object
                        Borrowed borrowed = getTableView().getItems().get(getIndex());
                        List<String> bookTitles = borrowed.getBookTitles();

                        // Format the list of book titles with numbering
                        StringBuilder formattedTitles = new StringBuilder();
                        for (int i = 0; i < bookTitles.size(); i++) {
                            formattedTitles.append(i + 1).append(". ").append(bookTitles.get(i)).append("\n");
                        }

                        // Update the Text object with the formatted titles
                        text.setText(formattedTitles.toString().trim()); // Optional: remove extra newline at the end
                        setGraphic(text); // Set the Text as the graphic for the TableCell
                    }
                }
            };
        });


        // Đưa danh sách vào TableView
        borrowTableView.setItems(borrowList);
        borrowTableView.setFixedCellSize(-1); // Cho phép tự động điều chỉnh chiều cao dòng



    }


    @FXML
    private void logOut() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Scene loginScene = new Scene(loader.load());
            loginScene.setFill(Color.TRANSPARENT);
            stage.setScene(loginScene);
            LoginController loginController = loader.getController();
            loginController.setStage(stage);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPage(String fxmlFileName) {
        try {
            // Load FXML
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

            // Xử lý controller
            Object controller = loader.getController();
            if (controller != null) {
                if (controller instanceof ListReaderController) {
                    ListReaderController listReaderController = (ListReaderController) controller;
                    listReaderController.setStage((Stage) pane.getScene().getWindow());
                } else if (controller instanceof ListBookController) {
                    ListBookController listBookController = (ListBookController) controller;
                    listBookController.setStage((Stage) pane.getScene().getWindow());
                } else if (controller instanceof ProfileController) {
                    ProfileController profileController = (ProfileController) controller;
                    profileController.setStage((Stage) pane.getScene().getWindow());
                } else if (controller instanceof BorrowController) {
                    BorrowController borrowController = (BorrowController) controller;
                    borrowController.setStage((Stage) pane.getScene().getWindow());
                }
                // Thêm các controller khác vào đây nếu cần
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Hàm để lấy dữ liệu từ cơ sở dữ liệu
    private void loadBorrowData() {
        try {
            // Gọi phương thức lấy tất cả các lượt mượn từ cơ sở dữ liệu
            List<Borrowed> borrowDataList = Database.getAllBorrowData();

            // Thêm vào ObservableList để hiển thị trên TableView
            this.borrowList.clear();
            this.borrowList.addAll(borrowDataList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}

