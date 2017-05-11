package com.example.i2lc.edi.dbClasses;

import java.sql.Time;

/**
 * Created by vlad on 11/05/2017.
 */

public class Question {

    protected int questionID;
    protected int userID;
    protected int presentationID;
    protected String dateCreated; //let it be string for now, in DB is interval type
    protected Time timeAnswered; //in DB is time type, not sure if it's the same as Time in java
    protected String questionData;
    protected int slideNumber;

    public Question() {

    }

    public Question(int questionID, int userID, int presentationID, String dateCreated, Time timeAnswered, String questionData, int slideNumber) {
        this.questionID = questionID;
        this.userID = userID;
        this.presentationID = presentationID;
        this.dateCreated = dateCreated;
        this.timeAnswered = timeAnswered;
        this.questionData = questionData;
        this.slideNumber = slideNumber;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getPresentationID() {
        return presentationID;
    }

    public void setPresentationID(int presentationID) {
        this.presentationID = presentationID;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Time getTimeAnswered() {
        return timeAnswered;
    }

    public void setTimeAnswered(Time timeAnswered) {
        this.timeAnswered = timeAnswered;
    }

    public String getQuestionData() {
        return questionData;
    }

    public void setQuestionData(String questionData) {
        this.questionData = questionData;
    }

    public int getSlideNumber() {
        return slideNumber;
    }

    public void setSlideNumber(int slideNumber) {
        this.slideNumber = slideNumber;
    }
}
