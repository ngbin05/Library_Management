package org.example.demo;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AddReaderController {
    @FXML
    Label successLabel;

    private ListReaderController listReaderController;
    public void setListReaderController(ListReaderController listReaderController) {
        this.listReaderController = listReaderController;
    }
    public ListReaderController getListReaderController() {
        return listReaderController;
    }

    private Stage stage;
    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField cccdField;
    @FXML
    private Label errorCreate;
    @FXML
    private Label nameEmpty;
    @FXML
    private Label phoneEmpty;
    @FXML
    private Label addressEmpty;
    @FXML
    private Label cccdEmpty;

    @FXML
    private void initialize() {
        errorCreate.setVisible(false);
        nameEmpty.setVisible(false);
        addressEmpty.setVisible(false);
        phoneEmpty.setVisible(false);
        cccdEmpty.setVisible(false);
        successLabel.setVisible(false);

        nameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                phoneField.requestFocus();
            }
        });

        phoneField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                addressField.requestFocus();
            }
        });

        addressField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                cccdField.requestFocus();
            }
        });
    }

    @FXML
    private void handleCreate() {
        String fullname = nameField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String cccd = cccdField.getText();

        if (fullname == null || fullname.isEmpty()) {
            nameEmpty.setVisible(true);
            errorCreate.setVisible(false);
            addressEmpty.setVisible(false);
            phoneEmpty.setVisible(false);
            cccdEmpty.setVisible(false);
        } else if (address == null || address.isEmpty()) {
            nameEmpty.setVisible(false);
            phoneEmpty.setVisible(false);
            cccdEmpty.setVisible(false);
            errorCreate.setVisible(false);
            addressEmpty.setVisible(true);
        } else if (phone == null || phone.isEmpty()) {
            nameEmpty.setVisible(false);
            phoneEmpty.setVisible(true);
            cccdEmpty.setVisible(false);
            errorCreate.setVisible(false);
            addressEmpty.setVisible(false);
        } else if (cccd == null || cccd.isEmpty()) {
            nameEmpty.setVisible(false);
            phoneEmpty.setVisible(false);
            cccdEmpty.setVisible(true);
            errorCreate.setVisible(false);
            addressEmpty.setVisible(false);
        } else if (MySQLDatabase.getUserDatabase().isUserExists(cccd)) {
            nameEmpty.setVisible(false);
            phoneEmpty.setVisible(false);
            cccdEmpty.setVisible(false);
            errorCreate.setVisible(true);
            addressEmpty.setVisible(false);
            clearTextFields();
        } else {
            MySQLDatabase.getUserDatabase().insertUser(fullname, phone, address, cccd);
            listReaderController.loadCustomerData();
            clearError();
            clearTextFields();
            successLabel.setVisible(true);
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> handleClose());
            pause.play();

        }
    }

    @FXML
    public void handleClose() {
        stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void clearTextFields() {
        nameField.clear();
        phoneField.clear();
        addressField.clear();
        cccdField.clear();
    }

    private void clearError() {
        errorCreate.setVisible(false);
        nameEmpty.setVisible(false);
        phoneEmpty.setVisible(false);
        cccdEmpty.setVisible(false);
    }
}
