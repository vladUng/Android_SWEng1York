package com.example.i2lc.edi.dbClasses;

import java.sql.Timestamp;

/**
 * Created by vlad on 11/05/2017.
 */

public class Interaction {

    protected int interactionID;
    protected int userID;
    protected int interactiveElementID;
    protected String interactionData;
    protected Timestamp timeCreated;

    public Interaction() {

    }
    public Interaction(int interactionID, int userID, int interactiveElementID, String interactionData, Timestamp timeCreated) {
        this.interactionID = interactionID;
        this.userID = userID;
        this.interactiveElementID = interactiveElementID;
        this.interactionData = interactionData;
        this.timeCreated = timeCreated;
    }

    public int getInteractionID() {
        return interactionID;
    }

    public void setInteractionID(int interactionID) {
        this.interactionID = interactionID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getInteractiveElementID() {
        return interactiveElementID;
    }

    public void setInteractiveElementID(int interactiveElementID) {
        this.interactiveElementID = interactiveElementID;
    }

    public Timestamp getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Timestamp timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getInteractionData() {
        return interactionData;
    }

    public void setInteractionData(String interactionData) {
        this.interactionData = interactionData;
    }
}
