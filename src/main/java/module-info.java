module com.example.virtualmachine {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.virtualmachine to javafx.fxml;
    exports com.example.virtualmachine;
}