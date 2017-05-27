package com.example.i2lc.edi.dbClasses;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by vlad on 11/05/2017.
 */

public class InteractiveElement {

    protected int interactiveElementID;
    protected int presentationID;
    protected String InteractiveElementData;
    protected String type;
    protected boolean live;
    protected Date responsesInterval;
    protected int slideNumber;
    protected ArrayList<Interaction> interactions;

    public InteractiveElement() {

    }

    public InteractiveElement(int interactiveElementID, int presentationID, String interactiveElementData,
                              String type, boolean live, int slideNumber, Date responsesInterval) {
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

    public Date getResponsesInterval() {
        return responsesInterval;
    }

    public void setResponsesInterval(Date responsesInterval) {
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
