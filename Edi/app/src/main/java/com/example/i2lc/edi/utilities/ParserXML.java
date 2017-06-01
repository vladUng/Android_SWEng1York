package com.example.i2lc.edi.utilities;

import com.example.i2lc.edi.dbClasses.InteractiveElement;
import com.example.i2lc.edi.dbClasses.Presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by habl on 23/02/2017.
 */
public class ParserXML {

    //private DOMParser xmlParser;
    private Document xmlDocument;
    private String presentationXmlPath = "projectResources/sampleFiles/xml/i2lp_sample_xml.xmlml";
    private File xmlFile;
    private Logger logger = LoggerFactory.getLogger(ParserXML.class);
    private Presentation presentation;
    private ArrayList<String> faultsDetected = new ArrayList<>();
    private ArrayList<Presentation> presentationList;

    public ParserXML(Presentation presentation, File xmlFile){// throws InvalidPathException {
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
        parseSlidesAndSlideElements();

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
    private void parseSlidesAndSlideElements() {
        //Loop through each slide and add elements to every slide and every slide to the presentation:
        //Instantiate an array to add the slides to
        ArrayList<Slide> slideArray = new ArrayList<>();

        //Find all elements named "slide"
        NodeList slideNodeList = xmlDocument.getElementsByTagName("slide");

        if (slideNodeList.getLength() != 0) {
            //For all slides:
            for (int i = 0; i < slideNodeList.getLength(); i++) {
                //Create a new slide for every slide element
                Slide mySlide = new Slide();
                //Create a slideElement array to store elements on the current slide
                ArrayList<InteractiveElement> slideElementArrayList = new ArrayList<>();

                //Find the current slide node
                Node slideNode = slideNodeList.item(i);

                if (slideNode.getAttributes().getLength() != 0) {
                    //A slide only has one attribute, slideID
                    String attributeName = slideNode.getAttributes().item(0).getNodeName();

                    if (attributeName.equals("slideid")) {
                        String attrContent = slideNode.getAttributes().item(0).getNodeValue();
                        mySlide.setSlideID(Integer.valueOf(attrContent));
                    }
                }

                //Find all children of the current slide:
                NodeList slideNodeChildrenList = slideNode.getChildNodes();
                //For all elements on a slide:
                for (int j = 0; j < slideNodeChildrenList.getLength(); j++) {
                    //Find the current slide element (slide child)
                    Node slideElementNode = slideNodeChildrenList.item(j);

                    if (slideElementNode.getNodeType() == Node.ELEMENT_NODE) {
                        String elementName = slideElementNode.getNodeName();
                        switch (elementName) {
                            case "poll":
                                InteractiveElement pollElement = new InteractiveElement();
                                pollElement.setType("poll");
                                parsePollElement(slideElementNode, pollElement);
                                parseElementAttributes(slideElementNode, pollElement);
                                slideElementArrayList.add(pollElement);
                                break;
                            case "wordcloud":
                                InteractiveElement wordCloudElement = new InteractiveElement();
                                wordCloudElement.setType("wordcloud");
                                parseElementAttributes(slideElementNode, wordCloudElement);
                                parseWordCloudElement(slideElementNode, wordCloudElement);
                                slideElementArrayList.add(wordCloudElement);
                                break;

                            default:
                                logger.warn("SlideElement Name Not Recognised! Name: " + elementName);
                                faultsDetected.add("SlideElement Name Not Recognised! Name: " + elementName);
                        }
                    }
                }
                mySlide.setSlideElementList(slideElementArrayList);
                slideArray.add(mySlide);
            }
        } else {
            logger.warn("No slides found!");
            faultsDetected.add("No slides found!");
        }
        presentation.setSlideList(slideArray);
    }
    private void parseWordCloudElement(Node wordCloudElementNode, InteractiveElement wordCloudElement) {
        //Find and store all elements of the wordcloud element
        NodeList textNodeChildrenList = wordCloudElementNode.getChildNodes();
        for (int i = 0; i < textNodeChildrenList.getLength(); i++) {
            //Find the current element node
            Node elementNode = textNodeChildrenList.item(i);
            if (elementNode.getNodeType() == Node.ELEMENT_NODE) {
                //Find the element name and its content, and store this in the wordCloudElement
                String elementName = elementNode.getNodeName();
                String elementContent = elementNode.getTextContent();
                switch (elementName){
                    case "question":
                        wordCloudElement.setInteractiveElementQuestion(elementContent);
                        break;
                    case "timelimit":
                        wordCloudElement.setResponsesInterval(Integer.valueOf(elementContent));
                        break;
                    default:
                        logger.warn("Wordcloud Element Property Name Not Recognised! Name: " + elementName +
                                ", Value: " + elementContent + ", and XML-Type: " + elementNode.getNodeType());
                        faultsDetected.add("Wordcloud Element Property Name Not Recognised! Name: " + elementName +
                                ", Value: " + elementContent + ", and XML-Type: " + elementNode.getNodeType());
                }
            }
        }
    }
    private void parsePollElement(Node pollElementNode, InteractiveElement pollElement) {
        //Find and store all elements of the poll element
        NodeList textNodeChildrenList = pollElementNode.getChildNodes();
        for (int i = 0; i < textNodeChildrenList.getLength(); i++) {
            //Find the current element node
            Node elementNode = textNodeChildrenList.item(i);
            if (elementNode.getNodeType() == Node.ELEMENT_NODE) {
                //Find the element name and its content, and store this in the pollElement
                String elementName = elementNode.getNodeName();
                String elementContent = elementNode.getTextContent();

                switch (elementName){
                    case "question":
                        pollElement.setInteractiveElementQuestion(elementContent);
                        break;
                    case "answers":
                        pollElement.setAnswers(elementContent);
                        break;
                    case "timelimit":
                        pollElement.setResponsesInterval(Integer.valueOf(elementContent));
                        break;
                    default:
                        logger.warn("Poll Element Property Name Not Recognised! Name: " + elementName +
                                ", Value: " + elementContent + ", and XML-Type: " + elementNode.getNodeType());
                        faultsDetected.add("Poll Element Property Name Not Recognised! Name: " + elementName +
                                ", Value: " + elementContent + ", and XML-Type: " + elementNode.getNodeType());
                }
            }
        }
    }
    private void parseElementAttributes (Node slideElementNode, InteractiveElement slideElement) {
        for (int i = 0; i < slideElementNode.getAttributes().getLength(); i++) {
            //Find the current attribute node
            Node attributeNode = slideElementNode.getAttributes().item(i);

            if (attributeNode.getNodeType() == Node.ATTRIBUTE_NODE) {
                //Find the attribute name and its content, and store this in the element
                String attributeName = attributeNode.getNodeName();
                String attributeContent = attributeNode.getNodeValue();

                switch (attributeName) {
                    case "elementid":
                        slideElement.setXml_element_id(Integer.valueOf(attributeContent));
                        break;
                    default:
                        logger.warn("Attribute Not Recognised! Name: " + attributeName +
                                ", Value: " + attributeContent + ", and Type: " + attributeNode.getNodeType());
                        faultsDetected.add("Attribute Not Recognised! Name: " + attributeName +
                                ", Value: " + attributeContent + ", and Type: " + attributeNode.getNodeType());
                }
            }
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