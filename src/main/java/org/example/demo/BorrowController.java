package org.example.demo;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BorrowController {

    private static final List<Book> cart = new ArrayList<>(); 
    private Stage stage;
    @FXML
    private ListView<Book> listViewBooks; 
    @FXML
    private Label nameLabel; 
    @FXML
    private Label idLabel; 
    @FXML
    private Label borrowDateLabel;
    @FXML
    private TextField borrowDaysField;
    @FXML
    private Label returnDateLabel;
    @FXML
    private Label unBookErrorLabel;
    @FXML
    private Label unDateErrorLabel;
    @FXML
    private Label borrowSuccessLabel;
    @FXML
    private Pane pane;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static boolean addBookIfNotInCart(Book book) {
        if (!isBookInCart(book)) {
            cart.add(book);
            return true; 
        }
        return false; 
    }

    public static void remove(Book book) {
        BookCartManager.removeBook(book);
    }

    private static boolean isBookInCart(Book book) {
        return cart.stream().anyMatch(b -> b.getId() == book.getId());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void showCartView() {
        loadPage("cart-view.fxml");
    }

    @FXML
    public void back() {
        loadPage("addTransaction-view.fxml");
    }

    
    @FXML
    public void initialize() {
        pane.setVisible(false);
        unBookErrorLabel.setVisible(false);
        unDateErrorLabel.setVisible(false);
        borrowSuccessLabel.setVisible(false);

        
        listViewBooks.setCellFactory(listView -> new BookListCell());

        
        updateListView();

        
        LocalDate today = LocalDate.now();
        borrowDateLabel.setText("From: " + today.format(formatter));

        
        borrowDaysField.textProperty().addListener((observable, oldValue, newValue) -> updateReturnDate());
    }

    public void setNameLabel(String name) {
        nameLabel.setText("Tên: " + name);
    }

    public void setIdLabel(int id) {
        idLabel.setText("ID: " + id);
    }

    public List<Book> getCartBooks() {
        return BookCartManager.getCart();
    }

    private void updateListView() {
        listViewBooks.getItems().setAll(BookCartManager.getCart());
    }

    private void updateReturnDate() {
        try {
            int borrowDays = Integer.parseInt(borrowDaysField.getText());
            LocalDate borrowDate = LocalDate.parse(borrowDateLabel.getText().split(": ")[1], formatter);
            LocalDate returnDate = borrowDate.plusDays(borrowDays);
            returnDateLabel.setText("To: " + returnDate.format(formatter));
        } catch (NumberFormatException e) {
            returnDateLabel.setText("To: ");
        }
    }

    @FXML
    public void borrow() {
        if (BookCartManager.getCart().isEmpty()) {
            unBookErrorLabel.setVisible(true);
            unDateErrorLabel.setVisible(false);
            borrowSuccessLabel.setVisible(false);
            return;
        }

        int userId = Integer.parseInt(idLabel.getText().split(": ")[1]);
        String[] dateParts = returnDateLabel.getText().split(": ");
        if (dateParts.length < 2) {
            unDateErrorLabel.setVisible(true);
            unBookErrorLabel.setVisible(false);
            return;
        }

        String borrowDate = borrowDateLabel.getText().split(": ")[1];
        String returnDate = returnDateLabel.getText().split(": ")[1];

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate borrowLocalDate = LocalDate.parse(borrowDate, formatter);
        LocalDate returnLocalDate = LocalDate.parse(returnDate, formatter);

        if (returnLocalDate.isBefore(borrowLocalDate)) {
            unDateErrorLabel.setVisible(true);
            unBookErrorLabel.setVisible(false);
            borrowSuccessLabel.setVisible(false);
            return;
        }

        java.sql.Date sqlBorrowDate = java.sql.Date.valueOf(borrowLocalDate);
        java.sql.Date sqlReturnDate = java.sql.Date.valueOf(returnLocalDate);

        List<Integer> bookIds = new ArrayList<>();
        for (Book book : BookCartManager.getCart()) {
            bookIds.add(book.getId());
        }

        boolean success = MySQLDatabase.getBorrowDatabase().insertBorrowRecord(userId, bookIds, sqlBorrowDate, sqlReturnDate);

        if (success) {
            borrowSuccessLabel.setVisible(true);
            unBookErrorLabel.setVisible(false);
            unDateErrorLabel.setVisible(false);
            BookCartManager.getCart().clear();
            updateListView();
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

            Object controller = loader.getController();
            if (controller instanceof CartController cartController) {

                cartController.setStage((Stage) pane.getScene().getWindow());
                cartController.setBorrowController(this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class BookListCell extends javafx.scene.control.ListCell<Book> {
        private final HBox content = new HBox(10);
        private final ImageView bookImage = new ImageView();
        private final VBox details = new VBox();
        private final Button removeButton = new Button("Xóa");

        public BookListCell() {
            bookImage.setFitWidth(50);
            bookImage.setFitHeight(50);

            removeButton.setStyle("-fx-background-color: #FF6B6B; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 15px; -fx-border-radius: 15px;");
            removeButton.setOnAction(event -> {
                Book book = getItem();
                if (book != null) {
                    BorrowController.remove(book); 
                    getListView().getItems().remove(book); 
                }
            });

            content.getChildren().addAll(bookImage, details, removeButton);
        }

        @Override
        protected void updateItem(Book book, boolean empty) {
            super.updateItem(book, empty);

            if (empty || book == null) {
                setGraphic(null);
            } else {
                if (book.getImage() != null) {
                    bookImage.setImage(new Image(new ByteArrayInputStream(book.getImage())));
                } else {
                    bookImage.setImage(null); 
                }

                details.getChildren().setAll(
                        new Text("Name: " + book.getTitle()),
                        new Text("Author: " + book.getAuthor()),
                        new Text("Publisher: " + book.getPublisher()),
                        new Text("Publication Date: " + book.getPublishedDate())
                );

                setGraphic(content);
            }
        }
    }
}
