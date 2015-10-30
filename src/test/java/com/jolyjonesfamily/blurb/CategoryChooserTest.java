//package com.jolyjonesfamily.blurb;
//
//import com.google.inject.Guice;
//import com.google.inject.Injector;
//import com.jolyjonesfamily.blurb.guice.TestBlurbModule;
//import org.junit.Assert;
//import org.junit.Test;
//import org.w3c.dom.Node;
//
///**
// * Created by samjones on 1/17/15.
// */
//public class CategoryChooserTest extends BlurbTest {
//    @Test public void constructor()
//    {
//        Injector injector = Guice.createInjector(new TestBlurbModule());
//        CategoryChooser testCategory = injector.getInstance(CategoryChooser.class);
//        testCategory.setCategoryNode(baseNode);
//        Entry testEntry = testCategory.chooseEntry();
//        Node testNode = baseNode.getChildNodes().item(1);
//        Assert.assertEquals(1, testEntry.weight);
//        Assert.assertEquals(testNode, testEntry.element);
//    }
//
//    @Test public void output()
//    {
//        Injector injector = Guice.createInjector(new TestBlurbModule());
//        CategoryChooser testCategory = injector.getInstance(CategoryChooser.class);
//        testCategory.setCategoryNode(baseNode);
//        Assert.assertEquals("Faith is the root of despair.", testCategory.getOutput());
//    }
//}
