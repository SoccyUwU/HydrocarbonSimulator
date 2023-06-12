/**
 * Invalid Names will result in Unspecified Behaviour
 */
package com.example.hydrocarbonsimulator;

import javafx.application.Application;
import javafx.event.*;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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
        GridPane base = new GridPane();
        Scene scene = new Scene(base, screenWidth, screenHeight);

        TextField nameBox = new TextField("Input hydrocarbon name here");
        Button confirmButton = new Button("Generate");

        HBox inputBox = new HBox();
        inputBox.getChildren().addAll(nameBox, confirmButton);
        base.add(inputBox, 0, 0);

        Canvas lewisCanvas = new Canvas(screenWidth, screenHeight-nameBox.getHeight());
        GraphicsContext lewisContext = lewisCanvas.getGraphicsContext2D();
        base.add(lewisCanvas, 0, 1);

        stage.setTitle("test com");
        stage.setScene(scene);
        stage.show();

        Font elementFont = new Font(20);
        lewisContext.setFont(elementFont);
        lewisContext.setFill(Color.LIGHTPINK);
        lewisContext.fillRect(0, 0, lewisCanvas.getWidth(), lewisCanvas.getHeight());

        EventHandler<ActionEvent> genNew = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                // placeholder name
                String IUPACname = new String("hept-1,2-ene-5-yne");
                Compound sample = new Compound();
                IUPACname = nameBox.getText();
                IUPACname = removeSpace(IUPACname);
                sample.name = IUPACname;

                if (sample.name.isEmpty())
                {
                    lewisContext.fillRect(0, 0, lewisCanvas.getWidth(), lewisCanvas.getHeight());
                    lewisContext.strokeText("Please enter something",
                            lewisCanvas.getWidth() / 2, lewisCanvas.getHeight() / 2);
                    return;
                }

                sample.parseMainPath();
                sample.populateH();
                //DEBUG
                for(Element ele : sample.elements)
                {
                    System.out.printf("Element %s has final bonds:\n", ele.getName());
                    for(BondInfo th : ele.getBonds())
                    {
                        System.out.println(th.end().getName()+th.number());
                    }
                }
                sample.draw(lewisContext);
            }
        };
        confirmButton.setOnAction(genNew);
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
            case "octa":
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