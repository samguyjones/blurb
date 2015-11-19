package com.jolyjonesfamily.blurb.map;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jolyjonesfamily.blurb.guice.TestBlurbModule;
import com.jolyjonesfamily.blurb.models.Blurb;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test that our XML Mapper works.
 *
 * Created by samjones on 10/18/15.
 */
public class MapBlurbXMLTest {
    private Blurb blurb;


    @Before
    public void setUp()
    {
        Injector injector = Guice.createInjector( new TestBlurbModule() );
        MapBlurb map = injector.getInstance(MapBlurbXML.class);
        try {
            blurb = map.getMappedBlurb();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPattern()
    {
        assertEquals((Integer) 20, blurb.getPattern().getEntry(1).getWeight());
        assertEquals("There is no ", blurb.getPattern().getEntry(1)
            .getContent(0));
    }

    @Test
    public void testNamespace()
    {
        assertEquals("main", blurb.getNamespace("main").getName());
    }

    @Test
    public void testCategory()
    {
        assertEquals("love", (String) blurb.getNamespace("main").getCategory("abstraction")
                .getEntry(0).getContent(0));
    }

    @Test
    public void testNamespaces()
    {
        assertEquals("friendship", (String) blurb.getNamespace("second").getCategory("abstraction")
                .getEntry(2).getContent(0));
    }
}
