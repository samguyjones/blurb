package com.jolyjonesfamily.blurb;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jolyjonesfamily.blurb.guice.TestBlurbModule;
import org.w3c.dom.Node;

/**
 * Created by samjones on 1/17/15.
 */
public abstract class BlurbTest {
    protected Node baseNode;
    protected Runner setupRunner;

    public BlurbTest()
    {
        Injector injector = Guice.createInjector(new TestBlurbModule());
        setupRunner = injector.getInstance(Runner.class);
        baseNode = setupRunner.getPattern();
    }
}
