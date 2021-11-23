module com.example.virtualmachine {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.virtualmachine to javafx.fxml;
    exports com.example.virtualmachine;
    exports codeTableView;
    opens codeTableView to javafx.fxml;
    exports stackTableView;
    opens stackTableView to javafx.fxml;
}