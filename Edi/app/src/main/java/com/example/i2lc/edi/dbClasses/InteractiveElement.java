package com.example.i2lc.edi.dbClasses;

import java.util.ArrayList;

/**
 * Created by vlad on 11/05/2017.
 */

public class InteractiveElement {

    protected int interactiveElementID;
    protected int presentationID;
    protected String InteractiveElementData;
    protected String type;
    protected boolean live;
    protected String responsesInterval; //TODO: let it be string for know
    protected int slideNumber;
    protected ArrayList<Interaction> interactions;

    public InteractiveElement() {

    }

    public InteractiveElement(int interactiveElementID, int presentationID, String interactiveElementData,
                              String type, boolean live, int slideNumber, String responsesInterval) {
        this.interactiveElementID = interactiveElementID;
        this.presentationID = presentationID;
        InteractiveElementData = interactiveElementData;
        this.type = type;
        this.live = live;
        this.responsesInterval = responsesInterval;
        this.slideNumber = slideNumber;
    }

    public int getInteractiveElementID() {
        return interactiveElementID;
    }

    public void setInteractiveElementID(int interactiveElementID) {
        this.interactiveElementID = interactiveElementID;
    }

    public int getPresentationID() {
        return presentationID;
    }

    public void setPresentationID(int presentationID) {
        this.presentationID = presentationID;
    }

    public String getInteractiveElementData() {
        return InteractiveElementData;
    }

    public void setInteractiveElementData(String interactiveElementData) {
        InteractiveElementData = interactiveElementData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public String getResponsesInterval() {
        return responsesInterval;
    }

    public void setResponsesInterval(String responsesInterval) {
        this.responsesInterval = responsesInterval;
    }

    public int getSlideNumber() {
        return slideNumber;
    }

    public void setSlideNumber(int slideNumber) {
        this.slideNumber = slideNumber;
    }

    public ArrayList<Interaction> getInteractions() {
        return interactions;
    }

    public void setInteractions(ArrayList<Interaction> interactions) {
        this.interactions = interactions;
    }
}
