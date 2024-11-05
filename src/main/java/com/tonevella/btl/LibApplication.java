package com.tonevella.btl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LibApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LibApplication.class.getResource("LibApp.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 680);
        stage.setTitle("Library Application");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tonevella/btloop/LibApp.fxml"));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}