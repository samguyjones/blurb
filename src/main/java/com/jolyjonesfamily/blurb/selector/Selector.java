package com.jolyjonesfamily.blurb.selector;

/**
 * Created by samjones on 1/11/15.
 */
public interface Selector {
    /**
     * Returns a number up to -- but not including -- the given limit.
     * @param limit
     * @return
     */
    public int PickNumber(int limit);
}
