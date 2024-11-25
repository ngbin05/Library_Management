package org.example.demo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;


public class LibraryManageMent extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Parent root = fxmlLoader.load();

        // Tạo Stage cho ứng dụng
        Scene scene = new Scene(root);
        primaryStage.setOpacity(1);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/media/icon-library.png")));
        primaryStage.setTitle("Library Management");
        LoginController controller = fxmlLoader.getController();
        controller.setStage(primaryStage);
        // Lấy controller từ FXML và gọi phương thức khởi tạo camera
//        TestCamera controller = fxmlLoader.getController();

        primaryStage.show();
    }

//    @Override
//    public void start(Stage primaryStage) throws Exception {
//
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("testcam-view.fxml"));
//        Parent root = fxmlLoader.load();
//        Rectangle clip = new Rectangle(795, 600);
//        clip.setArcWidth(30);
//        clip.setArcHeight(30);
//        root.setClip(clip);
//        Scene scene = new Scene(root);
//        primaryStage.setOpacity(1);
//        scene.setFill(Color.TRANSPARENT);
//        primaryStage.initStyle(StageStyle.TRANSPARENT);
//        primaryStage.setScene(scene);
//        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/media/icon-library.png")));
//        primaryStage.setTitle("Library Management");
//
////        LoginController controller = fxmlLoader.getController();
////        controller.setStage(primaryStage);
//
//        /* testttttttttttttttttttttttttttttttttttttt pushhhhhhhhhhhhhhhhhhhhhhhhhhhhhh*/
//        primaryStage.show();
//    }

    public static void main(String[] args) {
        launch(args);
    }
}
