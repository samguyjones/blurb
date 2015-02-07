package com.jolyjonesfamily.blurb;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;

/**
 * Created by samjones on 1/12/15.
 */
public class EntryTest extends BlurbTest {

    @Test public void constructor()
    {
        NodeList topElements = baseNode.getChildNodes();
        Element secondElement = (Element) topElements.item(3);
        int weight = Integer.parseInt(secondElement.getAttribute("weight"));
        Entry testEntry = new Entry(setupRunner.getBaseCategory(),secondElement, weight);
        Assert.assertEquals(secondElement, testEntry.element);
        Assert.assertEquals(20, testEntry.weight);
    }

    @Test public void output()
    {
        NodeList topElements = baseNode.getChildNodes();
        Element secondElement = (Element) topElements.item(3);
        int weight = Integer.parseInt(secondElement.getAttribute("weight"));
        Entry testEntry = new Entry(setupRunner.getBaseCategory(),secondElement, weight);
        Assert.assertEquals("A funny word is \"Weaselnipples\".",testEntry.getOutput());
    }
}