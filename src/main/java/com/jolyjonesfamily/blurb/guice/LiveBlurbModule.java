package com.jolyjonesfamily.blurb.guice;

import com.google.inject.AbstractModule;
import com.jolyjonesfamily.blurb.selector.RandomSelector;
import com.jolyjonesfamily.blurb.selector.Selector;

/**
 * Created by samjones on 1/11/15.
 */
public class LiveBlurbModule extends AbstractModule {
    @Override protected void configure()
    {
        bind(Selector.class).to(RandomSelector.class);
    }
}
