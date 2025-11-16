module com.example.mill {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;

    opens MainApp to javafx.fxml;
    exports MainApp to javafx.graphics;
    opens Controllers to javafx.fxml;

}