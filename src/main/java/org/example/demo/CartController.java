package org.example.demo;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartController {

    private static final List<Book> cart = new ArrayList<>();
    private BorrowController borrowController;
    private Stage stage;
    @FXML
    private Pane pane;
    @FXML
    private Label emptyLabel;
    @FXML
    private TableView<Borrowed> borrowTableView;
    @FXML
    private TableColumn<Borrowed, Integer> userIdColumn;
    @FXML
    private TableColumn<Borrowed, Integer> userNameColumn;
    @FXML
    private TableColumn<Borrowed, Integer> bookCountColumn; 
    @FXML
    private TableColumn<Borrowed, String> borrowDateColumn;
    @FXML
    private TableColumn<Borrowed, String> returnDateColumn;
    @FXML
    private TableColumn<Borrowed, String> statusColumn;
    @FXML
    private TableColumn<Borrowed, Void> actionColumn;
    @FXML
    private TextField txtSearchID;
    @FXML
    private TextField txtSearchUserName;
    @FXML
    private TextField txtSearchLoanDate;
    @FXML
    private TextField txtSearchStatus;
    @FXML
    private ListView<Book> listViewBooks;
    @FXML
    private Button clearButton;
    
    private final ObservableList<Borrowed> borrowList = FXCollections.observableArrayList();

    private static boolean isBookInCart(Book book) {
        return cart.stream().anyMatch(b -> b.getId() == book.getId());
    }

    public static boolean addBookIfNotInCart(Book book) {
        return BookCartManager.addBookIfNotInCart(book);
    }

    public static void remove(Book book) {
        BookCartManager.removeBook(book);
    }

    
    public void setBorrowController(BorrowController borrowController) {
        this.borrowController = borrowController;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void showBookList() {
        loadPage("bookList-view.fxml");
    }

    @FXML
    public void showReaderList() {
        if (BookCartManager.getCart().isEmpty()) {
            emptyLabel.setVisible(true);
        } else {
            loadPage("addTransaction-view.fxml");
        }
    }

    @FXML
    public void initialize() {
        listViewBooks.setCellFactory(listView -> new BookListCell());
        emptyLabel.setVisible(false);

        
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("user_name"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        statusColumn.setCellFactory(column -> new TableCell<Borrowed, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);

                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    if ("BORROWING".equalsIgnoreCase(status)) {
                        setStyle("-fx-text-fill: #f2ce03; -fx-font-weight: bold;");
                    } else if ("RETURNED".equalsIgnoreCase(status)) {
                        setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    } else if ("OVERDUE".equalsIgnoreCase(status)) {
                        setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    }
                }
            }
        });

        
        bookCountColumn.setCellValueFactory(param -> {
            Borrowed borrowed = param.getValue();
            return new javafx.beans.property.SimpleIntegerProperty(borrowed.getBooks().size()).asObject();
        });

        actionColumn.setCellFactory(column -> {
            return new TableCell<Borrowed, Void>() {
                private final javafx.scene.control.Button returnButton = new javafx.scene.control.Button("Trả sách");
                private final javafx.scene.control.Label checkMarkLabel = new javafx.scene.control.Label("✔");

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null); 
                    } else {
                        Borrowed borrowed = getTableView().getItems().get(getIndex());

                        
                        if ("RETURNED".equals(borrowed.getStatus())) {
                            returnButton.setVisible(false);
                            checkMarkLabel.setVisible(true);
                            setGraphic(checkMarkLabel); 
                        } else {
                            returnButton.setVisible(true);
                            checkMarkLabel.setVisible(false);
                            setGraphic(returnButton); 

                            
                            returnButton.setOnAction(event -> {
                                boolean isReturned = handleReturnBook(borrowed); 
                                if (isReturned) {
                                    
                                    returnButton.setVisible(false); 
                                    checkMarkLabel.setVisible(true); 
                                    borrowed.setStatus("RETURNED"); 
                                    
                                    borrowTableView.refresh();
                                }
                            });
                        }
                    }
                }
            };
        });

        clearButton.setOnAction(event -> {
            cart.clear();
            listViewBooks.getItems().clear();
            System.out.println("The entire bookcart has been deleted!");
        });


        
        borrowTableView.setItems(borrowList);
        borrowTableView.setFixedCellSize(-1);

        loadBorrowData();
        updateListView();
    }

    private void updateListView() {
        listViewBooks.getItems().setAll(BookCartManager.getCart());
    }

    private void loadBorrowData() {
        try {
            
            List<Borrowed> borrowDataList = MySQLDatabase.getBorrowDatabase().getAllBorrowData();

            
            this.borrowList.clear();
            this.borrowList.addAll(borrowDataList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean handleReturnBook(Borrowed borrowed) {
        try {
            boolean confirmed = ConfirmDialog.show("Confirm", "Confirm the return of the book?");
            if (!confirmed) {
                return false;  
            }

            
            boolean isReturned = MySQLDatabase.getBorrowDatabase().returnBook(borrowed.getBorrowId(), borrowed.getBooks());
            if (isReturned) {
                System.out.println("Successful Return to Borrow ID:" + borrowed.getBorrowId());
                
                loadBorrowData();
                borrowTableView.refresh(); 
                return true; 
            } else {
                System.out.println("Return the book failed!");
                return false; 
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; 
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
            if (controller != null) {
                if (controller instanceof ListReaderController listReaderController) {
                    listReaderController.setStage((Stage) pane.getScene().getWindow());
                } else if (controller instanceof ListBookController listBookController) {
                    listBookController.setStage((Stage) pane.getScene().getWindow());
                } else if (controller instanceof BorrowController borrowController) {
                    borrowController.setStage((Stage) pane.getScene().getWindow());
                }
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearchBorrowRecords() {
        
        String borrowId = txtSearchID.getText().trim();
        String userName = txtSearchUserName.getText().trim();
        String loanDate = txtSearchLoanDate.getText().trim();
        String status = txtSearchStatus.getText().trim();

        try {
            ObservableList<Borrowed> searchResults;

            
            if (borrowId.isEmpty() && userName.isEmpty() && loanDate.isEmpty() && status.isEmpty()) {
                
                searchResults = FXCollections.observableArrayList(MySQLDatabase.getBorrowDatabase().getAllBorrowData());
            } else {
                
                searchResults = MySQLDatabase.getBorrowDatabase().searchBorrowRecords(borrowId, userName, loanDate, status);
            }

            
            borrowTableView.setItems(searchResults);

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    static class BookListCell extends javafx.scene.control.ListCell<Book> {
        private final HBox content = new HBox(10); 
        private final ImageView bookImage = new ImageView(); 
        private final VBox details = new VBox(); 
        private final Button removeButton = new Button("Detele"); 

        public BookListCell() {
            
            bookImage.setFitWidth(50);
            bookImage.setFitHeight(50);

            
            removeButton.setStyle("-fx-background-color: #FF6B6B; -fx-text-fill: white; -fx-font-size: 14px; -fx-shape: \"M 0,0 L 1,0 L 1,1 L 0,1 Z\"; -fx-background-radius: 15px; -fx-border-radius: 15px;");
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