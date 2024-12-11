package org.example.demo;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class CameraController {
    private Stage stage;
    private ProfileController profileController;
    @FXML
    private Canvas gridCanvas;
    @FXML
    private Label countDownLabel;
    @FXML
    private ImageView imageView;
    @FXML
    private Button zoomInButton;
    @FXML
    private Button zoomOutButton;
    @FXML
    private Button rotateButton;
    @FXML
    private Button flipButton;
    @FXML
    private Button retakeButton;
    @FXML
    private Button saveButton;
    @FXML
    private Label successTakenLabel;
    @FXML
    private Pane pane;
    private FrameGrabber grabber;
    private boolean isCameraRunning = false;
    private boolean isPhotoCaptured = false;
    private BufferedImage currentCapturedImage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setProfileController(ProfileController profileController) {
        this.profileController = profileController;
    }

    @FXML
    public void Close() {
        stopCamera();
        loadPage("profile-view.fxml");
    }

    @FXML
    public void initialize() {
        stage = new Stage();
        resetUI();
        imageView.setStyle("-fx-background-color: #000000; " +
                "-fx-padding: 10; " +
                "-fx-border-color: #000000; " +
                "-fx-border-width: 3; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10, 0.5, 0, 0);");
        
        imageView.boundsInParentProperty().addListener((obs, oldBounds, newBounds) -> drawGridOverlay());
        drawGridOverlay();
        startCamera();
    }

    public void startCamera() {
        
        imageView.setVisible(true);
        new Thread(() -> {
            try {
                grabber = new OpenCVFrameGrabber(0); 
                grabber.start();
                isCameraRunning = true;
                gridCanvas.setVisible(true);

                
                while (isCameraRunning) {
                    if (isPhotoCaptured) {
                        break;  
                    }
                    Frame frame = grabber.grab();
                    if (frame != null) {
                        BufferedImage bufferedImage = frameToBufferedImage(frame);
                        if (bufferedImage != null) {
                            Image image = bufferedImageToImage(bufferedImage);
                            if (image != null) {
                                

                                Platform.runLater(() -> {



                                    imageView.setImage(image); 
                                });
                            }
                        }
                    }
                }
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    public void retakePhoto() {
        resetUI();
        isPhotoCaptured = false; 
        imageView.setImage(null); 
        startCamera();
    }


    public void stopCamera() {
        try {
            if (grabber != null) {
                isCameraRunning = false; 
                isPhotoCaptured = false;
                grabber.stop(); 
                System.out.println("Closed camera!");
                imageView.setImage(null); 
                gridCanvas.setVisible(false);
                imageView.setVisible(false);
            }
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void captureImage() {
        if (grabber != null && isCameraRunning) {
            try {
                
                int countdownTime = 3; 

                Platform.runLater(() -> {
                    countDownLabel.setVisible(true);
                    countDownLabel.setText("Get Started!");
                });

                
                new Thread(() -> {
                    try {
                        for (int i = countdownTime; i > 0; i--) {
                            final int count = i;
                            Thread.sleep(1000); 

                            Platform.runLater(() -> countDownLabel.setText(Integer.toString(count)));
                        }

                        
                        Frame frame = grabber.grab();
                        if (frame != null) {
                            BufferedImage bufferedImage = frameToBufferedImage(frame);
                            if (bufferedImage != null) {
                                System.out.println("Successful photoshoot!");
                                gridCanvas.setVisible(false);

                                
                                Image image = bufferedImageToImage(bufferedImage);
                                if (image != null) {
                                    isPhotoCaptured = true;
                                    currentCapturedImage = bufferedImage; 
                                    Platform.runLater(() -> imageView.setImage(image));
                                    zoomInButton.setVisible(true);
                                    zoomOutButton.setVisible(true);
                                    retakeButton.setVisible(true);
                                    saveButton.setVisible(true);
                                    rotateButton.setVisible(true);
                                    flipButton.setVisible(true);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        Platform.runLater(() -> countDownLabel.setVisible(false));
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void saveCapturedImage() {
        if (currentCapturedImage != null) {
            try {
                String fileName = "avatar-image/image_" + System.currentTimeMillis() + ".png";
                ImageIO.write(currentCapturedImage, "png", new File(fileName));
                System.out.println("Saved image: " + fileName);
                successTakenLabel.setVisible(true);
                profileController.loadImageFromCamera();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No photos to save!");
        }
    }


    public Image bufferedImageToImage(BufferedImage bufferedImage) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(toByteArray(bufferedImage));
            return new Image(byteArrayInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] toByteArray(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);  
        return baos.toByteArray();
    }

    public BufferedImage frameToBufferedImage(Frame frame) {
        
        if (frame != null && frame.image != null) {
            int width = frame.imageWidth;
            int height = frame.imageHeight;

            
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            
            ByteBuffer byteBuffer = (ByteBuffer) frame.image[0].position(0);  
            int byteBufferSize = byteBuffer.remaining();  

            
            int expectedSize = width * height * 3; 
            if (byteBufferSize < expectedSize) {
                System.out.println("There are not enough bytes in the ByteBuffer to create the image. Incomplete data.");
                return null;  
            }

            byte[] byteArray = new byte[expectedSize]; 
            byteBuffer.get(byteArray, 0, expectedSize);  

            int[] pixels = new int[width * height];

            
            for (int i = 0; i < byteArray.length / 3; i++) {
                int b = byteArray[i * 3] & 0xFF;     
                int g = byteArray[i * 3 + 1] & 0xFF; 
                int r = byteArray[i * 3 + 2] & 0xFF; 

                
                int rgb = (r << 16) | (g << 8) | b;   
                pixels[i] = rgb;                      
            }

            
            bufferedImage.setRGB(0, 0, width, height, pixels, 0, width);

            return bufferedImage;
        }
        return null;
    }

    private void drawGridOverlay() {
        
        double width = imageView.getBoundsInParent().getWidth();
        double height = imageView.getBoundsInParent().getHeight();

        
        System.out.println("ImageView size: " + width + "x" + height);

        if (width <= 0 || height <= 0) {
            System.out.println("The ImageView size is invalid, the grid cannot be drawn.");
            return;
        }

        
        gridCanvas.setWidth(width);
        gridCanvas.setHeight(height);

        
        GraphicsContext gc = gridCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, width, height); 

        
        gc.setStroke(javafx.scene.paint.Color.WHITESMOKE.deriveColor(0, 1, 1, 0.5));
        gc.setLineWidth(1);

        
        for (int i = 1; i < 3; i++) {
            double y = height * i / 3;
            gc.strokeLine(0, y, width, y);
        }

        
        for (int i = 1; i < 3; i++) {
            double x = width * i / 3;
            gc.strokeLine(x, 0, x, height);
        }

        System.out.println("Drew a 3x3 grid on Canvas.");
    }


    @FXML
    public void rotateImage() {
        if (currentCapturedImage != null) {
            int width = currentCapturedImage.getWidth();
            int height = currentCapturedImage.getHeight();
            BufferedImage rotatedImage = new BufferedImage(height, width, currentCapturedImage.getType());
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    rotatedImage.setRGB(y, width - x - 1, currentCapturedImage.getRGB(x, y));
                }
            }
            currentCapturedImage = rotatedImage;
            Platform.runLater(() -> imageView.setImage(bufferedImageToImage(rotatedImage)));
        }
    }


    @FXML
    public void flipImageHorizontally() {
        if (currentCapturedImage != null) {
            int width = currentCapturedImage.getWidth();
            int height = currentCapturedImage.getHeight();
            BufferedImage flippedImage = new BufferedImage(width, height, currentCapturedImage.getType());
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    flippedImage.setRGB(width - x - 1, y, currentCapturedImage.getRGB(x, y));
                }
            }
            currentCapturedImage = flippedImage;
            Platform.runLater(() -> imageView.setImage(bufferedImageToImage(flippedImage)));
        }
    }


    @FXML
    public void zoomInImage() {
        zoomImage(1.2); 
    }

    @FXML
    public void zoomOutImage() {
        zoomImage(1 / 1.2); 
    }

    private void zoomImage(double zoomFactor) {
        if (imageView.getImage() == null) return; 

        
        Rectangle2D viewport = imageView.getViewport();
        if (viewport == null) {
            viewport = new Rectangle2D(0, 0, imageView.getImage().getWidth(), imageView.getImage().getHeight());
        }

        
        double newWidth = viewport.getWidth() / zoomFactor;
        double newHeight = viewport.getHeight() / zoomFactor;

        
        double centerX = viewport.getMinX() + viewport.getWidth() / 2;
        double centerY = viewport.getMinY() + viewport.getHeight() / 2;
        double newMinX = centerX - newWidth / 2;
        double newMinY = centerY - newHeight / 2;

        
        newMinX = Math.max(newMinX, 0);
        newMinY = Math.max(newMinY, 0);
        if (newMinX + newWidth > imageView.getImage().getWidth()) {
            newMinX = imageView.getImage().getWidth() - newWidth;
        }
        if (newMinY + newHeight > imageView.getImage().getHeight()) {
            newMinY = imageView.getImage().getHeight() - newHeight;
        }

        
        imageView.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
    }

    public void resetUI() {
        successTakenLabel.setVisible(false);
        zoomInButton.setVisible(false);
        zoomOutButton.setVisible(false);
        retakeButton.setVisible(false);
        saveButton.setVisible(false);
        rotateButton.setVisible(false);
        flipButton.setVisible(false);

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
                if (controller instanceof ProfileController) {
                    profileController.setStage((Stage) pane.getScene().getWindow());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
