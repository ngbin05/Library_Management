package org.example.demo;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;

public class ProfileController {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private TestCamera testCamera;

    public void setTestCamera(TestCamera testCamera) {
        this.testCamera = testCamera;
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
    private AnchorPane changeInf;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPhoneNumber;

    @FXML
    private TextField txtGmail;

    @FXML
    private Label lblChangeInf;

    @FXML
    public void initialize() {
        if(LoginController.account.getImage() == null) {
            avatar.setImage(new Image(getClass().getResourceAsStream("/media/man-user-icon.jpg")));
        } else {
            avatar.setImage(ImageUtils.byteArrayToImage(LoginController.account.getImage()));
        }

        double radius = Math.min(avatar.getFitWidth(), avatar.getFitHeight()) / 2;
        Circle clip = new Circle(avatar.getFitWidth() / 2, avatar.getFitHeight() / 2, radius);
        clip.setStroke(Color.BLACK);
        clip.setStrokeWidth(3);

        // Áp dụng clip vào ImageView
        avatar.setClip(clip);
        loadUserName();
        loadPhoneNumber();
        loadGmail();
//        loadAddress();

        userNameTextField.setEditable(false);
        phoneNumberTextField.setEditable(false);
        gmailTextField.setEditable(false);
    }



    @FXML
    public void onClickChangeAvatar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "Image Files", "*.jpg", "*.png", "*.gif", "*.bmp"));

        File selectedFile = fileChooser.showOpenDialog(avatar.getScene().getWindow());

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            avatar.setImage(image);
            LoginController.account.setImage(ImageUtils.imageToByteArray(image));
        }
    }

    @FXML
    public void loadImageFromCamera() {
        File folder = new File("avatar-image");

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Thư mục không tồn tại hoặc không phải thư mục.");
            return;
        }

        // Lấy danh sách các tệp trong thư mục và lọc ra các tệp ảnh
        File[] imageFiles = folder.listFiles(file -> file.isFile() &&
                (file.getName().endsWith(".jpg") || file.getName().endsWith(".png") || file.getName().endsWith(".jpeg")));

        if (imageFiles == null || imageFiles.length == 0) {
            System.out.println("Không tìm thấy ảnh nào trong thư mục.");
            return;
        }


        // Sắp xếp các tệp theo thời gian chỉnh sửa (mới nhất ở đầu)
        Arrays.sort(imageFiles, Comparator.comparingLong(File::lastModified).reversed());

        // Lấy tệp ảnh mới nhất
        File latestImage = imageFiles[0];
        Image image = new Image(latestImage.toURI().toString());
        avatar.setImage(image);
        LoginController.account.setImage(ImageUtils.imageToByteArray(image));
        System.out.println("Đã tải ảnh: " + latestImage.getName());
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



    // Phương thức lấy dữ liệu từ cơ sở dữ liệu và gán vào TextField
    private void loadUserName() {
        // Lấy tên người dùng từ Database
        String userName = Database.getFullName(LoginController.account.getUsername());

        // Nếu có dữ liệu, gán vào TextField
        if (userName != null) {
            userNameTextField.setText(userName); // Gán giá trị vào TextField
        }
    }

    private void loadPhoneNumber() {
        // Lấy tên người dùng từ Database
        String phoneNumber = Database.getPhoneNumber(LoginController.account.getUsername());

        // Nếu có dữ liệu, gán vào TextField
        if (phoneNumber != null) {
            phoneNumberTextField.setText(phoneNumber); // Gán giá trị vào TextField
        }
    }

    private void loadGmail() {
        // Lấy tên người dùng từ Database
        String gmail = Database.getEmail(LoginController.account.getUsername()
        );

        // Nếu có dữ liệu, gán vào TextField
        if (gmail != null) {
            gmailTextField.setText(gmail); // Gán giá trị vào TextField
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

        if (name.isEmpty() || phoneNumber.isEmpty() || gmail.isEmpty()) {
            lblChangeInf.setText("Please fill in all fields!");
            return;
        }

        boolean isInfoUpdated = Database.updateUserInformation(name, phoneNumber, gmail, LoginController.account.getUsername());

        if (isInfoUpdated) {
            lblChangeInf.setStyle("-fx-text-fill: green;");
            lblChangeInf.setText("Information updated successfully!");
            loadUserName(); // Optionally reload the values
            loadPhoneNumber();
            loadGmail();
        } else {
            lblChangeInf.setStyle("-fx-text-fill: red;");
            lblChangeInf.setText("Failed to update information.");
        }
        txtName.clear();
        txtPhoneNumber.clear();
        txtGmail.clear();
    }

    public void switchToCamera() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("testcam-view.fxml"));
            Parent addBookParent = fxmlLoader.load();  // Load FXML cho cửa sổ Add Reader
            testCamera = fxmlLoader.getController();
            Scene addReaderScene = new Scene(addBookParent);  // Thay bằng FXML tương ứng
            Stage addBookStage = new Stage();
            addBookStage.initOwner(stage);
            addBookStage.initStyle(StageStyle.TRANSPARENT);
            addReaderScene.setFill(Color.TRANSPARENT);
            addBookStage.setScene(addReaderScene);
            testCamera.setStage(addBookStage);
            testCamera.setProfileController(this);
            Platform.runLater(() ->
            {
                double mainStageX = stage.getX();
                double mainStageY = stage.getY();
                addBookStage.show();
                double x = mainStageX + stage.getWidth() - 795;
                double y = mainStageY + stage.getHeight() - 600;
                addBookStage.setX(x);
                addBookStage.setY(y);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
