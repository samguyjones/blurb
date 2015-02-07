package com.jolyjonesfamily.blurb;

import com.jolyjonesfamily.blurb.filter.Filter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by samjones on 4/6/14.
 */
public class Entry {
    public int weight;
    public Element element;
    private static final String FILTER_NAMESPACE = "com.jolyjonesfamily.blurb.filter.";
    private static final String DEFAULT_ECHO = "UNDEFINED";
    private static final String PARAMETER_TYPE = "param";
    private static final String PARAM_KEY_FIELD = "name";
    private static final String PARAM_VALUE_FIELD = "value";
    private Category parentCategory;
    // public Entry entry;

    /**
     * Constructor with basic parameters:  DOM element and weight.
     *
     * @param element
     * @param weight
     */
    public Entry(Category category, Element element, int weight)
    {
        this.element = element;
        this.weight = weight;
        parentCategory = category;
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
                String embedOut = getPopulatedEmbed(embed).chooseEntry().getOutput();
                if (embed.hasAttribute("filter")) {
                    embedOut = filter(embedOut, embed.getAttribute("filter"));
                }
                output += embedOut;
            } else if (myNode.getNodeName().equals("echo")) {
                Element echo = (Element) myNode;
                String echoed = (getParam(echo.getAttribute("param")) == null) ?
                    DEFAULT_ECHO : getParam(echo.getAttribute("param"));
                output += echoed;
            }
        }
        return output;
    }

    private String getParam(String key)
    {
        return parentCategory.getParam(key);
    }

    /**
     * Return the category of specified element.
     *
     * @param embed
     * @return
     */
    protected  Category getCategory(Element embed)
    {
        if (embed.hasAttribute("namespace")) {
            return Category.getCategory(embed.getAttribute("namespace"),
                    embed.getAttribute("category"));
        } else {
            return Category.getCategory(embed.getAttribute("category"));
        }
    }

    /**
     * Return the category of specified element with added parameters.
     *
     * @param embed
     * @return
     */
    protected Category getPopulatedEmbed(Element embed)
    {
        Category embedCategory = getCategory(embed);
        NodeList paramList = embed.getElementsByTagName(PARAMETER_TYPE);
        if (paramList.getLength() == 0) {
            embedCategory.setParams(null);
            return embedCategory;
        }
        Map<String, String> params = new HashMap<String,String>();
        for (int count = 0; count < paramList.getLength(); count++){
            Element myParam = (Element) paramList.item(count);
            params.put(myParam.getAttribute(PARAM_KEY_FIELD),
                myParam.getAttribute(PARAM_VALUE_FIELD));
        }
        return embedCategory;
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
