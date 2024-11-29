package org.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.SQLException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ManageBorrowingController {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private TableView<Borrowed> borrowTableView;

    @FXML
    private TableColumn<Borrowed, Integer> borrowIdColumn;

    @FXML
    private TableColumn<Borrowed, Integer> userIdColumn;

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
        // Kết nối với CSDL và lấy dữ liệu
        loadBorrowData();

        // Cấu hình các cột trong TableView
        borrowIdColumn.setCellValueFactory(new PropertyValueFactory<>("borrowId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Tùy chỉnh cột bookTitlesColumn để hiển thị danh sách sách với số thứ tự và xuống dòng
        bookTitlesColumn.setCellFactory(column -> {
            return new TableCell<>() {
                private final Text text = new Text();

                {
                    text.setWrappingWidth(300); // Đảm bảo rằng tên sách có thể xuống dòng
                    text.setStyle("-fx-text-alignment: justify;"); // Căn lề nếu cần
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        Borrowed borrowed = getTableView().getItems().get(getIndex());
                        List<String> bookTitles = borrowed.getBookTitles();

                        // Tạo danh sách sách với số thứ tự
                        StringBuilder formattedTitles = new StringBuilder();
                        for (int i = 0; i < bookTitles.size(); i++) {
                            formattedTitles.append(i + 1).append(". ").append(bookTitles.get(i)).append("\n");
                        }

                        text.setText(formattedTitles.toString());
                        setGraphic(text); // Hiển thị đối tượng Text
                    }
                }
            };
        });

        // Đưa danh sách vào TableView
        borrowTableView.setItems(borrowList);
        borrowTableView.setFixedCellSize(-1); // Cho phép tự động điều chỉnh chiều cao dòng
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
