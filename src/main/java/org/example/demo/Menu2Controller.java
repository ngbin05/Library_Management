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
    
    private void loadPage(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent root = loader.load();
            ListReaderController controller = loader.getController();
            pane.getChildren().setAll(root);
            controller.setStage((Stage) pane.getScene().getWindow());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}

