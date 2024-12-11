package org.example.demo;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginController {
    public static Account account = new Account();
    private Stage primaryStage;
    @FXML
    private Pane pane;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private Label usernameEmpty;

    @FXML
    private Label passwordEmpty;
    @FXML
    private TextField fullnameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField createUsername;
    @FXML
    private PasswordField createPassword;
    @FXML
    private Label errorCreate;
    @FXML
    private Label fullnameEmpty;
    @FXML
    private Label emailEmpty;
    @FXML
    private Label phoneEmpty;
    @FXML
    private Label createUsernameEmpty;
    @FXML
    private Label createPasswordEmpty;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();

        String password = passwordField.getText();

        if (username == null || username.isEmpty()) {
            errorLabel.setVisible(false);
            usernameEmpty.setVisible(true);
            passwordEmpty.setVisible(false);
        } else if (password == null || password.isEmpty()) {
            errorLabel.setVisible(false);
            usernameEmpty.setVisible(false);
            passwordEmpty.setVisible(true);
        } else if (MySQLDatabase.getUserDatabase().validAccount(username, password)) {
            errorLabel.setVisible(false);
            usernameEmpty.setVisible(false);
            passwordEmpty.setVisible(false);
            account = MySQLDatabase.getUserDatabase().getAccountByUsername(username);
            switchToMainScreen();
        } else {
            errorLabel.setVisible(true);
            usernameField.clear();
            passwordField.clear();
            usernameEmpty.setVisible(false);
            passwordEmpty.setVisible(false);
        }
    }

    @FXML
    private void handleCreate() {
        String fullname = fullnameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String username = createUsername.getText();
        String password = createPassword.getText();

        if (fullname == null || fullname.isEmpty()) {
            fullnameEmpty.setVisible(true);
            errorLabel.setVisible(false);
            emailEmpty.setVisible(false);
            phoneEmpty.setVisible(false);
            createUsernameEmpty.setVisible(false);
            createPasswordEmpty.setVisible(false);
        } else if (email == null || email.isEmpty()) {
            fullnameEmpty.setVisible(false);
            phoneEmpty.setVisible(false);
            createUsernameEmpty.setVisible(false);
            createPasswordEmpty.setVisible(false);
            errorLabel.setVisible(false);
            emailEmpty.setVisible(true);
        } else if (phone == null || phone.isEmpty()) {
            fullnameEmpty.setVisible(false);
            emailEmpty.setVisible(false);
            createUsernameEmpty.setVisible(false);
            createPasswordEmpty.setVisible(false);
            errorLabel.setVisible(false);
            phoneEmpty.setVisible(true);
        } else if (username == null || username.isEmpty()) {
            fullnameEmpty.setVisible(false);
            emailEmpty.setVisible(false);
            phoneEmpty.setVisible(false);
            createPasswordEmpty.setVisible(false);
            errorLabel.setVisible(false);
            createUsernameEmpty.setVisible(true);
        } else if (password == null || password.isEmpty()) {
            fullnameEmpty.setVisible(false);
            emailEmpty.setVisible(false);
            phoneEmpty.setVisible(false);
            createUsernameEmpty.setVisible(false);
            createPasswordEmpty.setVisible(true);
            errorLabel.setVisible(false);
        } else if (MySQLDatabase.getUserDatabase().isUsernameTaken(username)) {
            fullnameEmpty.setVisible(false);
            emailEmpty.setVisible(false);
            phoneEmpty.setVisible(false);
            createUsernameEmpty.setVisible(false);
            createPasswordEmpty.setVisible(false);
            errorLabel.setVisible(true);
            createUsername.clear();
            createPassword.clear();
        } else {
            MySQLDatabase.getUserDatabase().registerAccount(fullname, email, phone, username, password);
            account = MySQLDatabase.getUserDatabase().getAccountByUsername(username);
            switchToMainScreen();
        }
    }

    @FXML
    private void initialize() {
        pane.setVisible(false);

        errorLabel.setVisible(false);
        errorCreate.setVisible(false);
        usernameEmpty.setVisible(false);
        passwordEmpty.setVisible(false);

        fullnameEmpty.setVisible(false);
        emailEmpty.setVisible(false);
        phoneEmpty.setVisible(false);
        createUsernameEmpty.setVisible(false);
        createPasswordEmpty.setVisible(false);

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLogin();
            }
        });

        usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                passwordField.requestFocus();
            }
        });

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                usernameField.requestFocus();
            }
        });

        createPassword.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleCreate();
            }
        });

        fullnameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                emailField.requestFocus();
            }
        });

        emailField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                phoneField.requestFocus();
            }
        });

        phoneField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                createUsername.requestFocus();
            }
        });

        createUsername.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                createPassword.requestFocus();
            }
        });

        createPassword.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                fullnameField.requestFocus();
            }
        });
    }

    public void setStage(Stage stage) {
        this.primaryStage = stage;
    }

    private void switchToMainScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu2-view.fxml"));
            Parent root = loader.load();
            Scene mainScene = new Scene(root);
            mainScene.setFill(Color.TRANSPARENT);
            primaryStage.setScene(mainScene);
            MenuController homeController = loader.getController();
            homeController.setStage(primaryStage);

            FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), root);
            fadeTransition.setFromValue(0.9);
            fadeTransition.setToValue(1.0);
            fadeTransition.play();

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void handleExitButton(ActionEvent event) {
        Platform.exit(); 
    }
}
