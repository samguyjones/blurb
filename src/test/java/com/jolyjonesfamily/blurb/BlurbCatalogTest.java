package com.jolyjonesfamily.blurb;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
* Created by samjones on 1/17/15.
*/
public class BlurbCatalogTest extends BlurbTest {
    protected BlurbCatalog catalog;

    @Before
    public void setUp() throws  Exception {
        catalog = new BlurbCatalog(getMap());
    }

    @Test
    public void rootCategory() throws Exception {
        CategorySwitch pattern = catalog.fetch(new HashMap<String, String>() {{
            put("foo", "bar");
        }});
        assertEquals((Integer) 20, pattern.getCategory().getEntry(1).getWeight());
        assertEquals("bar", pattern.getParam("foo"));
    }

    @Test
    public void mainCategory() throws Exception {
        CategorySwitch abstraction = catalog.fetch("abstraction",
                new HashMap<String, String>() {{
            put("also", "frogs");
        }});
        assertEquals("anger", abstraction.getCategory().getEntry(1).getContent(0));
        assertEquals("frogs", abstraction.getParam("also"));
        assertEquals((Integer) 5, (Integer) abstraction.getCategory().getEntry().size());
    }

    @Test
    public void secondNamespace() throws Exception {
        CategorySwitch otherAbstraction = catalog.fetch("second", "abstraction",
            new HashMap<String, String>());
        assertEquals("religion", otherAbstraction.getCategory().getEntry(0).getContent(0));
        assertEquals("friendship", otherAbstraction.getCategory().getEntry(2).getContent(0));
        assertEquals((Integer) 3, (Integer) otherAbstraction.getCategory().getEntry().size());
    }
}