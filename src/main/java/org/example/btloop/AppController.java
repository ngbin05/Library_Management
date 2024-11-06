package org.example.btloop;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AppController {

    @FXML
    private TextField userTextField;

    @FXML
    private PasswordField pwBox;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private Label messageLabel;

    @FXML
    private void initialize() {
        loginButton.setOnAction(event -> {
            String username = userTextField.getText();
            String password = pwBox.getText();
            boolean result = Account.processLogin(username, password);
            if (result) {
                messageLabel.setText("Login Successful");
            } else {
                messageLabel.setText("Login Failed");
            }
        });
    }
}
