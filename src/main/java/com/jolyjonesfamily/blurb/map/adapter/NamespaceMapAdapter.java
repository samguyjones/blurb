package com.jolyjonesfamily.blurb.map.adapter;

import com.jolyjonesfamily.blurb.models.Categories;
import com.jolyjonesfamily.blurb.models.Category;
import com.jolyjonesfamily.blurb.models.Namespace;
import com.jolyjonesfamily.blurb.models.Namespaces;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by samjones on 10/19/15.
 */
public class NamespaceMapAdapter extends XmlAdapter<Categories, Map<String, Category>> {

    @Override
    public Map<String, Category> unmarshal(Categories categoryList) throws Exception {
        Map<String, Category> categoryMap = new HashMap<String, Category>();
        for(Category myCategory : categoryList.getCategory()) {
            categoryMap.put(myCategory.getName(), myCategory);
        }
        return categoryMap;
    }

    @Override
    public Categories marshal(Map<String, Category> categoryMap) throws Exception {
        Categories result = new Categories();
        List<Category> categoryList = new ArrayList<Category>();
        categoryList.addAll(categoryMap.values());
        result.setCategory(categoryList);
        return result;
    }
}
