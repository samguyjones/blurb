package com.jolyjonesfamily.blurb;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.jolyjonesfamily.blurb.guice.LiveBlurbModule;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Created by samjones on 4/2/14.
 */
public class Runner {
    private Category baseCategory;


    private Node pattern;

    public static void main (String argv[]) {
        Runner myRunner = new Runner(argv[0]);
        System.out.println(myRunner.getOutput());
    }

    public Runner(String filename)
    {
        Document xmlDoc = getDocument(filename);
        pattern = getPattern(xmlDoc);
        Injector injector = Guice.createInjector(new LiveBlurbModule());
        baseCategory = injector.getInstance(Category.class);
        baseCategory.setCategoryNode(pattern);
    }

    public Node getPattern() {
        return pattern;
    }

    public String getOutput() {
        return baseCategory.chooseEntry().getOutput();
    }

    /**
     * Given a filename, return DOM Document object
     *
     * @param filename XML file
     * @return DOM document
     */
    private Document getDocument(String filename)
    {
        File blurbXml = new File(filename);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            System.err.println("Parser configuration:  " + pce.getMessage());
            System.exit(1);
        }
        try {
            return dBuilder.parse(blurbXml);
        } catch (Exception e) {
            System.err.println("Parse Exception:  " + e.getMessage());
            System.exit(1);
        }
        return null;
    }

    private final Node getPattern(Document xmlFile)
    {
        return xmlFile.getDocumentElement().getElementsByTagName("pattern").item(0);
    }
}
