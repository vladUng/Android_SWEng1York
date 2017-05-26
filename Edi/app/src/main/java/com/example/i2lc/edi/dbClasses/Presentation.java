package com.example.i2lc.edi.dbClasses;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by vlad on 11/05/2017.
 */

public class Presentation {

    protected int presentationID;
    protected int moduleID;
    protected URL xmlURL;
    protected boolean live;
    protected int currentSlideNumber;
    protected ArrayList<InteractiveElement> interactiveElements;
    //TODO:Add total num of slides
    //TODO:And current slide ID

    public Presentation(){

    }

    public Presentation(int presentationID, int moduleID, URL xmlURL, boolean live) {
        this.presentationID = presentationID;
        this.moduleID = moduleID;
        this.xmlURL = xmlURL;
        this.live = live;
    }

    public int getPresentationID() {
        return presentationID;
    }

    public void setPresentationID(int presentationID) {
        this.presentationID = presentationID;
    }

    public int getModuleID() {
        return moduleID;
    }

    public void setModuleID(int moduleID) {
        this.moduleID = moduleID;
    }

    public URL getXmlURL() {
        return xmlURL;
    }

    public void setXmlURL(URL xmlURL) {
        this.xmlURL = xmlURL;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public int getCurrentSlideNumber() {
        return currentSlideNumber;
    }

    public void setCurrentSlideNumber(int currentSlideNumber) {
        this.currentSlideNumber = currentSlideNumber;
    }

    public ArrayList<InteractiveElement> getInteractiveElements() {
        return interactiveElements;
    }

    public void setInteractiveElements(ArrayList<InteractiveElement> interactiveElements) {
        this.interactiveElements = interactiveElements;
    }
}
