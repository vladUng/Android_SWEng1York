package com.example.i2lc.edi.dbClasses;

/**
 * Created by vlad on 11/05/2017.
 */

public class InteractiveElement {
    protected int interactiveElementID;
    protected int presentationID;
    protected String interactiveElementQuestion;
    protected String type;
    protected boolean live;
    protected int responsesInterval;
    protected int slideNumber;
    protected String answers;
    //protected ArrayList<Interaction> interactions;

    public InteractiveElement() {
    }

    public InteractiveElement(int interactiveElementID, int presentationID, String interactiveElementQuestion,
                              String type, boolean live, int slideNumber, int responsesInterval) {
        this.interactiveElementID = interactiveElementID;
        this.presentationID = presentationID;
        this.interactiveElementQuestion = interactiveElementQuestion;
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

    public String getInteractiveElementQuestion() {
        return interactiveElementQuestion;
    }

    public void setInteractiveElementQuestion(String interactiveElementQuestion) {
        this.interactiveElementQuestion = interactiveElementQuestion;
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

    public int getResponsesInterval() {
        return responsesInterval;
    }

    public void setResponsesInterval(int responsesInterval) {
        this.responsesInterval = responsesInterval;
    }


    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public int getSlideNumber() {
        return slideNumber;
    }

    public void setSlideNumber(int slideNumber) {
        this.slideNumber = slideNumber;
    }

//    public ArrayList<Interaction> getInteractions() {
//        return interactions;
//    }
//
//    public void setInteractions(ArrayList<Interaction> interactions) {
//        this.interactions = interactions;
//    }
}
