package org.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public class profileController {

    @FXML
    private Stage stage;

    @FXML
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private ImageView avatar;

    @FXML
    private AnchorPane changeAccount;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtOldPassword;

    @FXML
    private PasswordField txtNewPassword;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private Label lblChangePass;

    @FXML
    private TextField userNameTextField;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private TextField gmailTextField;

    @FXML
    private TextField adressTextField;

    @FXML
    private AnchorPane changeInf;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPhoneNumber;

    @FXML
    private TextField txtGmail;

    @FXML
    private TextField txtAddress;

    @FXML
    private Label lblChangeInf;

    @FXML
    public void onClickChangeAvatar(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif", "*.bmp"));

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            avatar.setImage(image);
        }
    }

    @FXML
    public void onClickButtonChangePass(ActionEvent e) {
        changeAccount.setVisible(!changeAccount.isVisible());
    }

    @FXML
    private void handleChangePassword() {
        String username = txtUsername.getText();
        String oldPassword = txtOldPassword.getText();
        String newPassword = txtNewPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        // Kiểm tra dữ liệu đầu vào
        if (username.isEmpty() || oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            lblChangePass.setText("Vui lòng nhập đầy đủ thông tin!");
            txtUsername.clear();
            txtOldPassword.clear();
            txtNewPassword.clear();
            txtConfirmPassword.clear();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            lblChangePass.setText("Mật khẩu mới không khớp!");
            txtUsername.clear();
            txtOldPassword.clear();
            txtNewPassword.clear();
            txtConfirmPassword.clear();
            return;
        }

        // Gọi phương thức thay đổi mật khẩu từ lớp Database
        boolean isPasswordChanged = Database.changePassword(username, oldPassword, newPassword);

        if (isPasswordChanged) {
            lblChangePass.setStyle("-fx-text-fill: green;"); // Đổi màu chữ sang xanh lá
            lblChangePass.setText("Password changed successfully!");
            txtUsername.clear();
            txtOldPassword.clear();
            txtNewPassword.clear();
            txtConfirmPassword.clear();
        } else {
            lblChangePass.setStyle("-fx-text-fill: red;"); // Đổi màu chữ sang xanh lá
            lblChangePass.setText("Password changed failed!");
            txtUsername.clear();
            txtOldPassword.clear();
            txtNewPassword.clear();
            txtConfirmPassword.clear();
        }
    }

    @FXML
    public void initialize() {
        // Gọi hàm để lấy tên người dùng từ cơ sở dữ liệu và gán vào TextField
        loadUserName();
        loadPhoneNumber();
        loadGmail();
        loadAddress();

        userNameTextField.setEditable(false);
        phoneNumberTextField.setEditable(false);
        gmailTextField.setEditable(false);
        adressTextField.setEditable(false);
    }

    // Phương thức lấy dữ liệu từ cơ sở dữ liệu và gán vào TextField
    private void loadUserName() {
        // Lấy tên người dùng từ Database
        String userName = Database.getUserName();

        // Nếu có dữ liệu, gán vào TextField
        if (userName != null) {
            userNameTextField.setText(userName); // Gán giá trị vào TextField
        }
    }

    private void loadPhoneNumber() {
        // Lấy tên người dùng từ Database
        String phoneNumber = Database.getPhoneNumber();

        // Nếu có dữ liệu, gán vào TextField
        if (phoneNumber != null) {
            phoneNumberTextField.setText(phoneNumber); // Gán giá trị vào TextField
        }
    }

    private void loadGmail() {
        // Lấy tên người dùng từ Database
        String gmail = Database.getGmail();

        // Nếu có dữ liệu, gán vào TextField
        if (gmail != null) {
            gmailTextField.setText(gmail); // Gán giá trị vào TextField
        }
    }

    private void loadAddress() {
        // Lấy tên người dùng từ Database
        String address = Database.getAddress();

        // Nếu có dữ liệu, gán vào TextField
        if (address != null) {
            adressTextField.setText(address); // Gán giá trị vào TextField
        }
    }

    @FXML
    public void onClickButtonChangeInf(ActionEvent e) {
        changeInf.setVisible(!changeInf.isVisible());
    }

    @FXML
    public void onHandleChangeInf(ActionEvent e) {
        String name = txtName.getText();
        String phoneNumber = txtPhoneNumber.getText();
        String gmail = txtGmail.getText();
        String address = txtAddress.getText();

        if (name.isEmpty() || phoneNumber.isEmpty() || gmail.isEmpty() || address.isEmpty()) {
            lblChangeInf.setText("Please fill in all fields!");
            return;
        }

        boolean isInfoUpdated = Database.updateUserInformation(name, phoneNumber, gmail, address, String.valueOf(1));

        if (isInfoUpdated) {
            lblChangeInf.setStyle("-fx-text-fill: green;");
            lblChangeInf.setText("Information updated successfully!");
            loadUserName(); // Optionally reload the values
            loadPhoneNumber();
            loadGmail();
            loadAddress();
        } else {
            lblChangeInf.setStyle("-fx-text-fill: red;");
            lblChangeInf.setText("Failed to update information.");
        }
        txtName.clear();
        txtPhoneNumber.clear();
        txtGmail.clear();
        txtAddress.clear();
    }
}