package com.example.i2lc.edi.utilities;

import android.renderscript.ScriptGroup;
import android.view.View;

import com.example.i2lc.edi.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import java.util.ArrayList;


/**
 * Created by habl on 23/02/2017.
 */
import com.example.i2lc.edi.dbClasses.Presentation;
public class ParserXML {

    //private DOMParser xmlParser;
    private Document xmlDocument;
    private String presentationXmlPath = "projectResources/sampleFiles/xml/i2lp_sample_xml.xmlml";
    private File xmlFile;
    private Logger logger = LoggerFactory.getLogger(ParserXML.class);
    private Presentation presentation;
    private ArrayList<String> faultsDetected = new ArrayList<>();
    private ArrayList<Presentation> presentationList;

    public ParserXML(View rootView, Presentation presentation, File xmlFile){// throws InvalidPathException {
        this.presentationList = presentationList;
        this.presentation = presentation;
        this.xmlFile = xmlFile;
//         if (this.validateExtension(presentationXmlPath))
//        {
            //this.presentationXmlPath = presentationXmlPath; //Set the path if valid
//            logger.info("Path valid...");
//        } else {
//            logger.warn("Path not valid, sample XML loaded...");
//            throw new InvalidPathException(presentationXmlPath, presentationXmlPath.substring(presentationXmlPath.lastIndexOf(".") + 1));
//        }

        XMLDOMParser parser = new XMLDOMParser();
        //InputStream stream = rootView.getContext().getResources().openRawResource(R.raw.i2lp_sample_xml);
        InputStream stream = null;
        try {
            stream = new FileInputStream(xmlFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        xmlDocument = parser.getDocument(stream);

    }

    public Presentation parsePresentation() {
        //presentation = new Presentation();

        parseDocumentDetails();
        setTotalSlideNum();

//        if (faultsDetected.size() > 0) {
//            presentation.setXmlFaults(faultsDetected);
//        }
//        logger.info("Presentation Parsed. Faults found: " + this.faultsDetected.size());

        return presentation;
    }


    private void parseDocumentDetails() {
        //Store the document details:
        // Find elements named "documentdetails"
        NodeList documentDetailsList = xmlDocument.getElementsByTagName("documentdetails");

        if ( documentDetailsList.getLength() != 0 && documentDetailsList.item(0).getNodeType() == Node.ELEMENT_NODE) {
            //There is only one "documentdetails" element, hence choose the first indexed node.
            Node documentDetailsNode = documentDetailsList.item(0);

            // List of all elements under(child of) "documentdetails"
            NodeList documentDetailsChildrenList = documentDetailsNode.getChildNodes();

            //Go through all child elements of "documentdetails" and store them in their respective fields
            // in the presentation.
            for (int i = 0; i < documentDetailsChildrenList.getLength(); i++) {
                //Find the current node
                Node documentDetailsElementNode = documentDetailsChildrenList.item(i);

                //If the node is an element node, find its nodeName /elementTag and
                // set the respective fields in the presentation.
                if (documentDetailsElementNode.getNodeType() == Node.ELEMENT_NODE) {
                    String elementName = documentDetailsElementNode.getNodeName();
                    String elementContent = documentDetailsElementNode.getTextContent();

                    switch (elementName) {
                        case "title":
                            presentation.setTitle(elementContent);
                            break;
                        case "author":
                            presentation.setAuthor(elementContent);
                            break;
                        case "description":
                            presentation.setDescription(elementContent);
                            break;
                        default:
                            logger.warn("Document Detail Not Recognised! Name: " + elementName +
                                    ", Value: " + elementContent + ", and XML-Type: " + documentDetailsElementNode.getNodeType());
                            faultsDetected.add("Document Detail Not Recognised! Name: " + elementName +
                                    ", Value: " + elementContent + ", and XML-Type: " + documentDetailsElementNode.getNodeType());
                    }
                }
            }
        } else {
            logger.warn("No Document Details Tag Found!");
            faultsDetected.add("No Document Details Tag Found!");
        }
    }



    private void setTotalSlideNum(){
        //Find all elements named "slide"
        NodeList slideNodeList = xmlDocument.getElementsByTagName("slide");
        presentation.setTotalSlideNumber(slideNodeList.getLength());
    }



    public Boolean validateExtension(String path) {
        Boolean validated = false;
        if(path.lastIndexOf(".") != -1 && path.lastIndexOf(".") != 0) {
            String extension = path.substring(path.lastIndexOf(".") + 1);
            if ((extension).equals("xml")) validated = true;
            else logger.warn("File Extension " + extension + " not accepted");
        }
        return validated;
    }

    private String produceLegalXmlCharacters(String textContent) {
        String xmlSafeHtmlText = "";
        //TODO do this properly, but not needed until writing
        return xmlSafeHtmlText;
    }
}