package org.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ForgotPassController {
    private Stage stage;

    @FXML
    private TextField email;

    @FXML
    private Label error_em;

    @FXML
    private AnchorPane changePass;

    @FXML
    private PasswordField pass;

    @FXML
    private PasswordField confirm;

    @FXML
    private Label error_pass;

    @FXML
    private Label email_text;

    @FXML
    private Button next_but;

    public void checkEmail() {
        String _email = email.getText().trim();

        if (_email.isEmpty()) {
            error_em.setText("Vui lòng nhập email!");
            error_em.setStyle("-fx-text-fill: red;");
            error_em.setVisible(true);
            changePass.setVisible(false);
            return;
        }

        // Gọi phương thức kiểm tra từ Database
        boolean accountExists = Database.checkEmailExists(_email);

        if (accountExists) {
            // Nếu tài khoản tồn tại, ẩn lỗi và hiển thị giao diện đổi mật khẩu
            error_em.setVisible(false); // Đảm bảo thông báo lỗi bị ẩn
            changePass.setVisible(true); // Hiển thị giao diện thay đổi mật khẩu
            email_text.setVisible(false);
            next_but.setVisible(false);
            email.setVisible(false);
        } else {
            // Nếu tài khoản không tồn tại, hiển thị lỗi
            error_em.setText("Email không tồn tại!");
            error_em.setStyle("-fx-text-fill: red;");
            error_em.setVisible(true); // Hiển thị thông báo lỗi
            changePass.setVisible(false); // Ẩn giao diện thay đổi mật khẩu
        }
    }


    public void updatePass() {
        String password = pass.getText();
        String confirmPass = confirm.getText();
        String _email = email.getText();

        if (password.isEmpty() || confirmPass.isEmpty()) {
            error_pass.setText("Vui lòng nhập đầy đủ thông tin!");
            error_pass.setStyle("-fx-text-fill: red;");
            error_pass.setVisible(true);
            pass.clear();
            confirm.clear();
            return;
        }

        if (!confirmPass.equals(password)) {
            error_pass.setText("Mật khẩu không khớp!");
            error_pass.setVisible(true);
            error_pass.setStyle("-fx-text-fill: red;");
            pass.clear();
            confirm.clear();
            return;
        }

        boolean isPasswordChanged = Database.updatenewPass(_email, password);

        if (isPasswordChanged) {
            error_pass.setStyle("-fx-text-fill: green;"); // Đổi màu chữ sang xanh lá
            error_pass.setText("Password changed successfully!");
            error_pass.setVisible(true);
            pass.clear();
            confirm.clear();
        } else {
            error_pass.setStyle("-fx-text-fill: red;"); // Đổi màu chữ sang xanh lá
            error_pass.setText("Password changed failed!");
            error_pass.setVisible(true);
            pass.clear();
            confirm.clear();
        }
    }

    public void logOut() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Scene loginScene = new Scene(loader.load());
            loginScene.setFill(Color.TRANSPARENT);
            stage.setScene(loginScene);
            LoginController loginController = loader.getController();
            loginController.setStage(stage);
            stage.show();
            if (stage != null) {
                stage.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }
}
