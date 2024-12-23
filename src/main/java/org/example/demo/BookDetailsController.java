package org.example.demo;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class BookDetailsController {
    private Stage stage;
    private Book book;
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
    @FXML
    private Label addSuccessLabel;
    @FXML
    private Label addFailureLabel;
    @FXML
    private Pane pane;

    public void setBook(Book book) {
        this.book = book;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        addSuccessLabel.setVisible(false);
        addFailureLabel.setVisible(false);
        pane.setVisible(false);
    }

    public void displayBookDetails(Book book) {
        bookTitleLabel.setText("Book name: " + book.getTitle());
        bookAuthorLabel.setText("Author: " + book.getAuthor());
        bookGenreLabel.setText("Category: " + book.getGenre());
        bookPublishedDateLabel.setText("Publication Date: " + book.getPublishedDate());
        bookDescriptionLabel.setText("Description: " + book.getDescription());
        bookIsbnLabel.setText("ISBN: " + book.getIsbn());

        
        if (book.getImage() != null) {
            Image image = new Image(new ByteArrayInputStream(book.getImage()));
            bookImageView.setImage(image);
        } else {
            bookImageView.setImage(new Image(getClass().getResourceAsStream("/media/no_image.jpg")));
        }
    }

    @FXML
    private void addBookToCart() {
        boolean flag = CartController.addBookIfNotInCart(book);
        if (flag) {
            addSuccessLabel.setVisible(true);
            addFailureLabel.setVisible(false);
        } else {
            addSuccessLabel.setVisible(false);
            addFailureLabel.setVisible(true);
        }
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClose() {
        loadPage("bookList-view.fxml");
    }

    @FXML
    private void goToCart() {
        loadPage("cart-view.fxml");
    }
}