package org.example.demo;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private Label readersCount;

    @FXML
    private Label booksCount;

    @FXML
    private Label booksBorrowCount;

    @FXML
    private Label hi;

    @FXML
    private Button exitButton;

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
    public void showDashBoard() { loadPage("dashboard-view.fxml"); }

    @FXML
    public void showCartView() { loadPage("cart-view.fxml"); }


    @FXML
    private TableView<Borrowed> borrowTableView;

    @FXML
    private TableColumn<Borrowed, Integer> userIdColumn;

    @FXML
    private TableColumn<Borrowed, Integer> userNameColumn;

    @FXML
    private TableColumn<Borrowed, Integer> bookCountColumn; // Cột cho tên các cuốn sách

    @FXML
    private TableColumn<Borrowed, String> borrowDateColumn;

    @FXML
    private TableColumn<Borrowed, String> returnDateColumn;

    @FXML
    private TableColumn<Borrowed, String> statusColumn;

    @FXML
    private TableColumn<Borrowed, Void> actionColumn;



    // ObservableList chứa các đối tượng Borrowed
    private ObservableList<Borrowed> borrowList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        pane.setVisible(false);
        sayHi();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            LocalDateTime now = LocalDateTime.now();
            dateTimeLabel.setText(now.format(formatter));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        exitButton.setOnAction(event -> {
            Platform.exit();
        });


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

        statusColumn.setCellFactory(column -> new TableCell<Borrowed, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);

                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    if ("BORROWING".equalsIgnoreCase(status)) {
                        setStyle("-fx-text-fill: #f2ce03; -fx-font-weight: bold;");
                    } else if ("RETURNED".equalsIgnoreCase(status)) {
                        setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    } else if ("OVERDUE".equalsIgnoreCase(status)) {
                        setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    }
                }
            }
        });


        // Tùy chỉnh cột bookTitlesColumn để hiển thị danh sách sách với số thứ tự và xuống dòng
        bookCountColumn.setCellValueFactory(param -> {
            Borrowed borrowed = param.getValue();
            return new javafx.beans.property.SimpleIntegerProperty(borrowed.getBooks().size()).asObject();
        });

        actionColumn.setCellFactory(column -> {
            return new TableCell<Borrowed, Void>() {
                private final javafx.scene.control.Button returnButton = new javafx.scene.control.Button("Trả sách");
                private final javafx.scene.control.Label checkMarkLabel = new javafx.scene.control.Label("✔");

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null); // Không hiển thị gì nếu ô trống
                    } else {
                        Borrowed borrowed = getTableView().getItems().get(getIndex());

                        // So sánh chuỗi bằng phương thức equals()
                        if ("RETURNED".equals(borrowed.getStatus())) {
                            returnButton.setVisible(false);
                            checkMarkLabel.setVisible(true);
                            setGraphic(checkMarkLabel); // Hiển thị dấu tích xanh
                        } else {
                            returnButton.setVisible(true);
                            checkMarkLabel.setVisible(false);
                            setGraphic(returnButton); // Hiển thị nút "Trả sách"

                            // Thiết lập sự kiện cho nút "Trả sách"
                            returnButton.setOnAction(event -> {
                                boolean isReturned = handleReturnBook(borrowed); // Gọi hàm xử lý trả sách
                                if (isReturned) {
                                    // Sau khi trả sách thành công, thay đổi nút "Trả sách" thành dấu tích xanh
                                    returnButton.setVisible(false); // Ẩn nút "Trả sách"
                                    checkMarkLabel.setVisible(true); // Hiển thị dấu tích xanh
                                    borrowed.setStatus("RETURNED"); // Cập nhật trạng thái của đối tượng Borrowed
                                    // Cập nhật lại TableView sau khi thay đổi trạng thái
                                    borrowTableView.refresh();
                                }
                            });
                        }
                    }
                }
            };
        });


        // Đưa danh sách vào TableView
        borrowTableView.setItems(borrowList);
        borrowTableView.setFixedCellSize(-1); // Cho phép tự động điều chỉnh chiều cao dòng


        int userCount = Database.countUsers();
        readersCount.setText(String.valueOf(userCount));

        int bookCount = Database.countBooks();
        booksCount.setText(String.valueOf(bookCount));

        int bookBorrowCount = Database.countBooksBorrow();
        booksBorrowCount.setText(String.valueOf(bookBorrowCount));

    }

    private void sayHi() {
        String userName = Database.getFullName(LoginController.account.getUsername());
        if (userName != null) {
            hi.setText("Hi, " + userName);
        }
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

    private boolean handleReturnBook(Borrowed borrowed) {
        try {
            boolean confirmed = ConfirmDialog.show("Xác nhận", "Xác nhận trả sách?");
            if (!confirmed) {
                return false;  // Nếu không xác nhận, trả về false
            }

            // Trả sách vào cơ sở dữ liệu
            boolean isReturned = Database.returnBook(borrowed.getBorrowId(), borrowed.getBooks());
            if (isReturned) {
                System.out.println("Trả sách thành công cho Borrow ID: " + borrowed.getBorrowId());
                // Cập nhật lại dữ liệu sau khi trả sách
                loadBorrowData();
                borrowTableView.refresh(); // Cập nhật TableView để hiển thị sự thay đổi
                return true; // Trả về true nếu trả sách thành công
            } else {
                System.out.println("Trả sách thất bại!");
                return false; // Trả về false nếu trả sách thất bại
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Trả về false nếu có ngoại lệ xảy ra
        }
    }
}

