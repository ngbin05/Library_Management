package org.example.demo;

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
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.SQLException;

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

        btnAdd.setOnAction(event -> handleAddReader());
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
            addReaderStage.initStyle(StageStyle.TRANSPARENT);
            rectangle.setVisible(true);
            addReaderStage.setScene(addReaderScene);
            controller.setStage(addReaderStage);
            Platform.runLater(() ->
            {
            double mainStageX = stage.getX();
            double mainStageY = stage.getY();
            double mainStageWidth = stage.getWidth();
            double mainStageHeight = stage.getHeight();
            addReaderStage.show();
            double x = mainStageX + (mainStageWidth - addReaderStage.getWidth()) / 2;
            double y = mainStageY + (mainStageHeight - addReaderStage.getHeight()) / 2;
            addReaderStage.setX(x);
            addReaderStage.setY(y);


            addReaderStage.setOnHidden(e -> rectangle.setVisible(false));
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

                Image trashIcon = new Image(getClass().getResourceAsStream("/media/trash.png"));
                ImageView imageView = new ImageView(trashIcon);
                imageView.setFitWidth(20);
                imageView.setFitHeight(20);

                btnDelete.setGraphic(imageView);
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
                    setGraphic(btnDelete);
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





}
