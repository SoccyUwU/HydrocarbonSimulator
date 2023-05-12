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

    /**
     * fetch a value by the key
     * @param key
     * @return the corresponding value
     */
    public Tvalue seekByKey(Tkey key)
    {
        int temp = keys.indexOf(key);
        return values.get(temp);
    }
    /**
     * fetch a key by the value
     * @param value
     * @return the corresponding key
     */
    public Tkey seekByValue(Tvalue value)
    {
        int temp = values.indexOf(value);
        return keys.get(temp);
    }

    /**
     * add a pair of values
     * @param key
     * @param value
     */
    public void add(Tkey key, Tvalue value)
    {
        values.add(value);
        keys.add(key);
    }
}
