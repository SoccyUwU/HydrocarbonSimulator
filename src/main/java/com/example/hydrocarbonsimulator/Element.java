package com.example.hydrocarbonsimulator;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Element
{

    // in hydrocarbons we've learned, an element only connects to at most four other
    private ArrayList<bondInfo> bonds = new ArrayList<>(4);
    private boolean[] bonded = new boolean[]{false, false, false, false};
    private int bondCount = 0; // max is 4
    // note that keys are shorter (C, Ca, H) while values are full (Carbon, Calcium)
    private static DualMap<String, String> nameList = new DualMap<String, String>();
    // full name is always fully lower case
    private String fullName = "morbium";
    // abbreviation name is according to the laws
    private String name = "M";

    /**
     * Special comp function to prioritize elements as described in sortBonds()
     * @param a
     * @param b
     * @return
     */
    private boolean compElement(bondInfo a, bondInfo b)
    {
        int aNum, bNum;
        if(a.end().name == "C")
            aNum = 0;
        else if(a.end().name == "H")
            aNum = 2;
        else
            aNum = 1;
        if(b.end().name == "C")
            bNum = 0;
        else if(b.end().name == "H")
            bNum = 2;
        else
            bNum = 1;
        return aNum < bNum;
    }

    /**
     * This sorts bonds in order from C to functional group to H
     */
    public void sortBonds()
    {
        for(int j = bonds.size(); j > 0; --j)
        {
            for (int i = 0; i < j-1; ++i)
            {
                if(!compElement(bonds.get(i), bonds.get(i+1)))
                {
                    Collections.swap(bonds, i, i+1);
                }
            }
        }
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

    public void draw(GraphicsContext context, int width, int height)
    {
        context.strokeText(name, width, height);
    }

    /**
     *
     * @param bondElement
     * @param bondNumber
     * @param position 0 is right 1 is left 2 is up 3 is down
     * @throws IllegalStateException
     */
    public void bondWith(Element bondElement, int bondNumber, int position) throws IllegalStateException
    {
        // fill bonds if it's not already sized enough with garbage info
        while(this.bonds.size() < 4)
            this.bonds.add(new bondInfo(77943, new Element("O")));
        if(this.bonded[position])
            return; // do nothing if it's already bonded
        if(this.bondCount >= 4)
            return; // do nothing if bonds are full
        this.bonds.set(position, new bondInfo(bondNumber, bondElement));
        this.bonded[position] = true;
        this.bondCount += bondNumber;
    }
    public ArrayList<bondInfo> getBonds()
    {
        return this.bonds;
    }

    public boolean isElement(String name)
    {
        return this.name == nameList.seekByValue(name) || this.name == nameList.seekByKey(name);
    }

    public int getBondCount()
    {
        return this.bondCount;
    }
}