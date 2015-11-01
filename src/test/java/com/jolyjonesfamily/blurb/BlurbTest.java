package com.jolyjonesfamily.blurb;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jolyjonesfamily.blurb.guice.TestBlurbModule;
import com.jolyjonesfamily.blurb.map.MapBlurb;
import com.jolyjonesfamily.blurb.map.MapBlurbXML;
import org.junit.Before;

import static org.junit.Assert.fail;

/**
 * Created by samjones on 11/1/15.
 */
public class BlurbTest {

    public MapBlurb getMap()
    {
        Injector injector = Guice.createInjector(new TestBlurbModule());
        return(injector.getInstance(MapBlurbXML.class));
    }
}
