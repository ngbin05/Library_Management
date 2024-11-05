module com.tonevella.btl {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.tonevella.btl to javafx.fxml;
    exports com.tonevella.btl;
}