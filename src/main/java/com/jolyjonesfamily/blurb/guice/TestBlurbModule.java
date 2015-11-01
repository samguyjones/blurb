package com.jolyjonesfamily.blurb.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.jolyjonesfamily.blurb.map.MapBlurb;
import com.jolyjonesfamily.blurb.map.MapBlurbXML;
import com.jolyjonesfamily.blurb.selector.Selector;
import com.jolyjonesfamily.blurb.selector.TestSelector;

import java.io.File;
import java.util.Stack;

import static org.junit.Assert.fail;

/**
 * Created by samjones on 1/17/15.
 */
public class TestBlurbModule extends AbstractModule{
    private static final String TEST_FILENAME = "pithy.xml";

    @Override protected void configure()
    {
    }

    @Provides
    public File provideFile()
    {
        ClassLoader loader = getClass().getClassLoader();
        File xmlFile = new File(loader.getResource(TEST_FILENAME).getFile());
        return xmlFile;

    }

    @Provides
    public Selector provideSelector()
    {
        Stack<Integer> testStack = new Stack<Integer>() {{
            push(4);
            push(3);
            push(0);
        }};
        return new TestSelector(testStack);
    }
}
