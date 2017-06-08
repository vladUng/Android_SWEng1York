package com.example.i2lc.edi.dbClasses;

import android.os.Build;
import android.os.StrictMode;

import com.example.i2lc.edi.backend.SocketClient;

import java.sql.Timestamp;

/**
 * Created by vlad on 11/05/2017.
 */

public class Question {

    protected int questionID;
    protected int userID;
    protected int presentationID;
    protected Timestamp dateCreated;
    protected String questionData;
    protected int slideNumber;

    public Question() {
    }

    /**
     *  Constructor for when the question is taken from the server
     *
     * @param questionID
     * @param userID
     * @param presentationID
     * @param dateCreated
     * @param questionData
     * @param slideNumber
     */
    public Question(int questionID, int userID, int presentationID, Timestamp dateCreated, String questionData, int slideNumber) {
        this.questionID = questionID;
        this.userID = userID;
        this.presentationID = presentationID;
        this.dateCreated = dateCreated;
        this.questionData = questionData;
        this.slideNumber = slideNumber;
    }

    /**
     *
     * Constructor used to create an object when the question is sent to server
     * @param userID
     * @param presentationID
     * @param questionData
     * @param slideNumber
     */
    public Question(int userID, int presentationID, String questionData, int slideNumber){
        this.userID = userID;
        this.presentationID = presentationID;
        this.questionData = questionData;
        this.slideNumber = slideNumber;
    }

    public String getQuestionData() {
        return questionData;
    }

    public void setQuestionData(String questionData) {
        this.questionData = questionData;
    }

    /**
     *
     * Sends the data to server, throws error if fails
     *
     *
     * @throws Exception
     */
    public void sendQuestion() throws Exception {
        int SDK_INT = Build.VERSION.SDK_INT;
        // >SDK 8 support async operations
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            //connect client
            SocketClient socketClient = new SocketClient();
            Boolean success = socketClient.postQuestion(userID,presentationID, questionData, slideNumber);

            if (success) {
                //for debug
                System.out.println("YAY the question was successfully sent");
            } else {
                //for debug
                System.out.println("There was an error sending the question to server");
            }
        } else {
            //for debug
            System.out.println("There was an error. SDK too old");
            throw new Exception();
        }
    }
}
