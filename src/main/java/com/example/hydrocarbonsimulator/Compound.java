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
    ArrayList<Integer> bondNumbers = new ArrayList<Integer>();
    int lengthMain = -1;
    String name = "";
    String[][] suffixList = {{"ane"}, {"ene", "yne"}};

    ArrayList<Element> elements = new ArrayList<Element>();

    // bunch of drawing related stuff
    final static int gap = 68; // simon told me to use this number
    final static int letterSize = 15; // looks nice
    final static int startSize = 100; // looks nice


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
            for(int i = 0; i < 4; ++i)
            {
                ele.bondWith(new Element("H"), 1, i);
            }
        }
    }

    public void parseMainPath()
    {
        for(int i = 0; i < suffixList.length; ++i)
        {
            boolean found = false;
            for(int j = 0; j < suffixList[i].length; ++j)
            {
                int start = name.indexOf(suffixList[i][j]);
                if(start != -1) // if any found
                {
                    if(!found) // only parse prefixes once
                    {
                        // if the suffix has dashes, skip them
                        if (name.charAt(start - 1) == '-')
                            parsePrefix(name.lastIndexOf('-', start - 2));
                        else
                            parsePrefix(start);
                        fillMainC(); // also only fill in the carbon backbone once
                    }
                    found = true;
                    parseSpecificSuffix(i, j, start);
                }
            }
            if(found)
                break;
        }


        if(this.elements.size() > 1)
        {
            this.elements.get(0).bondWith(this.elements.get(1), this.bondNumbers.get(0), 0);
            for (int i = 1; i < this.elements.size() - 1; ++i)
            {
                this.elements.get(i).bondWith(this.elements.get(i + 1), this.bondNumbers.get(i), 0);
                this.elements.get(i).bondWith(this.elements.get(i - 1), this.bondNumbers.get(i - 1), 1);
            }
            this.elements.get(this.elements.size() - 1).bondWith(this.elements.get(this.elements.size() - 2),
                    this.bondNumbers.get(this.elements.size() - 2), 1);
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
        // populate with default values first
        for(int i = 0; i < lengthMain; ++i)
            bondNumbers.add(1);
        switch(level)
        {
            case 0: // alkane
                // intentionally blank: no change needed
                break;
            case 1: // alkene/alkyne
                // if there's no number assume 1
                if(name.charAt(index-1) != '-')
                {
                    bondNumbers.set(0, version+2);
                    break;
                }
                else // otherwise parse numbers
                {
                    int start = name.lastIndexOf("-", index-2);
                    ArrayList<Integer> indexList = SimulatorMain.parseCommaNumList(name.substring(start+1, index-1));
                    for(int i : indexList)
                    {
                        bondNumbers.set(i-1, version+2);
                    }
                } break;
            default: // PANIC
                throw new IllegalArgumentException ("Specific suffix was not parsed");
        }
    }
}
