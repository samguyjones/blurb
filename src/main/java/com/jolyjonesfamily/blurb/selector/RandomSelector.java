package com.jolyjonesfamily.blurb.selector;

/**
 * Created by samjones on 1/11/15.
 */
public class RandomSelector implements Selector {
    public int PickNumber(int limit) {
        return (int) (Math.random() * (limit));
    }
}
