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
        return null; 
    }

    public static byte[] imageToByteArray(Image image) {
        try {
            
            BufferedImage bufferedImage = javafxImageToBufferedImage(image);

            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos); 
            return baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static BufferedImage javafxImageToBufferedImage(Image image) {
        try {
            URL url = new URL(image.getUrl()); 
            InputStream inputStream = url.openStream();
            return ImageIO.read(inputStream); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
