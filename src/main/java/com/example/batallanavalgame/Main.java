package com.example.batallanavalgame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/example/batallanavalgame/views/menu.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 700, 700);
        stage.setTitle("Batalla Naval");
        stage.setScene(scene);
        stage.show();



    }
}

