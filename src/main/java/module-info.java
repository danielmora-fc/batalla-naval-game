module com.example.batallanavalgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.fasterxml.jackson.databind;


    opens com.example.batallanavalgame to javafx.fxml, com.fasterxml.jackson.databind;
    opens com.example.batallanavalgame.controllers to javafx.fxml;
    opens com.example.batallanavalgame.models to com.fasterxml.jackson.databind;
    exports com.example.batallanavalgame;
    exports com.example.batallanavalgame.controllers;
    exports com.example.batallanavalgame.models;
    exports com.example.batallanavalgame.services;
    exports com.example.batallanavalgame.services.strategy;
}