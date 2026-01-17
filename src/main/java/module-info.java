module com.example.mill {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens Controllers to javafx.fxml;
    opens MainApp to javafx.graphics;

    exports MainApp;
    exports Controllers;
    exports Database;
    exports Board;
    exports GameLogic;
    exports Players;
    exports Navigator;
    exports Network;
}