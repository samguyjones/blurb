package com.jolyjonesfamily.blurb.map.adapter;

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
public class BlurbMapAdapter extends XmlAdapter<Namespaces, Map<String, Namespace>> {

    @Override
    public Map<String, Namespace> unmarshal(Namespaces namespaceList) throws Exception {
        Map<String, Namespace> namespaceMap = new HashMap<String, Namespace>();
        for(Namespace myNamespace : namespaceList.getNamespace()) {
            namespaceMap.put(myNamespace.getName(), myNamespace);
        }
        return namespaceMap;
    }

    @Override
    public Namespaces marshal(Map<String, Namespace> namespaceMap) throws Exception {
        Namespaces result = new Namespaces();
        List<Namespace> namespacesList = new ArrayList<Namespace>();
        namespacesList.addAll(namespaceMap.values());
        result.setNamespace(namespacesList);
        return result;
    }
}
