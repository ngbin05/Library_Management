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
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ListReaderController {
    private Stage stage;

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
    private Button btnAdd;

    @FXML
    private TextField txtSearchID;

    @FXML
    private TextField txtSearchName;

    @FXML
    private TextField txtSearchPhone;

    @FXML
    private TextField txtSearchCCCD;

    @FXML
    private Rectangle rectangle;

    @FXML
    private Label dateTimeLabel;

    @FXML
    private Label hi;


    // ObservableList chứa danh sách khách hàng
    private ObservableList<Customer> customerList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Gắn cột với thuộc tính của lớp Customer
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCCCD.setCellValueFactory(new PropertyValueFactory<>("cccd"));


        addButtonToTable();
        loadCustomerData();
        sayHi();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            LocalDateTime now = LocalDateTime.now();
            dateTimeLabel.setText(now.format(formatter));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        btnAdd.setOnAction(event -> handleAddReader());
    }

    private void sayHi() {
        String userName = Database.getFullName(LoginController.account.getUsername());
        if (userName != null) {
            hi.setText("Hi, " + userName);
        }
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
    private void handleAddReader() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-reader-view.fxml"));
            Parent addReaderParent = fxmlLoader.load();  // Load FXML cho cửa sổ Add Reader
            AddReaderController controller = fxmlLoader.getController();
            Scene addReaderScene = new Scene(addReaderParent,557, 362);  // Thay bằng FXML tương ứng
            Stage addReaderStage = new Stage();
            addReaderStage.initModality(Modality.APPLICATION_MODAL);  // Đảm bảo cửa sổ này là modal
            addReaderStage.initOwner(stage);
            addReaderScene.setFill(Color.TRANSPARENT);
            addReaderStage.initStyle(StageStyle.TRANSPARENT);
            addReaderStage.setScene(addReaderScene);
            controller.setStage(addReaderStage);
            Platform.runLater(() ->
            {
            double mainStageX = stage.getX();
            double mainStageY = stage.getY();
            double mainStageWidth = stage.getWidth();
            double mainStageHeight = stage.getHeight();
            addReaderStage.show();
            double x = mainStageX + 315;
            double y = mainStageY + (mainStageHeight - addReaderStage.getHeight()) / 2;
            addReaderStage.setX(x);
            addReaderStage.setY(y);


            });

        } catch (Exception e) {
            e.printStackTrace();
        }
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
            private final Button btnDelete = new Button("Xóa");

            {

                // Nút Xóa
                Image trashIcon = new Image(getClass().getResourceAsStream("/media/trash.png"));
                ImageView trashImageView = new ImageView(trashIcon);
                trashImageView.setFitWidth(20);
                trashImageView.setFitHeight(20);
                btnDelete.setGraphic(trashImageView);
                btnDelete.getStylesheets().add((getClass().getResource("/CustomerStyle/deleteButton-style.css")).toExternalForm());
                btnDelete.setOnAction(event -> {
                    Customer customer = getTableView().getItems().get(getIndex());
                    handleDeleteCustomer(customer);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttonBox = new HBox(10, btnDelete);  // Đặt các nút trong một HBox
                    setGraphic(buttonBox);  // Hiển thị các nút trong mỗi ô của cột Action
                }
            }
        });
    }


    private void handleDeleteCustomer(Customer customer) {
        if (customer != null) {
            try {
                boolean confirmed = ConfirmDialog.show("Xác nhận xóa",
                        "Bạn có chắc chắn muốn xóa người đọc này?");
                if (!confirmed) {
                    return;
                }

                // Gọi phương thức xóa trong Database
                Database.deleteCustomer(customer.getId());

                // Xóa khỏi danh sách hiển thị
                customerList.remove(customer);

                // Cập nhật lại bảng
                customerTable.setItems(customerList);

                System.out.println("Người đọc đã bị xóa: " + customer.getName());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleBorrowCustomer(Customer customer) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("borrow-view.fxml"));
            Parent parent = fxmlLoader.load();  // Load FXML cho cửa sổ chi tiết sách
            BorrowController controller = fxmlLoader.getController();
            Scene borrowScene = new Scene(parent);
            Stage borrowStage = new Stage();
//            borrowStage.initModality(Modality.APPLICATION_MODAL);  // Đảm bảo cửa sổ này là modal
            borrowStage.initOwner(stage);
            borrowScene.setFill(Color.TRANSPARENT);
            borrowStage.initStyle(StageStyle.TRANSPARENT);
            controller.setStage(borrowStage);
            Platform.runLater(() ->
            {
                double mainStageX = stage.getX();
                double mainStageY = stage.getY();
                borrowStage.show();
                double x = mainStageX + stage.getWidth() - 795;
                double y = mainStageY + stage.getHeight() - 600;
                borrowStage.setX(x);
                borrowStage.setY(y);
            });

            // Hiển thị thông tin chi tiết sách
            controller.setIdLabel(customer.getId());
            controller.setNameLabel(customer.getName());

            borrowStage.setScene(borrowScene);
            borrowStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
