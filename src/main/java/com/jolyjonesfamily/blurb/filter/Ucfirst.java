package com.jolyjonesfamily.blurb.filter;

/**
 * Created by samjones on 7/21/14.
 */
public class Ucfirst implements Filter {

    public Ucfirst()
    {
    }

    public String filter(String input) {
        char[] stringArray = input.toCharArray();
        stringArray[0] = Character.toUpperCase(stringArray[0]);
        return new String(stringArray);
    }
}
