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


    
    private final ObservableList<Customer> customerList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        
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
        String userName = MySQLDatabase.getUserDatabase().getFullName(LoginController.account.getUsername());
        if (userName != null) {
            hi.setText("Hi, " + userName);
        }
    }


    public void loadCustomerData() {
        try {
            customerList.clear();
            customerList.addAll(MySQLDatabase.getUserDatabase().getCustomers());
            customerTable.setItems(customerList); 
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
            Parent addReaderParent = fxmlLoader.load();  
            AddReaderController controller = fxmlLoader.getController();
            Scene addReaderScene = new Scene(addReaderParent, 557, 362);  
            Stage addReaderStage = new Stage();
            addReaderStage.initModality(Modality.APPLICATION_MODAL);  
            addReaderStage.initOwner(stage);
            addReaderScene.setFill(Color.TRANSPARENT);
            addReaderStage.initStyle(StageStyle.TRANSPARENT);
            addReaderStage.setScene(addReaderScene);
            controller.setStage(addReaderStage);
            controller.setListReaderController(this);
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
        
        String searchID = txtSearchID.getText().trim();
        String searchName = txtSearchName.getText().trim();
        String searchPhone = txtSearchPhone.getText().trim();
        String searchCCCD = txtSearchCCCD.getText().trim();

        
        if (searchID.isEmpty() && searchName.isEmpty() && searchPhone.isEmpty() && searchCCCD.isEmpty()) {
            loadCustomerData();  
            return;
        }

        try {
            ObservableList<Customer> searchResults = MySQLDatabase.getUserDatabase().getUsersByMultipleSearchTerms(searchID, searchName, searchPhone, searchCCCD);

            if (!searchResults.isEmpty()) {
                customerList.clear();
                customerList.addAll(searchResults);  
                customerTable.setItems(customerList);  
            } else {
                customerList.clear();  
                customerTable.setItems(customerList);
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
        }
    }

    private void addButtonToTable() {
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button btnDelete = new Button("Detele");

            {

                
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
                    HBox buttonBox = new HBox(10, btnDelete);  
                    setGraphic(buttonBox);  
                }
            }
        });
    }


    private void handleDeleteCustomer(Customer customer) {
        if (customer != null) {
            try {
                boolean confirmed = ConfirmDialog.show("Confirm",
                        "Are you sure you want to delete this reader?");
                if (!confirmed) {
                    return;
                }

                
                MySQLDatabase.getUserDatabase().deleteCustomer(customer.getId());

                
                customerList.remove(customer);

                
                customerTable.setItems(customerList);

                System.out.println("Reader removed:" + customer.getName());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
