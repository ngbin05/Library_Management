package org.example.demo;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class ProfileController {
    private Stage stage;
    private CameraController cameraController;
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
    private Label infChanged;
    @FXML
    private Label passwordChanged;
    @FXML
    private Pane pane;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setTestCamera(CameraController cameraController) {
        this.cameraController = cameraController;
    }

    @FXML
    public void openCamera() {
        loadPage("testcam-view.fxml");
    }

    @FXML
    public void initialize() {
        if (LoginController.account.getImage() == null) {
            avatar.setImage(new Image(getClass().getResourceAsStream("/media/man-user-icon.jpg")));
        } else {
            avatar.setImage(ImageUtils.byteArrayToImage(LoginController.account.getImage()));
        }
        avatar.setPreserveRatio(true);
        double radius = Math.min(avatar.getFitWidth(), avatar.getFitHeight()) / 2;
        Circle clip = new Circle(avatar.getFitWidth() / 2, avatar.getFitHeight() / 2, radius);
        avatar.setClip(clip);
        loadUserName();
        loadPhoneNumber();
        loadGmail();


        userNameTextField.setEditable(false);
        phoneNumberTextField.setEditable(false);
        gmailTextField.setEditable(false);
        lblChangeInf.setVisible(false);
        lblChangePass.setVisible(false);
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
            System.out.println("Folders that don't exist or aren't folders!");
            return;
        }

        File[] imageFiles = folder.listFiles(file -> file.isFile() &&
                (file.getName().endsWith(".jpg") || file.getName().endsWith(".png") || file.getName().endsWith(".jpeg")));

        if (imageFiles == null || imageFiles.length == 0) {
            System.out.println("No photos found in the folder!");
            return;
        }


        Arrays.sort(imageFiles, Comparator.comparingLong(File::lastModified).reversed());

        File latestImage = imageFiles[0];
        Image image = new Image(latestImage.toURI().toString());
        avatar.setImage(image);
        LoginController.account.setImage(ImageUtils.imageToByteArray(image));
        System.out.println("Photos Downloaded: " + latestImage.getName());
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

        if (username.isEmpty() || oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            lblChangePass.setVisible(true);
            lblChangePass.setText("Please enter complete information!");
            txtUsername.clear();
            txtOldPassword.clear();
            txtNewPassword.clear();
            txtConfirmPassword.clear();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            lblChangePass.setVisible(true);
            lblChangePass.setText("The new password doesn't match!");
            txtUsername.clear();
            txtOldPassword.clear();
            txtNewPassword.clear();
            txtConfirmPassword.clear();
            return;
        }


        boolean isPasswordChanged = MySQLDatabase.getUserDatabase().changePassword(username, oldPassword, newPassword);

        if (isPasswordChanged) {
            lblChangePass.setVisible(true);
            lblChangePass.setStyle("-fx-text-fill: green;");
            lblChangePass.setText("Password changed successfully!");
            txtUsername.clear();
            txtOldPassword.clear();
            txtNewPassword.clear();
            txtConfirmPassword.clear();

        } else {
            lblChangePass.setVisible(true);
            lblChangePass.setStyle("-fx-text-fill: red;");
            lblChangePass.setText("Password changed failed!");
            txtUsername.clear();
            txtOldPassword.clear();
            txtNewPassword.clear();
            txtConfirmPassword.clear();
        }
    }



    private void loadUserName() {

        String userName = MySQLDatabase.getUserDatabase().getFullName(LoginController.account.getUsername());

        if (userName != null) {
            userNameTextField.setText(userName);
        }
    }

    private void loadPhoneNumber() {

        String phoneNumber = MySQLDatabase.getUserDatabase().getPhoneNumber(LoginController.account.getUsername());


        if (phoneNumber != null) {
            phoneNumberTextField.setText(phoneNumber);
        }
    }

    private void loadGmail() {

        String gmail = MySQLDatabase.getUserDatabase().getEmail(LoginController.account.getUsername()
        );


        if (gmail != null) {
            gmailTextField.setText(gmail);
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
            lblChangeInf.setVisible(true);
            lblChangeInf.setText("Please fill in all fields!");
            return;
        }

        boolean isInfoUpdated = MySQLDatabase.getUserDatabase().updateUserInformation(name, phoneNumber, gmail, LoginController.account.getUsername());

        if (isInfoUpdated) {
            lblChangeInf.setVisible(true);
            lblChangeInf.setStyle("-fx-text-fill: green;");
            lblChangeInf.setText("Information updated successfully!");
            loadUserName();
            loadPhoneNumber();
            loadGmail();
        } else {
            lblChangeInf.setVisible(true);
            lblChangeInf.setStyle("-fx-text-fill: red;");
            lblChangeInf.setText("Failed to update information.");
        }
        txtName.clear();
        txtPhoneNumber.clear();
        txtGmail.clear();

    }

    private void loadPage(String fxmlFileName) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent root = loader.load();

            pane.setVisible(true);
            pane.getChildren().clear();
            pane.getChildren().setAll(root);
            pane.setBackground(new Background(new BackgroundFill(Color.web("F4F4F4"), null, null)));
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), root);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setToValue(1.0);
            fadeTransition.play();


            Object controller = loader.getController();
            if (controller != null) {
                if (controller instanceof CameraController cameraControllerController) {
                    cameraControllerController.setStage((Stage) pane.getScene().getWindow());
                    cameraControllerController.setProfileController(this);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
