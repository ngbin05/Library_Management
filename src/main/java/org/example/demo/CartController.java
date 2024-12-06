package org.example.demo;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartController {

    private BorrowController borrowController;

    // Phương thức setBorrowController để truyền BorrowController vào
    public void setBorrowController(BorrowController borrowController) {
        this.borrowController = borrowController;
    }
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private Pane pane;

    @FXML
    private Label emptyLabel;

    @FXML
    public void showBookList() {
        loadPage("bookList-view.fxml");
    }

    @FXML
    public void showReaderList() {
        if (BookCartManager.getCart().isEmpty()) {
            emptyLabel.setVisible(true);
        } else {
            loadPage("addTransaction-view.fxml");
        }
    }

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

    @FXML
    private TextField txtSearchID;

    @FXML
    private TextField txtSearchUserName;

    @FXML
    private TextField txtSearchLoanDate;

    @FXML
    private TextField txtSearchStatus;

    private static List<Book> cart = new ArrayList<>();

    @FXML
    private ListView<Book> listViewBooks;

    @FXML
    private Button clearButton;



    // ObservableList chứa các đối tượng Borrowed
    private ObservableList<Borrowed> borrowList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        listViewBooks.setCellFactory(listView -> new BookListCell());
        emptyLabel.setVisible(false);

        // Cấu hình các cột trong TableView
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("user_name"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

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

        clearButton.setOnAction(event -> {
            cart.clear();
            listViewBooks.getItems().clear();
            System.out.println("Đã xóa toàn bộ giỏ sách!");
        });


        // Đưa danh sách vào TableView
        borrowTableView.setItems(borrowList);
        borrowTableView.setFixedCellSize(-1);

        loadBorrowData();
        updateListView();
    }

    private static boolean isBookInCart(Book book) {
        return cart.stream().anyMatch(b -> b.getId() == book.getId());
    }

    public static boolean addBookIfNotInCart(Book book) {
        return BookCartManager.addBookIfNotInCart(book);
    }

    public static void remove(Book book) {
        BookCartManager.removeBook(book);
    }

    private void updateListView() {
        listViewBooks.getItems().setAll(BookCartManager.getCart());
    }

    static class BookListCell extends javafx.scene.control.ListCell<Book> {
        private final HBox content = new HBox(10); // HBox chứa các thành phần
        private final ImageView bookImage = new ImageView(); // Ảnh sách
        private final VBox details = new VBox(); // VBox chứa thông tin sách
        private final Button removeButton = new Button("Xóa"); // Nút "Xóa"

        public BookListCell() {
            // Cài đặt giao diện
            bookImage.setFitWidth(50);
            bookImage.setFitHeight(50);

            // Tạo sự kiện cho nút "Xóa"
            removeButton.setStyle("-fx-background-color: #FF6B6B; -fx-text-fill: white; -fx-font-size: 14px; -fx-shape: \"M 0,0 L 1,0 L 1,1 L 0,1 Z\"; -fx-background-radius: 15px; -fx-border-radius: 15px;");
            removeButton.setOnAction(event -> {
                Book book = getItem();
                if (book != null) {
                    BorrowController.remove(book); // Xóa sách khỏi giỏ
                    getListView().getItems().remove(book); // Xóa khỏi giao diện
                }
            });

            // Sắp xếp các phần tử
            content.getChildren().addAll(bookImage, details, removeButton);
        }

        @Override
        protected void updateItem(Book book, boolean empty) {
            super.updateItem(book, empty);

            if (empty || book == null) {
                setGraphic(null);
            } else {
                // Hiển thị ảnh sách
                if (book.getImage() != null) {
                    bookImage.setImage(new Image(new ByteArrayInputStream(book.getImage())));
                } else {
                    bookImage.setImage(null); // Ảnh mặc định nếu không có
                }

                // Hiển thị thông tin sách
                details.getChildren().setAll(
                        new Text("Tên: " + book.getTitle()),
                        new Text("Tác giả: " + book.getAuthor()),
                        new Text("Nhà xuất bản: " + book.getPublisher()),
                        new Text("Ngày xuất bản: " + book.getPublishedDate())
                );

                setGraphic(content);
            }
        }
    }

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



    @FXML
    private void handleSearchBorrowRecords() {
        // Lấy thông tin từ các TextField
        String borrowId = txtSearchID.getText().trim();
        String userName = txtSearchUserName.getText().trim();
        String loanDate = txtSearchLoanDate.getText().trim();
        String status = txtSearchStatus.getText().trim();

        try {
            ObservableList<Borrowed> searchResults;

            // Kiểm tra nếu tất cả các trường tìm kiếm đều trống
            if (borrowId.isEmpty() && userName.isEmpty() && loanDate.isEmpty() && status.isEmpty()) {
                // Gọi hàm getAllBorrowData để lấy toàn bộ dữ liệu
                searchResults = FXCollections.observableArrayList(Database.getAllBorrowData());
            } else {
                // Gọi hàm tìm kiếm với các tham số
                searchResults = Database.searchBorrowRecords(borrowId, userName, loanDate, status);
            }

            // Cập nhật TableView với kết quả tìm kiếm
            borrowTableView.setItems(searchResults);

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }



}