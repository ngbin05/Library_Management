package org.example.demo;

import javafx.animation.FadeTransition;
import javafx.scene.control.ListCell;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;

public class AddBookCellFactory extends ListCell<Book> {

    private boolean isSearchResult = false; 

    @Override
    protected void updateItem(Book book, boolean empty) {
        super.updateItem(book, empty);

        if (empty || book == null) {
            setText(null);
            setGraphic(null);
        } else {
            
            HBox hbox = new HBox(15); 
            hbox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ddd; -fx-border-radius: 10px; -fx-padding: 10px;");
            
            ImageView imageView = new ImageView();
            if (book.getImage() != null && book.getImage().length > 0) {
                
                Image image = new Image(new ByteArrayInputStream(book.getImage()));
                imageView.setImage(image);
            } else {
                
                imageView.setImage(new Image(getClass().getResource("/media/no_image.jpg").toExternalForm()));
            }
            
            imageView.setFitWidth(100); 
            imageView.setFitHeight(150); 

            imageView.setStyle("-fx-border-color: #cccccc; -fx-border-width: 2px; -fx-background-radius: 5px;"); 

            
            DropShadow dropShadow = new DropShadow();
            dropShadow.setRadius(5);
            dropShadow.setOffsetX(3);
            dropShadow.setOffsetY(3);
            dropShadow.setColor(Color.GRAY);
            imageView.setEffect(dropShadow);

            
            VBox vbox = new VBox(10); 


            
            
            Text titleText = new Text(book.getTitle());
            titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");


            Text authorText = new Text("Tác giả: " + (book.getAuthor() != null ? book.getAuthor() : "N/A"));
            authorText.setStyle("-fx-font-size: 16px; -fx-font-family: 'Open Sans';");


            Text publishedDateText = new Text("Ngày xuất bản: " + (book.getPublishedDate() != null ? book.getPublishedDate() : "N/A"));
            publishedDateText.setStyle("-fx-font-size: 16px; -fx-font-family: 'Montserrat';");


            Text genreText = new Text("Thể loại: " + (book.getGenre() != null ? book.getGenre() : "N/A"));
            genreText.setStyle("-fx-font-size: 16px; -fx-font-family: 'Poppins';");


            Text descriptionText = new Text("Mô tả: " + (book.getDescription() != null ? book.getDescription() : "N/A"));
            descriptionText.setWrappingWidth(600);
            descriptionText.setTextAlignment(TextAlignment.LEFT);
            descriptionText.setStyle("-fx-font-size: 14px; -fx-font-family: 'Lato';");


            
            vbox.getChildren().addAll(titleText, authorText, publishedDateText, genreText, descriptionText);

            
            hbox.getChildren().addAll(imageView, vbox);

            if (!isSearchResult) {
                applyAnimation(hbox);
                isSearchResult = true;
            }

            setGraphic(hbox);

            selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    
                    hbox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #000000; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-padding: 10px;");
                } else {
                    
                    hbox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ddd; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-padding: 10px;");
                }
            });


            


        }
    }

    public void setSearchResult(boolean isSearchResult) {
        this.isSearchResult = isSearchResult; 
    }

    private void applyAnimation(HBox hbox) {
        
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), hbox);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);

        fadeTransition.play();
    }
}
