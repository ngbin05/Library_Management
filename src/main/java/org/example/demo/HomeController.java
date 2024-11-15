package org.example.demo;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private Label MenuPopBack;

    @FXML
    private Label MenuPopUp;

    @FXML
    private AnchorPane sidebar;

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
}
