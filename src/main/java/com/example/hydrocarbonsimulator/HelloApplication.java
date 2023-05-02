package com.example.hydrocarbonsimulator;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class HelloApplication extends Application
{
    @Override
    public void start(Stage stage)
    {
        GridPane base = new GridPane();
        Scene scene = new Scene(base, 320, 240);
        stage.setTitle("test t");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }
}