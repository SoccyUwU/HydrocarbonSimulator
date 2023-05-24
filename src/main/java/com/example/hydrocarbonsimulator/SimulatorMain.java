package com.example.hydrocarbonsimulator;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.security.InvalidParameterException;
import java.util.ArrayList;

public class SimulatorMain extends Application
{
    final static int screenWidth = 320;
    final static int screenHeight = 240;

    /**
     * Constructs the main chain of carbons and bonds
     * @param com
     */
    public void constructMain(Compound com)
    {
        for(int i = 0; i < com.lengthMain; ++i)
        {
            Element temp = new Element("C");
            com.elements.add(temp);
        }
        for(int i = 0; i < com.lengthMain-1; ++i)
        {
            com.elements.get(i).bondWith(com.elements.get(i+1), com.bondNumbers.get(i));
        }
    }

    /**
     * Fills in the rest of the bonds with hydrogens
     * @param com
     */
    public void constructH(Compound com)
    {
        for(Element ele : com.elements)
        {
            if(ele.isElement("O") || ele.isElement("H"))
            {
                while (ele.getBonds().size() < 4)
                {
                    ele.bondWith(new Element("H"), 1);
                }
            }
        }
    }

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

        // placeholder for now: to test parsing before making ui
        String IUPACname = new String("but-1-ene");
        // bunch of variables to track properties
        Compound sample = new Compound();



        // if the compound ends in -ane: alkane
        if(IUPACname.endsWith("ane"))
        {
            // compounds ending in -ane are also incredibly simple
            sample.lengthMain = prefixToNum(IUPACname.substring(0, IUPACname.length()-3));
            for(int i = 0; i < sample.lengthMain - 1; ++i)
            {
                sample.bondNumbers.add(1);
            }
        }
        else if(IUPACname.endsWith("ene")) // alkene then
        {
            int end = IUPACname.lastIndexOf("-");
            int start = IUPACname.lastIndexOf("-", end-1);
            ArrayList<Integer> temp = parseCommaNumList(IUPACname.substring(start+1, end));
            // TODO: parse list of numbers separated by , here
            // update, parseCommaNumList is a wip
        }
        constructMain(sample);
        // TODO: 2023-05-23 Do other constructs dependent on type of hydrocarbon
        constructH(sample);
        System.out.println(removeSpace("   fuck  you  s "));
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
            case "hex": return 6;
            case "hept": return 7;
            case "oct": return 8;
            case "non": return 9;
            case "dec": return 10;
            default: throw new InvalidParameterException("Unknown prefix: only up to 10 is supported");
        }
    }

    public static ArrayList<Integer> parseCommaNumList(String str)
    { // well fuck me if it's not correct
        // TODO: 2023-05-23 Actually verify that the list is properly formatted
        ArrayList<Integer> temp = new ArrayList<Integer>();
        for(int i = 0; i < str.length(); i += 2)
        {
            temp.add(Integer.parseInt(str.substring(i, i+1)));
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