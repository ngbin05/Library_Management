package org.example.demo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;


public class LibraryManageMent extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("book-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600,500);
        primaryStage.setOpacity(1);
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/media/icon-library.png")));
        primaryStage.setTitle("Library Management");
//        LoginController loginController = fxmlLoader.getController();
//        loginController.setPrimaryStage(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}