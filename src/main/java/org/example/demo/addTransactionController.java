package org.example.demo;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import javax.imageio.IIOParam;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class addTransactionController {
    private Stage stage;

    @FXML
    private Pane pane;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, Integer> colId;

    @FXML
    private TableColumn<Customer, String> colName;

    @FXML
    private TableColumn<Customer, String> colPhone;

    @FXML
    private TableColumn<Customer, String> colAddress;

    @FXML
    private TableColumn<Customer, String> colCCCD;

    @FXML
    private TableColumn<Customer, Void> colAction;

    @FXML
    private TextField txtSearchID;

    @FXML
    private TextField txtSearchName;

    @FXML
    private TextField txtSearchPhone;

    @FXML
    private TextField txtSearchCCCD;

    @FXML
    public void back() { loadPage("cart-view.fxml"); }

    // ObservableList chứa danh sách khách hàng
    private ObservableList<Customer> customerList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        pane.setVisible(false);
        // Gắn cột với thuộc tính của lớp Customer
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCCCD.setCellValueFactory(new PropertyValueFactory<>("cccd"));


        loadCustomerData();
        addButtonToTable();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    }


    private void loadCustomerData() {
        try {
            customerList.clear();
            customerList.addAll(Database.getCustomers());
            customerTable.setItems(customerList); // Gắn dữ liệu vào TableView
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleSearch() {
        // Lấy giá trị từ các ô tìm kiếm
        String searchID = txtSearchID.getText().trim();
        String searchName = txtSearchName.getText().trim();
        String searchPhone = txtSearchPhone.getText().trim();
        String searchCCCD = txtSearchCCCD.getText().trim();

        // Nếu tất cả các ô tìm kiếm đều trống, tải lại toàn bộ danh sách
        if (searchID.isEmpty() && searchName.isEmpty() && searchPhone.isEmpty() && searchCCCD.isEmpty()) {
            loadCustomerData();  // Load lại toàn bộ danh sách
            return;
        }

        try {
            ObservableList<Customer> searchResults = Database.getUsersByMultipleSearchTerms(searchID, searchName, searchPhone, searchCCCD);

            if (!searchResults.isEmpty()) {
                customerList.clear();
                customerList.addAll(searchResults);  // Thêm kết quả tìm kiếm vào danh sách
                customerTable.setItems(customerList);  // Cập nhật TableView
            } else {
                customerList.clear();  // Nếu không tìm thấy, làm trống TableView
                customerTable.setItems(customerList);
                // Có thể thêm thông báo cho người dùng nếu cần
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Thêm thông báo lỗi cho người dùng nếu có sự cố trong truy vấn SQL
        }
    }

    private void addButtonToTable() {
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button btnBorrow = new Button("Add new");
            {
                // Nút Mượn
                btnBorrow.setOnAction(event -> {
                    Customer customer = getTableView().getItems().get(getIndex());
                    handleBorrowCustomer(customer);  // Bạn sẽ cần định nghĩa hàm này
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttonBox = new HBox(10, btnBorrow);  // Đặt các nút trong một HBox
                    setGraphic(buttonBox);  // Hiển thị các nút trong mỗi ô của cột Action
                }
            }
        });
    }


    private void handleBorrowCustomer(Customer customer) {
        try {
            // Load FXML cho BorrowController
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("borrow-view.fxml"));
            Parent root = fxmlLoader.load();

            // Hiển thị giao diện
            pane.setVisible(true);
            pane.getChildren().clear();
            pane.getChildren().setAll(root);
            pane.setBackground(new Background(new BackgroundFill(Color.web("F4F4F4"), null, null)));

            // Hiệu ứng mờ dần
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), root);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setToValue(1.0);
            fadeTransition.play();

            // Lấy controller từ FXML
            Object controller = fxmlLoader.getController();
            if (controller instanceof BorrowController borrowController) {
                // Thiết lập các giá trị cho BorrowController
                borrowController.setStage((Stage) pane.getScene().getWindow());
                borrowController.setIdLabel(customer.getId());
                borrowController.setNameLabel(customer.getName());
            }
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
                    CartController cartController = (CartController) controller;
                    cartController.setStage((Stage) pane.getScene().getWindow());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}