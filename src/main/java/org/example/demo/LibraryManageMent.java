package org.example.demo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;


public class LibraryManageMent extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        primaryStage.setOpacity(1);
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/media/icon-library.png")));
        primaryStage.setTitle("Library Management");
        LoginController controller = fxmlLoader.getController();
        controller.setPrimaryStage(primaryStage);


        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}