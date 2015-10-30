//package com.jolyjonesfamily.blurb;
//
//import com.google.inject.Inject;
//import com.google.inject.name.Named;
//import com.jolyjonesfamily.blurb.selector.RandomSelector;
//import org.apache.commons.cli.*;
//import org.w3c.dom.Document;
//import org.w3c.dom.Node;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import java.io.File;
//import java.util.HashMap;
//import java.util.Map;
//
//import com.jolyjonesfamily.blurb.selector.Selector;
//
///**
// * Created by samjones on 4/2/14.
// */
//public class Runner {
//    private CategoryChooser baseCategory;
//    private Node pattern;
//    private static final char PARAM = 'p';
//    private static final String PARAM_NAME = "parameters";
//
//    public static void main (String argv[]) {
//        Runner myRunner = parseOptions(argv);
//        if (myRunner != null) {
//            System.out.println(myRunner.getOutput());
//        }
//    }
//
//    public CategoryChooser getBaseCategory() {
//        return baseCategory;
//    }
//
//    private static Options getOptions()
//    {
//        Options options = new Options();
//        options.addOption(OptionBuilder.withArgName(PARAM_NAME)
//            .hasArgs()
//            .withValueSeparator('&')
//            .withDescription("A set of key value pairs of options represented by key=value")
//            .create(PARAM));
//        return options;
//    }
//
//    private static Runner parseOptions(String argv[])
//    {
//        Parser cliParser = new BasicParser();
//        CommandLine cmd = null;
//        try {
//            cmd = cliParser.parse(getOptions(), argv);
//        } catch (ParseException pe) {
//            System.err.println("Error parsing command line.");
//        }
//        if (cmd.getArgs().length < 1) {
//            HelpFormatter formatter = new HelpFormatter();
//            formatter.printHelp("blurb XML_CONFIG", getOptions());
//            return null;
//        }
//        return getInstance(cmd);
//    }
//
//    private static Runner getInstance(CommandLine cmd)
//    {
//        Runner myRunner = new Runner(cmd.getArgs()[0]);
//        if (cmd.hasOption(PARAM)) {
//            Map<String, String> parameters = new HashMap<String, String>();
//            String[] results = cmd.getOptionValues(PARAM);
//            for(String myResult : results) {
//                String[] twoParts = myResult.split("=");
//                parameters.put(twoParts[0],twoParts[1]);
//            }
//            myRunner.baseCategory.setParams(parameters);
//        }
//        return myRunner;
//    }
//
//    @Inject
//    public Runner(@Named("Config") String filename, Selector numberGen)
//    {
//        Document xmlDoc = getDocument(filename);
//        pattern = getPattern(xmlDoc);
//        baseCategory = new CategoryChooser(pattern, numberGen);
//    }
//
//    public Runner(String filename)
//    {
//        this(filename, new RandomSelector());
//    }
//
//    public Node getPattern() {
//        return pattern;
//    }
//
//    public String getOutput() {
//        return baseCategory.chooseEntry().getOutput();
//    }
//
//    /**
//     * Given a filename, return DOM Document object
//     *
//     * @param filename XML file
//     * @return DOM document
//     */
//    private Document getDocument(String filename)
//    {
//        File blurbXml = new File(filename);
//        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder dBuilder = null;
//        try {
//            dBuilder = dbFactory.newDocumentBuilder();
//        } catch (ParserConfigurationException pce) {
//            System.err.println("Parser configuration:  " + pce.getMessage());
//            System.exit(1);
//        }
//        try {
//            return dBuilder.parse(blurbXml);
//        } catch (Exception e) {
//            System.err.println("Parse Exception:  " + e.getMessage());
//            System.exit(1);
//        }
//        return null;
//    }
//
//    private final Node getPattern(Document xmlFile)
//    {
//        return xmlFile.getDocumentElement().getElementsByTagName("pattern").item(0);
//    }
//}
