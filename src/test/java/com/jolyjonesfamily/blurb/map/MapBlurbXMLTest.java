package com.jolyjonesfamily.blurb.map;

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
    private static final String TEST_FILENAME = "pithy.xml";

    @Before
    public void setUp()
    {
        ClassLoader loader = getClass().getClassLoader();
        MapBlurbXML map = new MapBlurbXML(new File(loader.getResource(TEST_FILENAME).getFile()));
        try {
            blurb = map.getMappedBlurb();
        } catch (Exception e)
        {
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
}
