package com.example.batallanavalgame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.geometry.Bounds;

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

    public AnchorPane buildShip(String type, int orientation){
        int valueOfBox = 20;
        int dimensions = 0;

        switch (type){
            case "PORTAAVIONES":
                dimensions = 4;
                break;
            case "SUBMARINO":
                dimensions = 3;
                break;
            case "DESTRUCTOR":
                dimensions = 2;
                break;
            case "FRAGATA":
                dimensions = 1;
                break;
        }

        AnchorPane canva = new AnchorPane();
        canva.setStyle("-fx-background-color: #3498db;");

        int width = valueOfBox;
        int height = dimensions * valueOfBox;

        // build elements for ship
        // En esta seccion podemos completar el diseño del barco por ahora es un cuadrado simple.
        Rectangle body = new Rectangle(width, height);
        Circle proa = new Circle(5, Color.RED);

        int valueOfOrientation = ((orientation % 4) + 4) % 4; // normalize to 0..3
        int angle = valueOfOrientation * 90;

        Group ship = new Group(body, proa);
        ship.setRotate(angle);


        body.setX(0);
        body.setY(0);

        proa.setCenterY(5);
        proa.setCenterX(width / 2.0);

        canva.getChildren().add(ship);

        Bounds b = ship.getBoundsInParent();
        ship.setTranslateX(-b.getMinX());
        ship.setTranslateY(-b.getMinY());

        Bounds b2 = ship.getBoundsInParent();
        canva.setPrefSize(b2.getWidth(), b2.getHeight());
        return canva;
    }

    private void enableDrag(AnchorPane node, AnchorPane parent) {
        final double[] mouseAnchor = new double[2];
        final double[] initialLayout = new double[2];

        node.setOnMousePressed(e -> {
            mouseAnchor[0] = e.getSceneX();
            mouseAnchor[1] = e.getSceneY();
            initialLayout[0] = node.getLayoutX();
            initialLayout[1] = node.getLayoutY();
            node.toFront();
        });

        node.setOnMouseDragged(e -> {
            double deltaX = e.getSceneX() - mouseAnchor[0];
            double deltaY = e.getSceneY() - mouseAnchor[1];

            double newX = initialLayout[0] + deltaX;
            double newY = initialLayout[1] + deltaY;

            double nodeWidth = node.getBoundsInParent().getWidth();
            double nodeHeight = node.getBoundsInParent().getHeight();

            double maxX = Math.max(0, parent.getWidth() - nodeWidth);
            double maxY = Math.max(0, parent.getHeight() - nodeHeight);

            newX = Math.min(Math.max(0, newX), maxX);
            newY = Math.min(Math.max(0, newY), maxY);

            node.setLayoutX(newX);
            node.setLayoutY(newY);
        });
    }

    private void enableRightClickRotate(AnchorPane node, AnchorPane parent) {
        node.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                if (node.getChildren().isEmpty()) return;
                Group ship = (Group) node.getChildren().get(0);
                ship.setTranslateX(0);
                ship.setTranslateY(0);
                ship.setRotate((ship.getRotate() + 90) % 360);
                Bounds b = ship.getBoundsInParent();
                ship.setTranslateX(-b.getMinX());
                ship.setTranslateY(-b.getMinY());
                Bounds b2 = ship.getBoundsInParent();
                node.setPrefSize(b2.getWidth(), b2.getHeight());

                double maxX = Math.max(0, parent.getWidth() - b2.getWidth());
                double maxY = Math.max(0, parent.getHeight() - b2.getHeight());
                double newX = Math.min(Math.max(0, node.getLayoutX()), maxX);
                double newY = Math.min(Math.max(0, node.getLayoutY()), maxY);
                node.setLayoutX(newX);
                node.setLayoutY(newY);
                e.consume();
            }
        });
    }
}

