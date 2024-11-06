module org.example.btloop {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.btloop to javafx.fxml;
    exports org.example.btloop;
}