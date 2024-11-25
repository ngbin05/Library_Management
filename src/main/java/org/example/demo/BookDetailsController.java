package org.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;

public class BookDetailsController {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private Label bookTitleLabel;
    @FXML
    private Label bookAuthorLabel;
    @FXML
    private Label bookGenreLabel;
    @FXML
    private Label bookPublishedDateLabel;
    @FXML
    private Label bookDescriptionLabel;
    @FXML
    private Label bookIsbnLabel;
    @FXML
    private ImageView bookImageView;

    public void displayBookDetails(Book book) {
        bookTitleLabel.setText("Tên sách: " + book.getTitle());
        bookAuthorLabel.setText("Tác giả: " + book.getAuthor());
        bookGenreLabel.setText("Thể loại: " + book.getGenre());
        bookPublishedDateLabel.setText("Ngày xuất bản: " + book.getPublishedDate());
        bookDescriptionLabel.setText("Mô tả: " + book.getDescription());
        bookIsbnLabel.setText("ISBN: " + book.getIsbn());

        // Hiển thị ảnh (nếu có)
        if (book.getImage() != null) {
            Image image = new Image(new ByteArrayInputStream(book.getImage()));
            bookImageView.setImage(image);
        } else {
            bookImageView.setImage(new Image(getClass().getResourceAsStream("/media/no_image.png")));
        }
    }

    @FXML
    private void handleClose() {
    stage.close();
    }
}