package com.example.i2lc.edi.dbClasses;

import java.sql.Timestamp;

/**
 * Created by vlad on 11/05/2017.
 */

public class Question {

    protected int questionID;
    protected int userID;
    protected int presentationID;
    protected Timestamp dateCreated; //let it be string for now, in DB is interval type
    protected String questionData;
    protected int slideNumber;

    public Question() {

    }

    public Question(int questionID, int userID, int presentationID, Timestamp dateCreated, String questionData, int slideNumber) {
        this.questionID = questionID;
        this.userID = userID;
        this.presentationID = presentationID;
        this.dateCreated = dateCreated;
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

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
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
