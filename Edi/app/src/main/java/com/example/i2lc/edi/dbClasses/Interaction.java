package com.example.i2lc.edi.dbClasses;

import android.os.Build;
import android.os.StrictMode;

import com.example.i2lc.edi.backend.SocketClient;

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
    public Interaction(int userID, int interactiveElementID, String interactionData) {
        this.userID = userID;
        this.interactiveElementID = interactiveElementID;
        this.interactionData = interactionData;
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

    public void sendInteraction() throws Exception {
        int SDK_INT = Build.VERSION.SDK_INT;
        // >SDK 8 support async operations
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            //connect client
            SocketClient socketClient = new SocketClient();
            String status = socketClient.postInteraction(userID, interactiveElementID, interactionData);

            if (status.equals("success")) {
                //for debug
                System.out.println("YAY the question was successfully sent");
            } else {
                //for debug
                System.out.println("There was an error sending the question to server: " + status);
            }
        } else {
            //for debug
            System.out.println("There was an error. SDK too old");
            throw new Exception();
        }
    }
}
