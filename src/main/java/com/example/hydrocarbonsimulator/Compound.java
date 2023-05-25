package com.example.hydrocarbonsimulator;

import java.util.ArrayList;

public class Compound
{
    // note that bond numbers are counting from 1~2 as first, and thus
    // size is one less than elements.size()
    ArrayList<Integer> bondNumbers = new ArrayList<Integer>();
    int lengthMain = -1;
    String name = "";

    ArrayList<Element> elements = new ArrayList<Element>();

    public void parseAlkene()
    {
        int end = name.lastIndexOf("-", name.indexOf("ene"));
        int start = name.lastIndexOf("-", end-1);

        this.lengthMain = SimulatorMain.prefixToNum(name.substring(0, start));

        ArrayList<Integer> parsedDoubleBondList = SimulatorMain.parseCommaNumList(name.substring(start+1, end));
        for(int i = 0; i < this.lengthMain-1; ++i)
        {
            if(parsedDoubleBondList.contains(i+1))
                this.bondNumbers.set(i, 2);
            else
                this.bondNumbers.set(i, 1);
        }
        System.out.println("Double bonds are: "+parsedDoubleBondList);
    }

    public void parseAlkyne(String name)
    {
        // TODO: 2023-05-24 finish parsing alkynes: literally just alkene but another name
    }
}
