package com.jolyjonesfamily.blurb;

import com.jolyjonesfamily.blurb.map.MapBlurb;
import com.jolyjonesfamily.blurb.map.MapBlurbXML;
import org.apache.commons.cli.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

///**
// * Created by samjones on 4/2/14.
// */
public class Runner {
    private BlurbCatalog catalog;
    private Map parameters;
    private static Runner instance;
    private static final char PARAM = 'p';
    private static final String PARAM_NAME = "parameters";


    public static void main(String argv[]) {
        Runner myRunner = parseOptions(argv);
        if (myRunner != null) {
            System.out.println(myRunner.getOutput());
        }
    }

    public static String run(String filename) throws Exception {
        return getInstance(filename).getOutput();
    }

    //
//    public CategoryChooser getBaseCategory() {
//        return baseCategory;
//    }
//
    private static Options getOptions() {
        Options options = new Options();
        options.addOption(OptionBuilder.withArgName(PARAM_NAME)
                .hasArgs()
                .withValueSeparator('&')
                .withDescription("A set of key value pairs of options represented by key=value")
                .create(PARAM));
        return options;
    }

    private static Runner parseOptions(String argv[]) {
        Parser cliParser = new BasicParser();
        CommandLine cmd = null;
        try {
            cmd = cliParser.parse(getOptions(), argv);
        } catch (ParseException pe) {
            System.err.println("Error parsing command line.");
        }
        if (cmd.getArgs().length < 1) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("blurb XML_CONFIG", getOptions());
            return null;
        }
        try {
            return getInstance(cmd);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    //
    private static Runner getInstance(CommandLine cmd) throws Exception {
        instance = getInstance(cmd.getArgs()[0]);
        if (cmd.hasOption(PARAM)) {
            Map<String, String> parameters = new HashMap<String, String>();
            String[] results = cmd.getOptionValues(PARAM);
            for (String myResult : results) {
                String[] twoParts = myResult.split("=");
                parameters.put(twoParts[0], twoParts[1]);
            }
            instance.setParameters(parameters);
        }
        return instance;
    }

    private static Runner getInstance() throws Exception {
        if (instance == null) {
            instance = new Runner();
        }
        instance.setParameters(new HashMap());
        return instance;
    }

    public static Runner getInstance(MapBlurb map) throws Exception {
        return getInstance().setCatalog(new BlurbCatalog(map));
    }

    public static Runner getInstance(String filename) throws Exception
    {
        return getInstance(new MapBlurbXML(new File(filename)));
    }

    public Map getParameters() {
        return parameters;
    }

    public void setParameters(Map parameters) {
        this.parameters = parameters;
    }

    public Runner() throws Exception
    {
//        setCatalog(new BlurbCatalog(new MapBlurbXML(new File(filename))));
    }

    public BlurbCatalog getCatalog() {
        return catalog;
    }

    public Runner setCatalog(BlurbCatalog catalog) {
        this.catalog = catalog;
        return instance;
    }

    public String getOutput() {
        return catalog.fetch(getParameters()).getOutput();
    }
}
