package com.example.hydrocarbonsimulator;

import java.util.ArrayList;

/**
 * A map-like class that stores pairs of keys and values.
 * Feature is that you can index keys by values as well, alongside values by keys
 * @param <Tkey> type of the key
 * @param <Tvalue>
 */
public class DualMap<Tkey, Tvalue>
{
    private ArrayList<Tvalue> values;
    private ArrayList<Tkey> keys;


}
