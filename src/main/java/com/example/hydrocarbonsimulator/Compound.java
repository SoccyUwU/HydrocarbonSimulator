package com.example.hydrocarbonsimulator;

import java.util.ArrayList;

public class Compound
{
    // note that bond numbers are counting from 1~2 as first, and thus
    // size is one less than elements.size()
    ArrayList<Integer> bondNumbers = new ArrayList<Integer>();
    int lengthMain = -1;

    ArrayList<Element> elements = new ArrayList<Element>();
}
