package com.jolyjonesfamily.blurb;

import com.jolyjonesfamily.blurb.filter.Filter;
import com.jolyjonesfamily.blurb.models.*;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
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
     * Constructor with basic parameters:  DOM element and owning
     * category
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
    public String getOutput() throws Exception
    {
        return contentSetFetch(entry.getContent());
    }

    /**
     * Given a block of content, iterate through it and return every
     * individual piece of content.
     *
     * @param contentSet
     * @return
     */
    private String contentSetFetch(List<Object> contentSet)
        throws Exception {
        String output = "";
        for (Object myContent : contentSet) {
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
    public String contentFetch(Object content) throws Exception {
        if (content.getClass() == String.class) {
            return content.toString();
        } else if (content.getClass() == Echo.class) {
            Echo echo = (Echo) content;
            return (parentCategory.getParam(echo.getParam()) == null) ?
                    MISSING_PARAM_TEXT : showText(parentCategory.getParam(echo.getParam()));
        } else if (content.getClass() == If.class) {
            return getIfText((If) content);
        } else if (content.getClass() == Embed.class) {
            return getEmbedText((Embed) content);
        }
        // @todo Throw exception here
        return "";
    }

    /**
     * Process an If block in the model and return appropriate text referencing
     * parameters.
     *
     * @param content
     * @return
     */
    private String getIfText(If content) throws Exception {
        String key = content.getParam();
        for (Else alternate : content.getElse()) {
            if (getParam(key).equals(content.getMatch())) {
                return contentSetFetch(content.getThen().getContent());
            } else if (null != alternate) {
                key = alternate.getParam();
                // Else condition doesn't exist or is met, else runs
                if (key.equals(null) || getParam(key).equals(alternate.getMatch())) {
                    return contentSetFetch(alternate.getThen().getContent());
                }
            }
        }
        // If neither 'if' nor else is met, return empty.
        return "";
    }

    /**
     * Pull the embedded text for an embed tag and return it.
     *
     * @param content An Embed model object in the content
     * @return rendered form of content after fetch
     */
    private String getEmbedText(Embed content) throws Exception {
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

    /**
     * Retrieve params for an embed tag, which include all params
     * inherited from the parent category along with whatever params
     * are set inside the embed tag itself.
     *
     * @param embed An embed model object with possible params
     * @return A map of key-value pairs of the params
     */
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
