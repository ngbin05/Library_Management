package org.example.demo;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.Region;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;

public class AddBookCellFactory extends ListCell<Book> {

    private boolean isSearchResult = false; // Cờ để kiểm tra trạng thái tìm kiếm

    @Override
    protected void updateItem(Book book, boolean empty) {
        super.updateItem(book, empty);

        if (empty || book == null) {
            setText(null);
            setGraphic(null);
        } else {
            // Tạo HBox để chứa ảnh và thông tin sách
            HBox hbox = new HBox(15); // Khoảng cách giữa ảnh và thông tin (15px để rộng hơn)
            hbox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ddd; -fx-border-radius: 10px; -fx-padding: 10px;");
            // Ảnh bìa
            ImageView imageView = new ImageView();
            if (book.getImage() != null && book.getImage().length > 0) {
                // Chuyển byte[] thành Image
                Image image = new Image(new ByteArrayInputStream(book.getImage()));
                imageView.setImage(image);
            } else {
                // Nếu không có ảnh, dùng ảnh mặc định
                imageView.setImage(new Image(getClass().getResource("/media/no_image.jpg").toExternalForm()));
            }
            // Tăng kích thước ảnh lên
            imageView.setFitWidth(100); // Tăng kích thước ảnh chiều rộng
            imageView.setFitHeight(150); // Tăng kích thước ảnh chiều cao

            imageView.setStyle("-fx-border-color: #cccccc; -fx-border-width: 2px; -fx-background-radius: 5px;"); // Viền xung quanh ảnh, màu xám nhạt, độ dày 2px và bo góc

            // Thêm hiệu ứng đổ bóng nhẹ cho ảnh
            DropShadow dropShadow = new DropShadow();
            dropShadow.setRadius(5);
            dropShadow.setOffsetX(3);
            dropShadow.setOffsetY(3);
            dropShadow.setColor(Color.GRAY);
            imageView.setEffect(dropShadow);

            // Tạo VBox để chứa thông tin sách bên phải ảnh
            VBox vbox = new VBox(10); // Tăng khoảng cách giữa các phần tử trong VBox



            // Tiêu đề sách
            // Tiêu đề sách
            Text titleText = new Text(book.getTitle());
            titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");

// Tác giả
            Text authorText = new Text("Tác giả: " + (book.getAuthor() != null ? book.getAuthor() : "N/A"));
            authorText.setStyle("-fx-font-size: 16px; -fx-font-family: 'Open Sans';");

// Ngày xuất bản
            Text publishedDateText = new Text("Ngày xuất bản: " + (book.getPublishedDate() != null ? book.getPublishedDate() : "N/A"));
            publishedDateText.setStyle("-fx-font-size: 16px; -fx-font-family: 'Montserrat';");

// Thể loại
            Text genreText = new Text("Thể loại: " + (book.getGenre() != null ? book.getGenre() : "N/A"));
            genreText.setStyle("-fx-font-size: 16px; -fx-font-family: 'Poppins';");

// Mô tả
            Text descriptionText = new Text("Mô tả: " + (book.getDescription() != null ? book.getDescription() : "N/A"));
            descriptionText.setWrappingWidth(600);
            descriptionText.setTextAlignment(TextAlignment.LEFT);
            descriptionText.setStyle("-fx-font-size: 14px; -fx-font-family: 'Lato';");


            // Thêm các phần tử vào VBox
            vbox.getChildren().addAll(titleText, authorText, publishedDateText, genreText, descriptionText);

            // Thêm ảnh và VBox vào HBox
            hbox.getChildren().addAll(imageView, vbox);

            if (!isSearchResult) {
                applyAnimation(hbox);// Đánh dấu hiệu ứng đã được áp dụng
                isSearchResult = true;
            }

            setGraphic(hbox);

            selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    // Nếu mục được chọn, thay đổi màu viền thành màu đen
                    hbox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #000000; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-padding: 10px;");
                } else {
                    // Nếu mục không được chọn, giữ màu viền mặc định
                    hbox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ddd; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-padding: 10px;");
                }
            });


            // Thêm HBox vào ListCell




        }
    }

    public void setSearchResult(boolean isSearchResult) {
        this.isSearchResult = isSearchResult; // Cập nhật cờ trạng thái
    }

    private void applyAnimation(HBox hbox) {
        // Hiệu ứng Fade In
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), hbox);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);

        fadeTransition.play();
    }
}
