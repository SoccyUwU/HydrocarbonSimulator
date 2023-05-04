package com.example.hydrocarbonsimulator;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HelloApplication extends Application
{
    final static int screenWidth = 320;
    final static int screenHeight = 240;
    @Override
    public void start(Stage stage)
    {
        Canvas lewisCanvas = new Canvas(screenWidth, screenHeight);
        GraphicsContext lewisContext = lewisCanvas.getGraphicsContext2D();

        GridPane base = new GridPane();
        base.add(lewisCanvas, 0, 0);
        Scene scene = new Scene(base, screenWidth, screenHeight);
        stage.setTitle("test t");
        stage.setScene(scene);
        stage.show();

        Font elementFont = new Font(20);
        lewisContext.setFont(elementFont);
        lewisContext.strokeText("C", 50, 40);
    }

    public static void main(String[] args)
    {
        launch();
    }
}