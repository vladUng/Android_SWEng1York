package com.example.i2lc.edi.dbClasses;

import android.os.Build;
import android.os.StrictMode;

import com.example.i2lc.edi.backend.SocketClient;

/**
 * Holds the relevant information from the Interactions table from the Database
 *
 * Created by vlad on 11/05/2017.
 */

public class Interaction {

    protected int userID;
    protected int interactiveElementID;
    protected String interactionData;

    /**
     * Constructor
     * @param userID
     * @param interactiveElementID
     * @param interactionData
     */
    public Interaction(int userID, int interactiveElementID, String interactionData) {
        this.userID = userID;
        this.interactiveElementID = interactiveElementID;
        this.interactionData = interactionData;
    }

    /**
     * Sends interaction to Database, throws exception if fails
     * @throws Exception
     */
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
                System.out.println("There was an error sending the Interaction to server: " + status);
            }
        } else {
            //for debug
            System.out.println("There was an error. SDK too old");
            throw new Exception();
        }
    }
}
