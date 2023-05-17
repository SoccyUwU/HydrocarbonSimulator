package com.example.hydrocarbonsimulator;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Objects;

public class Element
{
    // all elements will be drawn on the same canvas, the context of which is here
    private static GraphicsContext elementContext = null;
    // note that keys are shorter (C, Ca, H) while values are full (Carbon, Calcium)
    private static DualMap<String, String> nameList = new DualMap<String, String>();
    // full name is always fully lower case
    private String fullName = "morbium";
    // abbreviation name is according to the laws
    private String name = "M";

    private String abbreviation(String full) throws IllegalArgumentException
    {
        return "work in progress: wait for handmade map";
    }

    public Element(String name) throws IllegalArgumentException
    {
        // if the dictionary isn't initialized: populate it
        if(nameList.empty())
        {
            // populate some bitches
            nameList.add("C", "Carbon");
            nameList.add("H", "Hydrogen");
            nameList.add("O", "Oxygen");
            nameList.add("F", "Fluorine");
            nameList.add("Cl", "Chlorine");
            nameList.add("Br", "Bromine");
            nameList.add("I", "Iodine");
        }
        // if the name is short: has to be abbreviation
        if(name.length() <= 2)
        {
            String longName = nameList.seekByKey(name);
            if(longName == null)
            {
                throw new IllegalArgumentException("That's not even an element that goes in hydrocarbons");
            }
            this.name = name;
            this.fullName = longName;
        }
        else // otherwise it's full name
        {
            String shortName = nameList.seekByValue(name);
            if(shortName == null)
            {
                throw new IllegalArgumentException("Not an element of concern");
            }
            this.fullName = name;
            this.name = shortName;
        }
    }

    public static void setElementContext(GraphicsContext context)
    {
        elementContext = context;
    }
}
