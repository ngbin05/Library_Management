package org.example.demo;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.bytedeco.javacv.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;

public class TestCamera {
    private Stage stage;

    @FXML
    private Canvas gridCanvas;

    @FXML
    private Label countDownLabel;

    @FXML
    private ImageView imageView;

    private FrameGrabber grabber;
    private boolean isCameraRunning = false;
    private boolean isPhotoCaptured = false;
    private BufferedImage currentCapturedImage;

    @FXML
    public void initialize() {
        stage = new Stage();
        // Khởi tạo camera và bắt đầu lấy khung hình ngay khi ứng dụng chạ
        imageView.boundsInParentProperty().addListener((obs, oldBounds, newBounds) -> drawGridOverlay());
        drawGridOverlay();
    }

    @FXML
    public void startCamera() {
        // Mở camera và bắt đầu lấy frame liên tục
        imageView.setVisible(true);
        new Thread(() -> {
            try {
                grabber = new OpenCVFrameGrabber(0); // 0 là ID của camera mặc định
                grabber.start();
                isCameraRunning = true;
                gridCanvas.setVisible(true);

                // Liên tục lấy frame từ camera và cập nhật lên ImageView
                while (isCameraRunning) {
                    if (isPhotoCaptured) {
                        break;  // Nếu đã chụp ảnh, dừng lấy frame từ camera
                    }
                    Frame frame = grabber.grab();
                    if (frame != null) {
                        BufferedImage bufferedImage = frameToBufferedImage(frame);
                        if (bufferedImage != null) {
                            Image image = bufferedImageToImage(bufferedImage);
                            if (image != null) {
                                // Cập nhật ảnh lên ImageView (phải thực hiện trên UI thread)

                                Platform.runLater(() -> {
//                                    imageView.setFitWidth(300); // Chiều rộng của ImageView
//                                    imageView.setFitHeight(200); // Chiều cao của ImageView
//                                    imageView.setPreserveRatio(true); // Đảm bảo giữ tỷ lệ của ảnh
                                    imageView.setImage(image); // Cập nhật ảnh
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
            isPhotoCaptured = false; // Reset trạng thái đã chụp ảnh
            imageView.setImage(null); // Xóa ảnh đã chụp
            startCamera();
    }


    @FXML
    public void stopCamera() {
        try {
            if (grabber != null) {
                isCameraRunning = false; // Dừng việc lấy frame
                grabber.stop(); // Đóng camera
                System.out.println("Camera đã đóng.");
                imageView.setImage(null); // Xóa ảnh trong ImageView
                gridCanvas.setVisible(false);
                imageView.setVisible(false);
            }
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openCameraAndCaptureImage() {
        if (grabber != null && isCameraRunning) {
            try {
                // Đếm ngược thời gian trước khi chụp ảnh
                int countdownTime = 3; // Đếm ngược từ 3 giây

                Platform.runLater(() -> {
                    countDownLabel.setVisible(true);
                    countDownLabel.setText("Chuẩn bị!");
                });

                // Chạy đếm ngược trong một thread riêng để không block UI thread
                new Thread(() -> {
                    try {
                        for (int i = countdownTime; i > 0; i--) {
                            final int count = i;
                            Thread.sleep(1000); // Dừng lại 1 giây giữa mỗi vòng lặp

                            Platform.runLater(() -> countDownLabel.setText(Integer.toString(count)));
                        }

                        // Sau khi đếm ngược, thực hiện chụp ảnh
                        Frame frame = grabber.grab();
                        if (frame != null) {
                            BufferedImage bufferedImage = frameToBufferedImage(frame);
                            if (bufferedImage != null) {
                                System.out.println("Chụp ảnh thành công!");
                                gridCanvas.setVisible(false);

                                // Chuyển đổi BufferedImage thành Image và hiển thị lên ImageView
                                Image image = bufferedImageToImage(bufferedImage);
                                if (image != null) {
                                    isPhotoCaptured = true;
                                    currentCapturedImage = bufferedImage; // Lưu ảnh vào biến tạm
                                    Platform.runLater(() -> imageView.setImage(image));
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
                System.out.println("Ảnh đã lưu: " + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Không có ảnh nào để lưu!");
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
        ImageIO.write(bufferedImage, "png", baos);  // Sử dụng PNG để đảm bảo chất lượng
        return baos.toByteArray();
    }

    public BufferedImage frameToBufferedImage(Frame frame) {
        // Kiểm tra loại Frame và chuyển đổi
        if (frame != null && frame.image != null) {
            int width = frame.imageWidth;
            int height = frame.imageHeight;

            // Tạo BufferedImage mới với không gian màu RGB
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            // Lấy dữ liệu pixel từ Frame (ByteBuffer)
            ByteBuffer byteBuffer = (ByteBuffer) frame.image[0].position(0);  // Lấy dữ liệu từ Frame
            int byteBufferSize = byteBuffer.remaining();  // Kiểm tra số byte còn lại trong byteBuffer

            // Đảm bảo có đủ byte dữ liệu để xử lý
            int expectedSize = width * height * 3; // Dữ liệu ảnh RGB cần 3 byte mỗi pixel (BGR sẽ cần 3 byte mỗi pixel)
            if (byteBufferSize < expectedSize) {
                System.out.println("Không đủ byte trong ByteBuffer để tạo ảnh. Dữ liệu không đầy đủ.");
                return null;  // Dừng lại nếu không đủ dữ liệu
            }

            byte[] byteArray = new byte[expectedSize]; // Tạo mảng byte đủ lớn
            byteBuffer.get(byteArray, 0, expectedSize);  // Lấy dữ liệu byte vào mảng

            int[] pixels = new int[width * height];

            // Giả sử dữ liệu ảnh là màu BGR (3 byte cho mỗi pixel), ta cần đổi BGR thành RGB
            for (int i = 0; i < byteArray.length / 3; i++) {
                int b = byteArray[i * 3] & 0xFF;     // Màu xanh dương (B)
                int g = byteArray[i * 3 + 1] & 0xFF; // Màu xanh lá (G)
                int r = byteArray[i * 3 + 2] & 0xFF; // Màu đỏ (R)

                // Đổi từ BGR sang RGB
                int rgb = (r << 16) | (g << 8) | b;   // Tạo RGB từ R, G, B
                pixels[i] = rgb;                      // Thêm giá trị vào mảng pixels
            }

            // Sao chép dữ liệu vào BufferedImage
            bufferedImage.setRGB(0, 0, width, height, pixels, 0, width);

            return bufferedImage;
        }
        return null;
    }

    private void drawGridOverlay() {
        // Kích thước của ImageView
        double width = imageView.getBoundsInParent().getWidth();
        double height = imageView.getBoundsInParent().getHeight();

        // Kiểm tra kích thước
        System.out.println("ImageView size: " + width + "x" + height);

        if (width <= 0 || height <= 0) {
            System.out.println("Kích thước ImageView không hợp lệ, không thể vẽ lưới.");
            return;
        }

        // Kích thước của Canvas giống với ImageView
        gridCanvas.setWidth(width);
        gridCanvas.setHeight(height);

        // Lấy GraphicsContext để vẽ
        GraphicsContext gc = gridCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, width, height); // Xóa canvas trước khi vẽ lại

        // Cài đặt màu và độ rộng của lưới
        gc.setStroke(javafx.scene.paint.Color.WHITESMOKE.deriveColor(0, 1, 1, 0.5));
        gc.setLineWidth(1);

        // Vẽ các đường ngang
        for (int i = 1; i < 3; i++) {
            double y = height * i / 3;
            gc.strokeLine(0, y, width, y);
        }

        // Vẽ các đường dọc
        for (int i = 1; i < 3; i++) {
            double x = width * i / 3;
            gc.strokeLine(x, 0, x, height);
        }

        System.out.println("Đã vẽ lưới 3x3 trên Canvas.");
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
        imageView.setScaleX(imageView.getScaleX() * 1.2);
        imageView.setScaleY(imageView.getScaleY() * 1.2);
    }

    @FXML
    public void zoomOutImage() {
        imageView.setScaleX(imageView.getScaleX() / 1.2);
        imageView.setScaleY(imageView.getScaleY() / 1.2);
    }





}
