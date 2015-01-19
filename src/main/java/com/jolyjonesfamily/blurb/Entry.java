package com.jolyjonesfamily.blurb;

import com.jolyjonesfamily.blurb.filter.Filter;
import com.sun.xml.internal.ws.util.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.Constructor;
import java.util.Map;

/**
 * Created by samjones on 4/6/14.
 */
public class Entry {
    public int weight;
    public Element element;
    private static final String FILTER_NAMESPACE = "com.jolyjonesfamily.blurb.filter.";
    // public Entry entry;

    /**
     * Constructor with basic parameters:  DOM element and weight.
     *
     * @param element
     * @param weight
     */
    public Entry(Element element, int weight)
    {
        this.element = element;
        this.weight = weight;
    }

    /**
     * Central method that figures out what the text value of the entry should be.
     * @return
     */
    public String getOutput()
    {
        NodeList children = element.getChildNodes();
        String output = "";
        for (int count=0;count < children.getLength();count++) {
            Node myNode = children.item(count);
            if (myNode.getNodeType() == Node.TEXT_NODE) {
                output += showText(myNode.getTextContent());
            } else if (myNode.getNodeName().equals("embed")) {
                Element embed = (Element) myNode;
                String embedOut = getEmbedCategory(embed).chooseEntry().getOutput();
                if (embed.hasAttribute("filter")) {
                    embedOut = filter(embedOut, embed.getAttribute("filter"));
                }
                output += embedOut;
            }
        }
        return output;
    }

    /**
     * Return the category of specified element.
     *
     * @param embed
     * @return
     */
    protected Category getEmbedCategory(Element embed)
    {
        if (embed.hasAttribute("namespace")) {
            return Category.getCategory(embed.getAttribute("namespace"),
                    embed.getAttribute("category"));
        } else {
            return Category.getCategory(embed.getAttribute("category"));
        }

    }

    /**
     * Pull out the filter from reflection and use it to filter the provided string.
     *
     * @param original
     * @param filterName
     * @return
     */
    protected String filter(String original, String filterName)
    {
        try {
            String inFilter = Character.toUpperCase(filterName.charAt(0)) + filterName.substring(1);
            Class filterClass = Class.forName(FILTER_NAMESPACE + inFilter);
            Constructor emptyConstructor = filterClass.getConstructors()[0];
            Filter filterType = (Filter) emptyConstructor.newInstance();
            return filterType.filter(original);
        } catch (Exception e) {
            System.err.println(e.getClass().toString() + ":" + e.getMessage());
            System.exit(1);
        }
        return null;
    }

    /**
     * Wrapper for postprocessing of the string.  Currently just empty string if string
     * is nothing but whitespace.
     *
     * @param plainText
     * @return
     */
    protected final String showText(String plainText)
    {
        if (plainText.trim().isEmpty()) {
            return "";
        }
        return plainText;
    }
}
