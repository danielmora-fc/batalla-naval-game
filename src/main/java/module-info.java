module com.example.batallanavalgame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.batallanavalgame to javafx.fxml;
    exports com.example.batallanavalgame;
}