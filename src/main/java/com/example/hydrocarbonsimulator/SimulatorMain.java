package com.example.hydrocarbonsimulator;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.security.InvalidParameterException;
import java.util.ArrayList;

public class SimulatorMain extends Application
{
    final static int screenWidth = 1000;
    final static int screenHeight = 800;

    @Override
    public void start(Stage stage)
    {
        Canvas lewisCanvas = new Canvas(screenWidth, screenHeight);
        GraphicsContext lewisContext = lewisCanvas.getGraphicsContext2D();

        GridPane base = new GridPane();
        base.add(lewisCanvas, 0, 0);
        Scene scene = new Scene(base, screenWidth, screenHeight);
        stage.setTitle("test com");
        stage.setScene(scene);
        stage.show();

        Font elementFont = new Font(20);
        lewisContext.setFont(elementFont);
        lewisContext.setFill(Color.LIGHTPINK);
        lewisContext.fillRect(0, 0, screenWidth, screenHeight);

        // placeholder for now: to test parsing before making ui
        String IUPACname = new String("hept-1,2-ene-5-yne");
        Compound sample = new Compound();

        IUPACname = removeSpace(IUPACname);
        sample.name = IUPACname;
        lewisContext.strokeText("IUPAC name: " + IUPACname, 0, screenHeight-20);

        sample.parseMainPath();
        sample.populateH();
        sample.draw(lewisContext);

        // debug
        System.out.println(sample.bondNumbers);
    }

    public static void main(String[] args)
    {
        launch();
    }

    public static int prefixToNum (String prefix) throws InvalidParameterException
    {
        switch(prefix)
        {
            case "meth": return 1;
            case "eth": return 2;
            case "prop": return 3;
            case "but": return 4;
            case "pent": return 5;
            case "hex":
            case "hexa": return 6;
            case "hept": return 7;
            case "oct": return 8;
            case "non": return 9;
            case "dec": return 10;
            default: throw new InvalidParameterException("Unknown prefix: only up to 10 is supported");
        }
    }

    public static ArrayList<Integer> parseCommaNumList(String str) throws IllegalArgumentException
    {
        ArrayList<Integer> temp = new ArrayList<>();
        for(int i = 0; i < str.length(); ++i)
        {
            if(i % 2 == 0 && Character.isDigit(str.charAt(i)))
            {
                temp.add(Integer.parseInt(str.substring(i, i + 1)));
            }
            else if(i % 2 == 1 && str.charAt(i) == ',')
            {
                ;
            }
            else
            {
                throw new IllegalArgumentException("numbering isn't valid: alternate between digits and commas");
            }
        }
        return temp;
    }

    public static String removeSpace(String str)
    {
        String temp = new String(str);
        temp = temp.replaceAll("\s","");
        return temp;
    }
}