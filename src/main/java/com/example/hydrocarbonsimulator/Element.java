package com.example.hydrocarbonsimulator;

import java.util.ArrayList;

public class Element
{
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

    public Element(String name)
    {
        // if the dictionary isn't initialized: populate it
        if(nameList.empty())
        {
            // populate some bitches

        }
        // if the name is short: has to be abbreviation
        if(name.length() <= 2)
        {

        }
    }
}
