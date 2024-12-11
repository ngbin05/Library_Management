package org.example.demo;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class AddTransactionController {
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
    
    private final ObservableList<Customer> customerList = FXCollections.observableArrayList();

    @FXML
    public void back() {
        loadPage("cart-view.fxml");
    }

    @FXML
    public void initialize() {
        pane.setVisible(false);
        
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
            private final Button btnBorrow = new Button("Add new");

            {
                
                btnBorrow.setOnAction(event -> {
                    Customer customer = getTableView().getItems().get(getIndex());
                    handleBorrowCustomer(customer);  
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttonBox = new HBox(10, btnBorrow);  
                    setGraphic(buttonBox);  
                }
            }
        });
    }


    private void handleBorrowCustomer(Customer customer) {
        try {
            
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("borrow-view.fxml"));
            Parent root = fxmlLoader.load();

            
            pane.setVisible(true);
            pane.getChildren().clear();
            pane.getChildren().setAll(root);
            pane.setBackground(new Background(new BackgroundFill(Color.web("F4F4F4"), null, null)));

            
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), root);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setToValue(1.0);
            fadeTransition.play();

            
            Object controller = fxmlLoader.getController();
            if (controller instanceof BorrowController borrowController) {
                
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