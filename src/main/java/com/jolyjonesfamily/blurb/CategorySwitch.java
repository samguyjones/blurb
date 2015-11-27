package com.jolyjonesfamily.blurb;

import com.jolyjonesfamily.blurb.models.*;

import java.util.*;

import com.jolyjonesfamily.blurb.selector.*;

/**
 * Created by samjones on 4/5/14.
 */
public class CategorySwitch {


    /**
     * Sum of weights of all entries in this category.
     */
    protected int maxWeightedValue = 0;

    /**
     * Parameters as passed from runner or from embed code.
     */
    private Map<String, String> params;

    /**
     * Number picker, typically random.
     */
    private static Selector generator;

    /**
     * Category to do picking
     */
    private Cat category;


    /**
     * Parent catalog for maintaining a list of categories.
     */
    private BlurbCatalog catalog;

    /**
     * All public constructors call back to this private method.  Set up with
     * the category and params, the two basic things the switch needs.
     *
     * @param catalog
     * @param myCategory
     * @param params
     */
    private void setup(BlurbCatalog catalog, Cat myCategory, Map<String, String> params) throws Exception {
        setCatalog(catalog);
        this.category = myCategory;
        assignWeightedValue();
        assignParams(params);
        if (getGenerator() == null) {
            setGenerator(RandomSelector.getInstance());
        }
    }

    /**
     * Constructor for choosing the root pattern.
     *
     */
    public CategorySwitch(BlurbCatalog catalog, Map<String, String> params)
        throws Exception {
        setup(catalog, catalog.getBlurb().getPattern(), params);
    }

    /**
     * Constructor for choosing named category from named namespace.
     *
     * @param catalog Blurb catalog containing this switch's category
     * @param namespaceName
     * @param categoryName
     */
    public CategorySwitch(BlurbCatalog catalog, String namespaceName, String categoryName,
      Map<String, String> params) throws Exception {
        Cat catModel = catalog.getBlurb()
                .getNamespace(namespaceName)
                .getCategory(categoryName);
        if (null == catModel) {
            throw new CategoryException("Category '" + categoryName + "' in namespace '"
                + namespaceName + "' not found.");
        }
        setup(catalog, catModel, params);
    }

    /**
     * Fetch the POJO that this switch acts on.
     *
     * @return POJO Object with a list of entries.
     */
    public BlurbCatalog getCatalog() {
        return catalog;
    }

    /**
     * Set the pojo that this switch acts on.
     *
     * @param catalog Mapped pojo of resuts.
     * @return Returns itself for fluent fun
     */
    public CategorySwitch setCatalog(BlurbCatalog catalog) {
        this.catalog = catalog;
        return this;
    }

    /**
     * Set the thing that handles picking for this instance.
     *
     * @return
     */
    public static Selector getGenerator() {
        return generator;
    }

    /**
     * Set the thing that handles picking for this instance.
     *
     * @param generator
     */
    public static void setGenerator(Selector generator) {
        CategorySwitch.generator = generator;
    }

    /**
     * Set up the params for the switch
     *
     * @param params key/value pairs passed across categories.
     */
    private void assignParams(Map<String, String> params) throws Exception {
        this.params = new HashMap<String, String>();
        for (Param myParam: category.getParam()) {
            this.params.put(myParam.getName(), paramValue(myParam));
        }
        this.params.putAll(params);
    }

    /**
     * Retrieve or derive the value of the current parameter.
     *
     * @param param
     * @return
     */
    private String paramValue(Param param) throws Exception {
        if (null != param.getDefault()) {
            return param.getDefault();
        }
        if (null != param.getDerive()) {
            Entry pseudoEntry = (Entry) new Entry().setContent(
                    param.getDerive().getContent()
            );
            return new EntryRender(pseudoEntry, this).getOutput();
        }
        return "";
    }

    /**
     * Figure out the total of the weighted values of all entries in the
     * switch.  A number from zero to this total will let the switch pick
     * what entry to randomly select.
     */
    protected void assignWeightedValue()
    {
        maxWeightedValue = 0;
        for(Entry myEntry : category.getEntry()) {
            maxWeightedValue += myEntry.getWeight();
        }
    }

    /**
     * Fetch key/value param pairs.
     *
     * @return
     */
    public Map<String, String> getParams() {
        return params;
    }

    /**
     * Fetch a specific parameter.
     * @param key
     * @return
     */
    public String getParam(String key) {
        return params.get(key);
    }

    /**
     * Take a map of key/value strings and replace the existing parameters.
     *
     * @param params
     */
    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    /**
     * Return the model for this switch.
     *
     * @return
     */
    public Cat getCategory() {
        return category;
    }

    /**
     * Return output for this category.
     *
     * @return Output based on random data and the xml data.
     */
    public String getOutput() throws Exception
    {
        return chooseEntry().getOutput();
    }

    /**
     * Pick an entry at random
     *
     * @return the element that we had to select.
     */
    public EntryRender chooseEntry()
    {
        int random = (int) generator.PickNumber(maxWeightedValue);
        for (Entry choice : category.getEntry()) {
            random -= choice.getWeight();
            // @todo Look into caching this.
            if (random < 0) return new EntryRender(choice, this);
        }
        throw new IndexOutOfBoundsException("Generated number extends " +
            String.valueOf(random) + " steps past possible entries");
    }
}
