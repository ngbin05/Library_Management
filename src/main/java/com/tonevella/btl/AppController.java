package com.tonevella.btl;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;


public class AppController {
    @FXML
    private TextField usernamefield;
    @FXML
    private PasswordField passwordfield;
    @FXML
    private Label errorlabel;
    @FXML
    private Label successlabel;

    private Account acc = new Account();
    private boolean showError = false;

    @FXML
    public void initialize() {
        usernamefield.textProperty().addListener((observable, oldValue, newValue) -> resetMessages());
        passwordfield.textProperty().addListener((observable, oldValue, newValue) -> resetMessages());
    }

    @FXML
    protected void handleLogin() {
        String username = usernamefield.getText();
        String password = passwordfield.getText();

        if (showError) {
            errorlabel.setText("");
            successlabel.setText("");
            showError = false;
        }

        if (acc.account(username, password)) {
            successlabel.setText("Login successful!");
        } else {
            errorlabel.setText("Login failed!");
            showError = true;
        }
    }

    private void resetMessages() {
        if(showError) {
            errorlabel.setText("Login failed!");
        }
    }
}