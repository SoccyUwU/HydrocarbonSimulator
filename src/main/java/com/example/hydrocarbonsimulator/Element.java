package com.example.hydrocarbonsimulator;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class Element
{

    // in hydrocarbons we've learned, an element only connects to at most four other
    private ArrayList<bondInfo> bonds = new ArrayList<>();
    // all elements will be drawn on the same canvas, the context of which is here
    private static GraphicsContext elementContext = null;
    // note that keys are shorter (C, Ca, H) while values are full (Carbon, Calcium)
    private static DualMap<String, String> nameList = new DualMap<String, String>();
    // full name is always fully lower case
    private String fullName = "morbium";
    // abbreviation name is according to the laws
    private String name = "M";

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
                throw new IllegalArgumentException("Unknown abbreviation: " + name);
            }
            this.name = name;
            this.fullName = longName;
        }
        else // otherwise it's full name
        {
            String shortName = nameList.seekByValue(name);
            if(shortName == null)
            {
                throw new IllegalArgumentException("Unknown name: " + name);
            }
            this.fullName = name;
            this.name = shortName;
        }
    }

    public static void setElementContext(GraphicsContext context)
    {
        elementContext = context;
    }

    public void draw(int width, int height)
    {
        elementContext.strokeText(name, width, height);
    }

    public void bondWith(Element bondElement, int bondNumber) throws IllegalStateException
    {
        if(bonds.size() > 4)
            throw new IllegalStateException(this.fullName + " already has 4 bonds");
        this.bonds.add(new bondInfo(bondNumber, bondElement));
    }
    public ArrayList<bondInfo> getBonds()
    {
        return this.bonds;
    }

    public boolean isElement(String name)
    {
        return this.name == nameList.seekByValue(name) || this.name == nameList.seekByKey(name);
    }
}
