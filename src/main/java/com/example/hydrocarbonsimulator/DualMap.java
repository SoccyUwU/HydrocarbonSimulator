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
    private ArrayList<Tvalue> values = new ArrayList<>();
    private ArrayList<Tkey> keys = new ArrayList<>();

    /**
     * called whenever it is detected that there aren't the same number of values and keys<p></p></p>
     * in other words, when the keys and values mismatch somehow
     * @throws IllegalStateException whenever it's called
     */
    private void panic() throws IllegalStateException
    {
        throw new IllegalStateException("Number of values and keys mismatch\nSeriously how did you get here");
    }

    /**
     * fetch a value by the key
     * @param key
     * @return the corresponding value, or null if none found
     */
    public Tvalue seekByKey(Tkey key)
    {
        int temp = keys.indexOf(key);
        if(temp == -1) // if none found
            return null;
        return values.get(temp);
    }
    /**
     * fetch a key by the value
     * @param value
     * @return the corresponding key, or null if none found
     */
    public Tkey seekByValue(Tvalue value)
    {
        int temp = values.indexOf(value);
        if(temp == -1) // if none found
            return null;
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
    public int size()
    {
        return keys.size();
    }
    public boolean empty()
    {
        return keys == null || this.size() == 0;
    }
}
