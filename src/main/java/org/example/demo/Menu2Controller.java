package org.example.demo;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Menu2Controller {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private Pane pane;

    @FXML
    private Label dateTimeLabel;

    @FXML
    public void showReaderList(){
        loadPage("readers-view.fxml");
    }
    @FXML
    public void showBookList() {
        loadPage("bookList-view.fxml");
    }

    @FXML
    public void showProfile() { loadPage("profile-view.fxml"); }

    @FXML
    public void initialize() {
        pane.setVisible(false);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            LocalDateTime now = LocalDateTime.now();
            dateTimeLabel.setText(now.format(formatter));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }


    @FXML
    private void logOut() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Scene loginScene = new Scene(loader.load());
            loginScene.setFill(Color.TRANSPARENT);
            stage.setScene(loginScene);
            LoginController loginController = loader.getController();
            loginController.setStage(stage);
            stage.show();
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
                    ListReaderController listReaderController = (ListReaderController) controller;
                    listReaderController.setStage((Stage) pane.getScene().getWindow());
                } else if (controller instanceof ListBookController) {
                    ListBookController listBookController = (ListBookController) controller;
                    listBookController.setStage((Stage) pane.getScene().getWindow());
                } else if (controller instanceof ProfileController) {
                    ProfileController profileController = (ProfileController) controller;
                    profileController.setStage((Stage) pane.getScene().getWindow());
                }
                // Thêm các controller khác vào đây nếu cần
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

