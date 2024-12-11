package org.example.demo;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MenuController {
    private Stage stage;
    @FXML
    private Pane pane;
    @FXML
    private Label dateTimeLabel;
    @FXML
    private Label readersCount;
    @FXML
    private Label booksCount;
    @FXML
    private Label booksBorrowCount;
    @FXML
    private Label hi;
    @FXML
    private Button exitButton;
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
    
    private final ObservableList<Borrowed> borrowList = FXCollections.observableArrayList();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void showReaderList() {
        loadPage("readers-view.fxml");
    }

    @FXML
    public void showBookList() {
        loadPage("bookList-view.fxml");
    }

    @FXML
    public void showProfile() {
        loadPage("profile-view.fxml");
    }

    @FXML
    public void showDashBoard() {
        loadPage("dashboard-view.fxml");
    }

    @FXML
    public void showCartView() {
        loadPage("cart-view.fxml");
    }

    @FXML
    public void initialize() {
        pane.setVisible(false);
        sayHi();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            LocalDateTime now = LocalDateTime.now();
            dateTimeLabel.setText(now.format(formatter));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        exitButton.setOnAction(event -> {
            System.exit(0);
        });


        try {
            MySQLDatabase.getBorrowDatabase().updateOverdueStatus();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        loadBorrowData();

        
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
                private final javafx.scene.control.Button returnButton = new javafx.scene.control.Button("Return");
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


        
        borrowTableView.setItems(borrowList);
        borrowTableView.setFixedCellSize(-1); 


        showInforLabel();

    }

    private void sayHi() {
        String userName = MySQLDatabase.getUserDatabase().getFullName(LoginController.account.getUsername());
        if (userName != null) {
            hi.setText("Hi, " + userName);
        }
    }


    @FXML
    private void logOut() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Parent root = loader.load();

            FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), root);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setToValue(1.0);
            fadeTransition.play();

            Scene loginScene = new Scene(root);
            loginScene.setFill(Color.TRANSPARENT);
            stage.setScene(loginScene);
            LoginController loginController = loader.getController();
            loginController.setStage(stage);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
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
                } else if (controller instanceof ProfileController profileController) {
                    profileController.setStage((Stage) pane.getScene().getWindow());
                } else if (controller instanceof BorrowController borrowController) {
                    borrowController.setStage((Stage) pane.getScene().getWindow());
                }
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            boolean confirmed = ConfirmDialog.show("Confirm", "Confirm to return book?");
            if (!confirmed) {
                return false;  
            }

            
            boolean isReturned = MySQLDatabase.getBorrowDatabase().returnBook(borrowed.getBorrowId(), borrowed.getBooks());
            if (isReturned) {
                System.out.println("Trả sách thành công cho Borrow ID: " + borrowed.getBorrowId());
                
                loadBorrowData();
                borrowTableView.refresh(); 
                showInforLabel();
                return true; 
            } else {
                System.out.println("Trả sách thất bại!");
                return false; 
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; 
        }
    }

    void showInforLabel() {
        int userCount = MySQLDatabase.getUserDatabase().countUsers();
        readersCount.setText(String.valueOf(userCount));

        int bookCount = MySQLDatabase.getBookDatabase().countBooks();
        booksCount.setText(String.valueOf(bookCount));

        int bookBorrowCount = MySQLDatabase.getBorrowDatabase().countBooksBorrow();
        booksBorrowCount.setText(String.valueOf(bookBorrowCount));
    }
}

