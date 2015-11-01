package com.jolyjonesfamily.blurb.map;

import com.google.inject.Inject;
import com.jolyjonesfamily.blurb.models.Blurb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * This class exists to map XML into our model classes.  It's currently
 * a straightforward use of JAXB
 *
 * Created by samjones on 10/18/15.
 */
public class MapBlurbXML implements MapBlurb {
    /**
     * File that contains the XML for defining the Blurb.
     */
    private File blurbFile;

    /**
     * Basic constructor with XML settings input.
     *
     * @param blurbFile Blurb settings in XML.
     */
    @Inject
    public MapBlurbXML(File blurbFile) {
        setBlurbFile(blurbFile);
    }

    /**
     * Retrieve the file with the XML definition.
     *
     * @return XML file data
     */
    public File getBlurbFile() {
        return blurbFile;
    }

    /**
     * Set file for XML reading.
     *
     * @param blurbFile File object with Blurb XML.
     */
    public void setBlurbFile(File blurbFile) {
        this.blurbFile = blurbFile;
    }

    /**
     * Same thing, taking a string filename.
     *
     * @param blurbFileName String describing where the Blurb
     *                      XML is.
     */
    public void setBlurbFile(String blurbFileName) {
        this.setBlurbFile(new File(blurbFileName));
    }

    /**
     * Call JAXB to map the XML onto the model classes.
     *
     * @return Blurb model with all the description from the
     * XML file.
     * @throws JAXBException
     */
    public Blurb getMappedBlurb() throws JAXBException
    {
        JAXBContext context = JAXBContext.newInstance(Blurb.class, Blurb.class);

        Unmarshaller unmarshaller = context.createUnmarshaller();
        Object result = unmarshaller.unmarshal(blurbFile);
        return (Blurb) result;
    }
}
