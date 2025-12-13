module com.example.batallanavalgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens com.example.batallanavalgame to javafx.fxml;
    opens com.example.batallanavalgame.controllers to javafx.fxml;
    exports com.example.batallanavalgame;
    exports com.example.batallanavalgame.controllers;
}