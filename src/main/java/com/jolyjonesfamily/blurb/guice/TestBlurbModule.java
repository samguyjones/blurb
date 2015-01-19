package com.jolyjonesfamily.blurb.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.jolyjonesfamily.blurb.selector.Selector;
import com.jolyjonesfamily.blurb.selector.TestSelector;

import java.util.Stack;

/**
 * Created by samjones on 1/17/15.
 */
public class TestBlurbModule extends AbstractModule{
    protected static final String TEST_XML_FILENAME = "test_data.xml";

    @Override protected void configure()
    {
        bind(String.class)
            .annotatedWith(Names.named("Config"))
            .toInstance(TEST_XML_FILENAME);
    }

    @Provides public Selector provideSelector()
    {
        Stack<Integer> testStack = new Stack<Integer>();
        testStack.push(4);
        testStack.push(3);
        testStack.push(0);
        return new TestSelector(testStack);
    }
}
