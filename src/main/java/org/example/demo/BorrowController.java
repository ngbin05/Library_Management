package org.example.demo;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BorrowController {
    private Stage stage;
    private static List<Book> cart = new ArrayList<>(); // Giỏ sách mượn

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private ListView<Book> listViewBooks; // ListView hiển thị sách

    @FXML
    private Button clearButton; // Nút "Xóa toàn bộ"

    @FXML
    private Label nameLabel; // Label hiển thị tên người mượn (nếu cần)

    @FXML
    private Label idLabel; // Label hiển thị ID người mượn (nếu cần)


    @FXML
    private Label borrowDateLabel;
    @FXML
    private TextField borrowDaysField;
    @FXML
    private Label returnDateLabel;

    @FXML
    private Label unBookErrorLabel;

    @FXML
    private Label unDateErrorLabel;

    @FXML
    private Label borrowSuccessLabel;


    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Phương thức khởi tạo (initialize) cho Controller
    @FXML
    public void initialize() {
        unBookErrorLabel.setVisible(false);
        unDateErrorLabel.setVisible(false);
        borrowSuccessLabel.setVisible(false);
        // Cài đặt CellFactory để tùy chỉnh giao diện ListView
        listViewBooks.setCellFactory(listView -> new BookListCell());

        // Gán hành động cho nút "Xóa toàn bộ"
        clearButton.setOnAction(event -> {
            cart.clear();
            listViewBooks.getItems().clear();
            System.out.println("Đã xóa toàn bộ giỏ sách!");
        });

        // Hiển thị danh sách sách ban đầu (nếu có)
        updateListView();


        // Lấy ngày hiện tại và hiển thị vào borrowDateLabel
        // Lấy ngày hiện tại và hiển thị vào borrowDateLabel
        LocalDate today = LocalDate.now();
        borrowDateLabel.setText("From: " + today.format(formatter));

        // Thêm ChangeListener cho borrowDaysField
        borrowDaysField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateReturnDate();
            }
        });
    }

    public void setNameLabel(String name) {
            nameLabel.setText("Tên: " + name);
    }

    // Hàm set cho ID người mượn
    public void setIdLabel(int id) {
            idLabel.setText("ID: " + Integer.toString(id));
    }

    private static boolean isBookInCart(Book book) {
        return cart.stream().anyMatch(b -> b.getId() == book.getId());
    }

    // Thêm sách vào giỏ
    public static boolean addBookIfNotInCart(Book book) {
        if (!isBookInCart(book)) {
            cart.add(book);
            return true; // Thêm thành công
        }
        return false; // Sách đã tồn tại
    }

    // Xóa sách khỏi giỏ
    public static void remove(Book book) {
        cart.remove(book);
    }

    // Cập nhật ListView để hiển thị giỏ sách
    private void updateListView() {
        listViewBooks.getItems().setAll(cart);
    }

    // Lớp tùy chỉnh giao diện cho từng mục trong ListView
    private static class BookListCell extends javafx.scene.control.ListCell<Book> {
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

    private void updateReturnDate() {
        try {
            // Lấy số ngày mượn từ borrowDaysField
            int borrowDays = Integer.parseInt(borrowDaysField.getText());

            // Lấy ngày mượn từ borrowDateLabel và chuyển thành LocalDate
            LocalDate borrowDate = LocalDate.parse(borrowDateLabel.getText().split(": ")[1], formatter);

            // Tính ngày trả bằng cách cộng số ngày mượn
            LocalDate returnDate = borrowDate.plusDays(borrowDays);

            // Hiển thị ngày trả vào returnDateLabel với định dạng mong muốn
            returnDateLabel.setText("To: " + returnDate.format(formatter));

        } catch (NumberFormatException e) {
            // Nếu người dùng nhập không hợp lệ, hiển thị thông báo lỗi
            returnDateLabel.setText("To: ");
        }
    }

    @FXML
    public void borrow() {

        // Kiểm tra giỏ sách có trống không
        if (cart.isEmpty()) {
            unBookErrorLabel.setVisible(true);
            unDateErrorLabel.setVisible(false);
            borrowSuccessLabel.setVisible(false);
            return;
        }
        // Lấy thông tin người mượn từ giao diện
        int userId = Integer.parseInt(idLabel.getText().split(": ")[1]); // Lấy ID người mượn từ Label

        String[] dateParts = returnDateLabel.getText().split(": ");
        if (dateParts.length < 2) {
            unDateErrorLabel.setVisible(true);
            unBookErrorLabel.setVisible(false);
            return;
        }
        String borrowDate = borrowDateLabel.getText().split(": ")[1]; // Ngày mượn từ borrowDateLabel
        String returnDate = returnDateLabel.getText().split(": ")[1]; // Ngày trả từ returnDateLabel



        // Chuyển đổi String ngày mượn và trả thành đối tượng Date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate borrowLocalDate = LocalDate.parse(borrowDate, formatter);
        LocalDate returnLocalDate = LocalDate.parse(returnDate, formatter);

        if (returnLocalDate.isBefore(borrowLocalDate)) {
            unDateErrorLabel.setVisible(true);
            unBookErrorLabel.setVisible(false);
            borrowSuccessLabel.setVisible(false);
            return;
        }

        // Chuyển đổi thành Date
        java.sql.Date sqlBorrowDate = java.sql.Date.valueOf(borrowLocalDate);
        java.sql.Date sqlReturnDate = java.sql.Date.valueOf(returnLocalDate);



        // Chuyển danh sách cart thành danh sách các bookIds (chỉ lấy ID của sách)
        List<Integer> bookIds = new ArrayList<>();
        for (Book book : cart) {
            bookIds.add(book.getId());
        }

        // Thực hiện lưu thông tin mượn vào cơ sở dữ liệu
            // Gọi phương thức insertBorrowRecord từ lớp Database
            boolean success = Database.insertBorrowRecord(userId, bookIds, sqlBorrowDate, sqlReturnDate);

            if (success) {
                // Nếu mượn thành công, làm sạch giỏ sách và cập nhật ListView
                borrowSuccessLabel.setVisible(true);
                unBookErrorLabel.setVisible(false);
                unDateErrorLabel.setVisible(false);
                cart.clear();
                updateListView();

            } else {
                return;
            }
        }



}
