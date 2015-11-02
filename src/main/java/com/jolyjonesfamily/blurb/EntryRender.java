package com.jolyjonesfamily.blurb;

import com.jolyjonesfamily.blurb.filter.Filter;
import com.jolyjonesfamily.blurb.models.Echo;
import com.jolyjonesfamily.blurb.models.Embed;
import com.jolyjonesfamily.blurb.models.Entry;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

///**
// * Created by samjones on 4/6/14.
// */
public class EntryRender {
    private static final String FILTER_NAMESPACE = "com.jolyjonesfamily.blurb.filter.";
    private static final String FILTER_INTERFACE = "com.jolyjonesfamily.blurb.filter.Filter";
    private static final String MISSING_PARAM_TEXT = "undefined";
    private CategorySwitch parentCategory;
    public Entry entry;

    /**
     * Constructor with basic parameters:  DOM element and weight.
     *
     * @param entry The POJO containing the render information
     * @param parentCategory The category that picked this entry and which has the
     *                       parameters.
     */
    public EntryRender(Entry entry, CategorySwitch parentCategory)
    {
        this.entry = entry;
        this.parentCategory = parentCategory;
    }

    /**
     * Pull text for this embed after figuring results
     *
     * @return Text representing this part of the embedding.
     */
    public String getOutput()
    {
        String output = "";
        for (Object myContent : entry.getContent()) {
            output += contentFetch(myContent);
        }
        return output;
    }

    /**
     * Main engine of entry rendering.  This goes through the content
     * elements and makes them ready to display as part of the output.
     *
     * @param content
     * @return
     */
    public String contentFetch(Object content) {
        if (content.getClass() == String.class) {
            return content.toString();
        } else if (content.getClass() == Echo.class) {
            Echo echo = (Echo) content;
            return (parentCategory.getParam(echo.getParam()) == null) ?
                MISSING_PARAM_TEXT : showText(parentCategory.getParam(echo.getParam()));
        } else if (content.getClass() == Embed.class) {
            return getEmbedText((Embed) content);
        }
        // @todo Throw exception here
        return "";
    }

    private String getEmbedText(Embed content) {
        Embed embed = (Embed) content;
        CategorySwitch subcat;
        if (embed.getNamespace() == null) {
            subcat = parentCategory.getCatalog().fetch(
                embed.getCategory(), getParamSet(embed));
        } else {
            subcat = parentCategory.getCatalog().fetch(
                embed.getNamespace(), embed.getCategory(),
                getParamSet(embed));
        }
        String response = subcat.getOutput();
        if (embed.getFilter() != null) {
            response = filter(response, embed.getFilter());
        }
        return showText(response);
    }

    private Map getParamSet(Embed embed) {
        Map paramSet;
        if (embed.getParam() == null) {
            paramSet = parentCategory.getParams();
        } else {
            paramSet = new HashMap<String, String>();
            paramSet.putAll(parentCategory.getParams());
            for (Embed.Param myParam : embed.getParam()) {
                paramSet.put(myParam.getName(), myParam.getValue());
            }
        }
        return paramSet;
    }

//    /**
//     * Central method that figures out what the text value of the entry should be.
//     * @return
//     */
//    public String getOutput()
//    {
//        NodeList children = element.getChildNodes();
//        String output = "";
//        for (int count=0;count < children.getLength();count++) {
//            Node myNode = children.item(count);
//            if (myNode.getNodeType() == Node.TEXT_NODE) {
//                output += showText(myNode.getTextContent());
//            } else if (myNode.getNodeName().equals("embed")) {
//                Element embed = (Element) myNode;
//                String embedOut = getPopulatedEmbed(embed).chooseEntry().getOutput();
//                if (embed.hasAttribute("filter")) {
//                    embedOut = filter(embedOut, embed.getAttribute("filter"));
//                }
//                output += embedOut;
//            } else if (myNode.getNodeName().equals("echo")) {
//                Element echo = (Element) myNode;
//                String echoed = (getParam(echo.getAttribute("param")) == null) ?
//                    DEFAULT_ECHO : getParam(echo.getAttribute("param"));
//                output += echoed;
//            }
//        }
//        return output;
//    }

    private String getParam(String key)
    {
        return parentCategory.getParam(key);
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
            Boolean valid = false;
            for (Class myInterface : filterClass.getInterfaces()) {
                if (myInterface.getName() == FILTER_INTERFACE) {
                    valid=true;
                }
            }
            if (!valid) {
                throw new Exception("Class " + filterClass.getName() + "does not extend "
                        + FILTER_INTERFACE);
            }
            Constructor emptyConstructor = filterClass.getConstructors()[0];
            Filter filterType = (Filter) emptyConstructor.newInstance();
            return filterType.filter(original);
        } catch (Exception e) {
            System.err.println(e.getClass().toString() + ":" + e.getMessage());
            System.exit(1);
        }
        return null;
    }
//    /**
//     * Return the category of specified element.
//     *
//     * @param embed
//     * @return
//     */
//    protected CategoryChooser getCategory(Element embed)
//    {
//        if (embed.hasAttribute("namespace")) {
//            return CategoryChooser.getCategory(embed.getAttribute("namespace"),
//                    embed.getAttribute("category"));
//        } else {
//            return CategoryChooser.getCategory(embed.getAttribute("category"));
//        }
//    }
//
//    /**
//     * Return the category of specified element with added parameters.
//     *
//     * @param embed
//     * @return
//     */
//    protected CategoryChooser getPopulatedEmbed(Element embed)
//    {
//        CategoryChooser embedCategory = getCategory(embed);
//        NodeList paramList = embed.getElementsByTagName(PARAMETER_TYPE);
//        if (paramList.getLength() == 0) {
//            embedCategory.setParams(null);
//            return embedCategory;
//        }
//        Map<String, String> params = new HashMap<String,String>();
//        for (int count = 0; count < paramList.getLength(); count++){
//            Element myParam = (Element) paramList.item(count);
//            params.put(myParam.getAttribute(PARAM_KEY_FIELD),
//                myParam.getAttribute(PARAM_VALUE_FIELD));
//        }
//        return embedCategory;
//    }
//


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
        return plainText.trim();
    }
}
