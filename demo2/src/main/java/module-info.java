module com.example.demo2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires fontawesomefx;
    requires mysql.connector.java;

    requires com.almasb.fxgl.all;
    requires java.sql;

    opens com.example.demo2 to javafx.fxml;
    exports com.example.demo2;
}