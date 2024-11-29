package org.example.demo;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.io.ByteArrayInputStream;

public class AddBookCellFactory extends ListCell<Book> {
    @Override
    protected void updateItem(Book book, boolean empty) {
        super.updateItem(book, empty);

        if (empty || book == null) {
            setText(null);
            setGraphic(null);
        } else {
            // Tạo VBox để chứa các thông tin
            VBox vbox = new VBox(5);

            // Tiêu đề sách
            Text titleText = new Text(book.getTitle());
            titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            // Tác giả
            Text authorText = new Text("Tác giả: " + (book.getAuthor() != null ? book.getAuthor() : "N/A"));

            // Ngày xuất bản
            Text publishedDateText = new Text("Ngày xuất bản: " + (book.getPublishedDate() != null ? book.getPublishedDate() : "N/A"));

            // Thể loại
            Text genreText = new Text("Thể loại: " + (book.getGenre() != null ? book.getGenre() : "N/A"));

            // Mô tả
            Text descriptionText = new Text("Mô tả: " + (book.getDescription() != null ? book.getDescription() : "N/A"));

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
            imageView.setFitWidth(50);
            imageView.setFitHeight(75);

            // Sắp xếp các phần tử trong VBox
            vbox.getChildren().addAll(titleText, authorText, publishedDateText, genreText, descriptionText, imageView);

            // Thêm VBox vào ListCell
            setGraphic(vbox);
        }
    }
}
