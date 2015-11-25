package com.jolyjonesfamily.blurb;

import com.jolyjonesfamily.blurb.map.MapBlurb;
import com.jolyjonesfamily.blurb.models.Blurb;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by samjones on 10/27/15.
 */
public class BlurbCatalog {
    /**
     * Blurb for which this catalog records entries.
     */
    private Blurb blurb;

    /**
     * If no namespace is specified, we assume it's in one called "main".
     */
    public static final String DEFAULT_NAMESPACE = "main";

    /**
     * Double map keyed first by namespace name and then by category name.
     */
    private Map<String, Map<String,CategorySwitch>> categories;

    /**
     * The blurb has an initial category that kicks off the pattern check.
     */
    private CategorySwitch homeCategory;

    /**
     * Constructor sets the initial blurb.
     *
     * @param blurb
     */
    public BlurbCatalog(Blurb blurb) {

        this.blurb = blurb;
        categories = new HashMap<String, Map<String, CategorySwitch>>();
    }

    /**
     * Constructor sets initial blurb from a map.
     * @param map
     */
    public BlurbCatalog(MapBlurb map) throws Exception {
        this(map.getMappedBlurb());
    }

    /**
     * When called with only params, assume fetching home category.
     *
     * @param params Parameters for initial category
     * @return Home category
     */
    public CategorySwitch fetch(Map<String, String> params)
    {
        if (homeCategory == null) {
            homeCategory = new CategorySwitch(this, params);
        } else {
            homeCategory.setParams(params);
        }
        return homeCategory;
    }

    /**
     * Fetch a category given namespace, category and params
     * @param namespace Grouping of categories to prevent name conflicts
     * @param categoryName Readable name for this category
     * @param params Key value pairs showing arguments that can be displayed or acted
     *               on by category entries.
     * @return
     */
    public CategorySwitch fetch(String namespace, String categoryName,
                                Map<String, String> params) {
        if (!categories.containsValue(namespace)) {
            categories.put(namespace, new HashMap<String, CategorySwitch>());
        }
        if (!categories.get(namespace).containsValue(categoryName)) {
            CategorySwitch newSwitch = new CategorySwitch(this, namespace,
                categoryName, params);
            categories.get(namespace).put(categoryName, newSwitch);
            return newSwitch;
        }
        return categories.get(namespace).get(categoryName);
    }

    /**
     * As above, but assume DEFAULT_NAMESPACE in the absence of anything we can use.
     *
     * @param categoryName
     * @param params
     * @return
     */
    public CategorySwitch fetch(String categoryName, Map<String, String> params) {
        return fetch(DEFAULT_NAMESPACE, categoryName, params);
    }

    /**
     * Fetch the output from the topmost category (sometimes called the pattern).
     *
     * @param params key/value parameters that might influence the selection.
     * @return Random pattern selected by data value.
     */
    public String getOutput(Map<String, String> params)
    {
        return fetch(params).getOutput();
    }

    /**
     * Fetch the output from the topmost category with no provided pattern.
     *
     * @return Random pattern selected by data file.
     */
    public String getOutput()
    {
        return getOutput(new HashMap<String, String>());
    }

    /**
     * Fetch the model this class moves over.
     *
     * @return
     */
    public Blurb getBlurb() {
        return blurb;
    }
}
