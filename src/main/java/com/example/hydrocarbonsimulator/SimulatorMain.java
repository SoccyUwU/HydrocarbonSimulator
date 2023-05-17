package com.example.hydrocarbonsimulator;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

public class SimulatorMain extends Application
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

        // placeholder for now: to test parsing before making ui
        String IUPACname = new String("but-1-ene");
        // bunch of variables to track properties
        boolean alkane = false;
        ArrayList<Integer> doubleBonds = new ArrayList<Integer>();
        ArrayList<Integer> tripleBonds = new ArrayList<Integer>();

        // if the element ends in -ane: alkane
        if(IUPACname.endsWith("ane"))
        {
            alkane = true;
        }
        else if(IUPACname.endsWith("ene")) // alkene then
        {
            int end = IUPACname.lastIndexOf("-");
            int start = IUPACname.lastIndexOf("-", end-1);
            // todo: parse list of numbers separated by , here
        }
    }

    public static void main(String[] args)
    {
        launch();
    }
}