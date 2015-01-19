package com.jolyjonesfamily.blurb.selector;

import java.util.Stack;

/**
 * Created by samjones on 1/17/15.
 */
public class TestSelector implements Selector{
    protected Stack<Integer> numberSet;

    public TestSelector(Stack<Integer> preSet)
    {
        numberSet = preSet;
    }

    public int PickNumber(int limit)
    {
        return Math.min(numberSet.pop(), limit);
    }
}
