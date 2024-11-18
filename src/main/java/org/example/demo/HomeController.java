package org.example.demo;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private Label MenuPopBack;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label MenuPopUp;

    @FXML
    private AnchorPane sidebar;

    @FXML
    public void showReaderList(){
        loadPage("readers-view.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sidebar.setTranslateX(-148);

        MenuPopUp.setOnMouseClicked((event) -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.2));
            slide.setNode(sidebar);

            slide.setToX(0);
            slide.play();

            sidebar.setTranslateX(-148);

            slide.setOnFinished((ActionEvent e) -> {
                MenuPopUp.setVisible(false);
                MenuPopBack.setVisible(true);
            });
        });

        MenuPopBack.setOnMouseClicked((event) -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(sidebar);

            slide.setToX(-148);
            slide.play();

            sidebar.setTranslateX(0);

            slide.setOnFinished((ActionEvent e) -> {
                MenuPopUp.setVisible(true);
                MenuPopBack.setVisible(false);
            });
        });
    }


    @FXML
    private void loadPage(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent root = loader.load();
            ListReaderController controller = loader.getController();
            anchorPane.getChildren().setAll(root);
            controller.setStage((Stage) anchorPane.getScene().getWindow());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
