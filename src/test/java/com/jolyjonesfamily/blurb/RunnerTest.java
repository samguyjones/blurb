package com.jolyjonesfamily.blurb;

import com.jolyjonesfamily.blurb.selector.TestSelector;
import com.jolyjonesfamily.blurb.selector.Selector;
import org.junit.Assert;
import org.junit.Test;

import java.util.Stack;

/**
* Created by samjones on 1/18/15.
*/
public class RunnerTest extends BlurbTest {
    @Test public void output()
    {
        Runner runner;
        try {
            runner = Runner.getInstance(getMap());
        } catch (Exception e) {
            Assert.fail();
            return;
        }
        Selector testSelector = new TestSelector(new Stack<Integer>(){{
            push(4);
            push(3);
            push(0);
        }});
        CategorySwitch.setGenerator(testSelector);
        Assert.assertEquals("Faith is the root of despair.", runner.getOutput());
    }
}
