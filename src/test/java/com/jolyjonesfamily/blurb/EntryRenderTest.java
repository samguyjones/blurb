package com.jolyjonesfamily.blurb;

import com.jolyjonesfamily.blurb.selector.TestSelector;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import java.util.Stack;

///**
// * Created by samjones on 1/12/15.
// */
public class EntryRenderTest extends BlurbTest {
    BlurbCatalog blurbCatalog;

    @Before
    public void setUp() throws Exception
    {
        blurbCatalog = new BlurbCatalog(getMap());
    }

    @Test
    public void simpleEntry() throws Exception
    {
        CategorySwitch.setGenerator(new TestSelector(new Integer[]{2}));
        EntryRender entry = blurbCatalog.fetch("main","abstraction",
                new HashMap<String, String>()).chooseEntry();
        assertEquals("happiness", entry.getOutput());
    }

    @Test
    public void embedEntry() throws Exception
    {
        CategorySwitch.setGenerator(new TestSelector(new Integer[] {1,3,0}));
        EntryRender entry = blurbCatalog.fetch(new HashMap<String, String>()).chooseEntry();
        assertEquals("Faith is the root of anger.", entry.getOutput());
    }

    @Test
    public void embedNamespaceEntry() throws Exception
    {
        CategorySwitch.setGenerator(new TestSelector(new Integer[] {1,0,22}));
        EntryRender entry = blurbCatalog.fetch(new HashMap<String, String>()).chooseEntry();
        assertEquals("Love defines hope.", entry.getOutput());
    }

    @Test
    public void echoEntry() throws Exception
    {
        CategorySwitch.setGenerator(new TestSelector(new Integer[]{1,3,2}));
        EntryRender entry = blurbCatalog.fetch(new HashMap<String, String>() {{
            put("subject", "Zane");
        }}).chooseEntry();
        assertEquals("There is no Zane, only faith.", entry.getOutput());
    }

    @Test
    public void missingParam() throws Exception
    {
        CategorySwitch.setGenerator(new TestSelector(new Integer[]{3,2}));
        EntryRender entry = blurbCatalog.fetch(new HashMap<String, String>()).chooseEntry();
        assertEquals("There is no undefined, only faith.", entry.getOutput());
    }

    @Test
    public void ifResult() throws Exception
    {
        CategorySwitch.setGenerator(new TestSelector(new Integer[]{3,21}));
        EntryRender entry = blurbCatalog.fetch(new HashMap<String, String>() {{
            put("subject", "puppy");
        }}).chooseEntry();
        assertEquals("Faith is a warm puppy.", entry.getOutput());
        CategorySwitch.setGenerator(new TestSelector(new Integer[]{1,21}));
        entry = blurbCatalog.fetch(new HashMap<String, String>() {{
            put("subject", "poodle");
        }}).chooseEntry();
        assertEquals("Anger is a vicious poodle.", entry.getOutput());
        CategorySwitch.setGenerator(new TestSelector(new Integer[]{4,21}));
        entry = blurbCatalog.fetch(new HashMap<String, String>() {{
            put("subject", "gecko");
        }}).chooseEntry();
        assertEquals("Despair is a bored gecko.", entry.getOutput());
        CategorySwitch.setGenerator(new TestSelector(new Integer[]{2,21}));
        entry = blurbCatalog.fetch(new HashMap<String, String>() {{
            put("subject", "kitty");
        }}).chooseEntry();
        assertEquals("Happiness is a kitty.", entry.getOutput());
    }

    @Test
    public void deriveParam() throws Exception
    {
        CategorySwitch.setGenerator(new TestSelector(new Integer[]{0,0,23}));
        EntryRender entry = blurbCatalog.fetch(new HashMap<String, String>()).chooseEntry();
        assertEquals("People value love for love's sake.", entry.getOutput());

    }
}