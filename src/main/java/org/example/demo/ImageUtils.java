package org.example.demo;

import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

public class ImageUtils {
    public static Image byteArrayToImage(byte[] imageBytes) {
        if (imageBytes != null && imageBytes.length > 0) {
            return new Image(new ByteArrayInputStream(imageBytes));
        }
        return null; // Trả về null nếu mảng byte rỗng
    }
    public static byte[] imageToByteArray(Image image) {
        try {
            // Chuyển đổi Image (JavaFX) sang BufferedImage (AWT)
            BufferedImage bufferedImage = javafxImageToBufferedImage(image);

            // Ghi BufferedImage vào ByteArrayOutputStream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos); // Lưu dưới dạng PNG
            return baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static BufferedImage javafxImageToBufferedImage(Image image) {
        try {
            URL url = new URL(image.getUrl()); // Lấy URL của hình ảnh
            InputStream inputStream = url.openStream();
            return ImageIO.read(inputStream); // Đọc ảnh vào BufferedImage
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
