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

    public void draw(GraphicsContext context)
    {

    }

    /**
     * Fills in the rest of the bonds with hydrogens
     */
    public void populateH()
    {
        for(Element ele : this.elements)
        {
            if(ele.isElement("O") || ele.isElement("O"))
            {
                while (ele.getBonds().size() < 4)
                {
                    ele.bondWith(new Element("H"), 1);
                }
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
                        if(name.charAt(start-1) == '-')
                            parsePrefix(name.lastIndexOf('-', start-2)-1);
                        else
                            parsePrefix(start);
                        fillMainC(); // also only fill in the carbon backbone once
                    }
                    found = true;
                    parseSpecificSuffix(i, j, start);
                }
            }
            if(found)
                return;
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
            lengthMain = SimulatorMain.prefixToNum(name.substring(end-3, end));
        } catch(InvalidParameterException damn)
        { // try again then with another length
            lengthMain = SimulatorMain.prefixToNum(name.substring(end-4, end));
            // I'm not catching because if both of these tries fail
            // something else probably did and I'm just gonna let the exception propagate
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
