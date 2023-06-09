package com.example.hydrocarbonsimulator;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
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
        if(nameList.isEmpty())
        {
            // populate some bitches
            nameList.add("C", "Carbon");
            nameList.add("H", "Hydrogen");
            nameList.add("O", "Oxygen");
            nameList.add("F", "Fluorine");
            nameList.add("Cl", "Chlorine");
            nameList.add("Br", "Bromine");
            nameList.add("I", "Iodine");
            nameList.add("OH", "Hydroxyl");
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
     * Attempt to bond an element with another, only giving up if there is no space
     *
     * Note that this does NOT care about position: use bondWith() if you do
     * @param bondElement
     * @param bondNumber
     * @return If the bond has been successful
     */
    public boolean bondWithNOW(Element bondElement, int bondNumber)
    {
        // fill bonds if it's not already sized enough with garbage info
        while(this.bonds.size() < 4)
            this.bonds.add(new bondInfo(77943, new Element("O")));
        // DEBUG
        System.out.printf("ooo I wanna bond %s so bad ooo\n", bondElement.name);

        for(int i = 0; i < 4; ++i)
        {
            if(!this.bonded[i])
            {
                this.bonds.set(i, new bondInfo(bondNumber, bondElement));
                this.bonded[i] = true;
                this.bondCount += bondNumber;
                System.out.println("I bonded yippee");
                return true;
            }
        }
        return false;
    }

    public boolean updateBondNumber(int bondNumber, int position)
    {
        //DEBUG
        System.out.printf("Updating bind number of %s from %d to %d\n",
                this.name, this.getBonds().get(position).number(), bondNumber);
        if(this.bondCount + bondNumber - this.bonds.get(position).number() > 4)
            return false;
        this.bondCount -= this.bonds.get(position).number();
        this.bonds.set(position, new bondInfo(bondNumber, this.bonds.get(position).end()));
        this.bonded[position] = true;
        this.bondCount += bondNumber;
        return true;
    }

    /**
     * Attempt to bond an element with another, but gives up if the position is taken
     * @param bondElement
     * @param bondNumber
     * @param position 0 is right 1 is left 2 is up 3 is down
     * @throws IllegalStateException
     * @return If the bond has been successful
     */
    public boolean bondWith(Element bondElement, int bondNumber, int position) throws IllegalStateException
    {
        // fill bonds if it's not already sized enough with garbage info
        while(this.bonds.size() < 4)
            this.bonds.add(new bondInfo(77943, new Element("O")));
        if(this.bondCount >= 4)
            return false; // do nothing if bonds are full
        if(this.bonded[position] &&
                !this.bonds.get(position).end().isElement(bondElement.getName()))
            return false; // do nothing if it's already bonded and another attempt tries to bind differently
        else if(this.bonded[position] && this.bonds.get(position).end().isElement(bondElement.getName()))
        { // if the same bind is attempted again, update bind number when possible
            //DEBUG
            System.out.printf("Updating bind number of %s from %d to %d\n",
                    this.name, this.getBonds().get(position).number(), bondNumber);
            if(this.bondCount + bondNumber - this.bonds.get(position).number() > 4)
                return false;
            this.bondCount -= this.bonds.get(position).number();
            this.bonds.set(position, new bondInfo(bondNumber, bondElement));
            this.bonded[position] = true;
            this.bondCount += bondNumber;
            return true;
        }
        this.bonds.set(position, new bondInfo(bondNumber, bondElement));
        this.bonded[position] = true;
        this.bondCount += bondNumber;
        return true;
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
    public void setBondCount(int value)
    {
        this.bondCount = value;
    }
    public void addBondCount(int value)
    {
        this.bondCount += value;
    }

    public String getName()
    {
        return this.name;
    }
}