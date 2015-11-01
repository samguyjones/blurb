package com.jolyjonesfamily.blurb;

import com.jolyjonesfamily.blurb.models.Blurb;
import com.jolyjonesfamily.blurb.selector.TestSelector;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


import java.util.HashMap;
import java.util.Stack;

/**
* Created by samjones on 1/17/15.
*/
public class CategorySwitchTest extends BlurbTest {
    Blurb blurb;

    @Before
    public void setUp() throws Exception{
        blurb = getMap().getMappedBlurb();
    }

    @Test
    public void getPattern() {
        CategorySwitch pattern = new CategorySwitch(blurb, new HashMap() {{
            put("foo", "bar");
        }});
        CategorySwitch.setGenerator(new TestSelector(new Stack<Integer>() {{
            push(1);
            push(0);
        }}));
        assertEquals((Integer) 20, pattern.getCategory().getEntry(1).getWeight());
        assertEquals((Integer) 1, pattern.chooseEntry().entry.getWeight());
        assertEquals((Integer) 20, pattern.chooseEntry().entry.getWeight());
        assertEquals("bar", pattern.getParam("foo"));
        assertEquals("pig", pattern.getParam("unused"));
    }

    @Test
    public void getMainCategory() {
        CategorySwitch abstraction = new CategorySwitch(blurb, "main", "abstraction",
            new HashMap() {{
            put("pig", "foot");
        }});
        CategorySwitch.setGenerator(new TestSelector(new Stack<Integer>() {{
            push(1);
            push(0);
        }}));
        assertEquals("love", abstraction.chooseEntry().entry.getContent(0));
        assertEquals("anger", abstraction.chooseEntry().entry.getContent(0));
    }

}
