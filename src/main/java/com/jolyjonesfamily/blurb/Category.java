package com.jolyjonesfamily.blurb;

import com.google.inject.Inject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.*;
import com.jolyjonesfamily.blurb.selector.*;

/**
 * Created by samjones on 4/5/14.
 */
public class Category {
    /**
     * Sum of weights of all entries in this category.
     */
    protected int maxWeightedValue;

    /**
     * List of all entries contained in this category.
     */
    protected List<Entry> entries;

    /**
     * Default parameters as specified in the XML.
     */
    private Map<String, String> defaultParams;

    /**
     * Parameters as passed from runner or from embed code.
     */
    private Map<String, String> params;

    /**
     * Map with namespaces as keys and category names as categories
     */
    protected static Map<String,Map<String, Category>> categories;

    /**
     * Element that contains the base list of categories.
     */
    private static Element baseElement = null;

    /**
     * Number picker, typically random.
     */
    private static Selector generator;

    private static final String CATEGORY_XPATH = "/blurb/namespace[@name='%s']/category[@name='%s']";

    static
    {
        categories = new HashMap<String, Map<String, Category>>();
    }

    /**
     * Constructor to get number of entries and pick weight.
     * @param numberGen to handle generating random numbers.
     * @param categoryNode
     */
    public Category(Node categoryNode, Selector numberGen)
    {
        this(numberGen);
        setCategoryNode(categoryNode);
    }

    /**
     * Thinner constructor for initial call
     *
     * @param numberGen to handle generating random numbers.
     */
    @Inject public Category(Selector numberGen)
    {
        generator = numberGen;
        maxWeightedValue = 0;
    }

    /**
     * Return output for this category.
     *
     * @return Output based on random data and the xml data.
     */
    public String getOutput()
    {
        return chooseEntry().getOutput();
    }

    /**
     * Set up the beginning category
     * @param categoryNode
     */
    public void setCategoryNode(Node categoryNode)
    {
        entries = getEntries(categoryNode);
        if (null == baseElement) {
            baseElement = categoryNode.getOwnerDocument().getDocumentElement();
        }
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getParam(String key)
    {
        if ((params != null) && (params.containsKey(key))) {
            return params.get(key);
        }
        if ((defaultParams != null) && (defaultParams.containsKey(key))) {
            return defaultParams.get(key);
        }
        return null;
    }


    /**
     * Pick an entry at random
     *
     * @return the element that we had to select.
     */
    public Entry chooseEntry()
    {
        int random = (int) generator.PickNumber(maxWeightedValue);
        for (Entry choice : entries) {
            random -= choice.weight;
            if (random < 0) return choice;
        }
        return null;
    }

    /**
     * Pull a list of entries for a given category node.
     * @param categoryNode
     * @return list of selections derived from XML
     */
    private final List<Entry> getEntries(Node categoryNode)
    {
        NodeList children = categoryNode.getChildNodes();
        List<Entry> results = new ArrayList<Entry>();
        for (int nodeNumber = 0; nodeNumber < children.getLength(); nodeNumber++)
        {
            if (!children.item(nodeNumber).getNodeName().equals("entry")) continue;
            Element entryElement = (Element)children.item(nodeNumber);
            int weight = entryElement.hasAttribute("weight") ? Integer.parseInt(entryElement.getAttribute("weight")) : 1;
            Entry entry = new Entry(this, entryElement, weight);
            results.add(entry);
            maxWeightedValue += entry.weight;
        }
        return results;
    }

    /**
     * Given a name and namespace, pull a category.
     *
     * @param categoryName
     * @param namespace
     * @return
     */
    public static Category getCategory(String categoryName, String namespace)
    {
        if (!categories.containsKey(namespace)) {
            categories.put(namespace, new HashMap<String, Category>());
        }
        if (!categories.get(namespace).containsKey(categoryName)) {
            try {
                categories.get(namespace).put(categoryName,
                        fetchCategory(categoryName, namespace));
            } catch (IllegalArgumentException iae) {
                iae.printStackTrace();
                System.exit(1);
            }
        }
        return categories.get(namespace).get(categoryName);
    }

    /**
     * Retrieve the category named from the DOM and keep it in the object.
     *
     * @param categoryName
     * @param namespace
     * @return Newly instantiated category object fetched from the DOM.
     * @throws XPathExpressionException
     */
    private static Category fetchCategory(String categoryName, String namespace) throws IllegalArgumentException {
        XPath xseek = XPathFactory.newInstance().newXPath();
        try {
            String myPath = String.format(CATEGORY_XPATH, namespace, categoryName);
            NodeList results = (NodeList) xseek.evaluate(myPath, baseElement, XPathConstants.NODESET);
            if (results.getLength() > 1) {
                throw new IllegalArgumentException(String.format("Too many results for namespace %s category %s",
                        namespace, categoryName));
            } else if (results.getLength() == 0) {
                throw new IllegalArgumentException(String.format("No match for namespace %s category %s",
                        namespace, categoryName));
            }
            return new Category(results.item(0), generator);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    /**
     * If no namespace is provided, assume namespace "main".
     * @param categoryName
     * @return Category with specified name.
     */
    public static Category getCategory(String categoryName)
    {
        return getCategory(categoryName, "main");
    }
}
