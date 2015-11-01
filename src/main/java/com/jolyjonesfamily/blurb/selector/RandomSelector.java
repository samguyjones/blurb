package com.jolyjonesfamily.blurb.selector;

/**
 * Created by samjones on 1/11/15.
 */
public class RandomSelector implements Selector {
    private static RandomSelector instance;

    public static RandomSelector getInstance()
    {
        if (instance == null) {
            instance = new RandomSelector();
        }
        return instance;
    }

    public int PickNumber(int limit) {
        return (int) (Math.random() * (limit));
    }
}
