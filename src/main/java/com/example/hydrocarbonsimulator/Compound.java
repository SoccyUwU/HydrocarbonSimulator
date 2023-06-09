package com.example.hydrocarbonsimulator;

import javafx.scene.canvas.GraphicsContext;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Compound
{
    // note that bond numbers are counting from 1~2 as first, and thus
    // size is one less than elements.size()
    protected ArrayList<Integer> bondNumbers = new ArrayList<Integer>();
    protected int lengthMain = -1;
    protected String name = "";
    protected String[][] suffixList = {{"ane"}, {"ene", "yne", "en", "yn"}, {"anol"},
            {"enol", "ynol"}, {"ol"}, {"one"}, {"al"}};

    protected ArrayList<Element> elements = new ArrayList<Element>();

    // bunch of drawing related stuff
    protected final static int gap = 100 - 38; // simon told me to use this number
    protected final static int letterSize = 15; // looks nice
    protected final static int startSize = 100; // looks nice


    public void draw(GraphicsContext context)
    {
        context.fillRect(0, 0, context.getCanvas().getWidth(), context.getCanvas().getHeight());
        for(Element ele : elements)
        {
            if(ele.getBondCount() > 4)
            {
                context.strokeText("Invalid name: too many bonds",
                        context.getCanvas().getWidth()/2, context.getCanvas().getHeight()/2);
                return;
            }
        }
        for(int i = 0; i < elements.size(); ++i)
        {
            elements.get(i).draw(context, 100 + gap * i, 100);
            ArrayList<bondInfo> tempBonds = elements.get(i).getBonds();

            // this goes RIGHT
            if(tempBonds.get(0).end().isElement("C")
                    || tempBonds.get(0).number() == 77943)
                continue;
            else
            {
                int bondNumberHere = elements.get(i).getBonds().get(0).number();
                for(int j = 0; j < bondNumberHere; ++j)
                {
                    double adaptiveSizing = - 10 - letterSize / 2 + startSize
                            + 20.0/(bondNumberHere + 1) * (j + 1);
                    context.strokeLine(100 + gap * i + letterSize, adaptiveSizing,
                            startSize + gap * i + gap, adaptiveSizing);
                }
                tempBonds.get(0).end().draw(context, 100 + gap * (i+1), 100);
            }
            // this goes LEFT
            if(tempBonds.get(1).end().isElement("C")
                    || tempBonds.get(1).number() == 77943)
                continue;
            else
            {
                int bondNumberHere = elements.get(i).getBonds().get(1).number();
                for(int j = 0; j < bondNumberHere; ++j)
                {
                    double adaptiveSizing = - 10 - letterSize / 2 + startSize
                            + 20.0/(bondNumberHere + 1) * (j + 1);
                    context.strokeLine(startSize + gap * i, adaptiveSizing,
                            startSize + gap * (i-1) + letterSize, adaptiveSizing);
                }
                tempBonds.get(1).end().draw(context, 100 + gap * (i-1), 100);
            }
            // this goes UP
            if(tempBonds.get(2).end().isElement("C")
                    || tempBonds.get(2).number() == 77943)
                continue;
            else
            {
                int bondNumberHere = elements.get(i).getBonds().get(2).number();
                for(int j = 0; j < bondNumberHere; ++j)
                {
                    double adaptiveSizing = - 10 + letterSize/2 + 20.0/(bondNumberHere + 1) * (j + 1);
                    context.strokeLine(startSize + gap * i + adaptiveSizing, startSize-letterSize,
                            startSize + gap * i + adaptiveSizing, startSize-gap);
                }
                tempBonds.get(2).end().draw(context, 100 + gap * i, 100 - gap);
            }
            // this goes DOWN
            if(tempBonds.get(3).end().isElement("C")
                    || tempBonds.get(3).number() == 77943)
                continue;
            else
            {
                int bondNumberHere = elements.get(i).getBonds().get(3).number();
                for(int j = 0; j < bondNumberHere; ++j)
                {
                    double adaptiveSizing = - 10 + letterSize/2 + 20.0/(bondNumberHere + 1) * (j + 1);
                    context.strokeLine(startSize + gap * i + adaptiveSizing, startSize,
                            startSize + gap * i + adaptiveSizing, startSize+gap-letterSize);
                }
                tempBonds.get(3).end().draw(context, 100 + gap * i, 100 + gap);
            }
        }
    }

    /**
     * Fills in the rest of the bonds with hydrogens
     */
    public void populateH()
    {
        for(Element ele : this.elements)
        {
            System.out.printf("C element has %d bonds before H\n", ele.getBondCount());
            while(ele.getBondCount() < 4)
            {
                ele.bondWithNOW(new Element("H"), 1);
            }
        }
    }

    public void parseMainPath()
    {
        boolean prep = false;
        for(int i = 0; i < suffixList.length; ++i)
        {
            for(int j = 0; j < suffixList[i].length; ++j)
            {
                int start = name.lastIndexOf(suffixList[i][j]);
                //DEBUG
                System.out.println("start is " + start);
                if(start != -1) // if any found
                {
                    if(!prep) // only parse prefixes once
                    {
                        // if the suffix has dashes, skip them
                        if (name.charAt(start - 1) == '-')
                            parsePrefix(name.lastIndexOf('-', start - 2));
                        else
                            parsePrefix(start);
                        fillMainC(); // also only fill in the carbon backbone once
                        // populate with default values first
                        for(int _k = 0; _k < lengthMain-1; ++_k)
                            bondNumbers.add(1);
                        if(this.elements.size() > 1)
                        {
                            this.elements.get(0).bondWith(this.elements.get(1), this.bondNumbers.get(0), 0);
                            for (int k = 1; k < this.elements.size() - 1; ++k)
                            {
                                this.elements.get(k).bondWith(this.elements.get(k + 1),
                                        this.bondNumbers.get(k), 0);
                                this.elements.get(k).bondWith(this.elements.get(k - 1),
                                        this.bondNumbers.get(k - 1), 1);
                            }
                            this.elements.get(this.elements.size() - 1).bondWith(this.elements.get(this.elements.size() - 2),
                                    this.bondNumbers.get(this.elements.size() - 2), 1);
                        }
                        // DEBUG
                        System.out.println("prep is done!");
                        prep = true;
                    }
                    parseSpecificSuffix(i, j, start);
                }
            }
        }
    }

    /**
     * This may not be elegant but it sure works
     *
     * Since prefixes are only 3 to 4 letters long, we can just try both,
     * if prefixToSum doesn't throw an exception we know we found it
     * @param end
     */
    private void parsePrefix(int end) throws InvalidParameterException
    {
        try
        {
            lengthMain = SimulatorMain.prefixToNum(name.substring(end-4, end));
        } catch(InvalidParameterException damn)
        { // try again then with another length
            lengthMain = SimulatorMain.prefixToNum(name.substring(end-3, end));
            // I'm not catching because if both of these tries fail
            // something else probably did and I'm just gonna let the exception propagate
        } catch(StringIndexOutOfBoundsException damn)
        {
            lengthMain = SimulatorMain.prefixToNum(name.substring(end-3, end));
        }
        // DEBUG
        System.out.printf("parsed length: %d\n", lengthMain);
    }

    private void fillMainC()
    {
        for(int i = 0; i < lengthMain; ++i)
        {
            elements.add(new Element("C"));
        }
    }

    private void parseSpecificSuffix(int level, int version, int index) throws IllegalArgumentException
    {

        switch(level)
        {
            case 0: // alkane
                // intentionally blank: no change needed
                break;
            case 1: // alkene/alkyne
                addDTbond(version%2, index); break;
            case 2, 4: // alcohol
                parseHydroxylBond(version, index); break;
            case 3: // alcohol with alkene/alkyne
                addDTbond(version, index);
                parseHydroxylBond(version, index); break;
            default: // PANIC
                throw new IllegalArgumentException ("Specific suffix was not parsed");
        }
    }

    private void addDTbond(int version, int index)
    {
        // if there's no number assume 1
        if(name.charAt(index-1) != '-')
        {
            this.elements.get(0).updateBondNumber(version+2, 0);
            this.elements.get(1).updateBondNumber(version+2, 1);
            return;
        }
        else // otherwise parse numbers
        {
            int start = name.lastIndexOf("-", index-2);
            ArrayList<Integer> indexList = SimulatorMain.parseCommaNumList(name.substring(start+1, index-1));
            for(int i : indexList)
            {
                bondNumbers.set(i-1, version+2);
                this.elements.get(i-1).updateBondNumber(version+2, 0);
                this.elements.get(i).updateBondNumber(version+2, 1);
            }
        }
    }

    private void parseHydroxylBond(int version, int index)
    {
        // if there's no numbering assume 1
        if(name.charAt(index-1) != '-')
        {
            elements.get(0).bondWithNOW(new Element("OH"), 1);
        }
        else
        {
            int start = name.lastIndexOf("-", index-2);
            ArrayList<Integer> indexList = SimulatorMain.parseCommaNumList(name.substring(start+1, index-1));
            for(int i : indexList)
            {
                elements.get(i-1).bondWithNOW(new Element("OH"), 1);
            }
        }
    }
}
