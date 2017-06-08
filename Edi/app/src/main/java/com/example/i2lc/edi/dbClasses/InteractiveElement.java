package com.example.i2lc.edi.dbClasses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vlad on 11/05/2017.
 */

public class InteractiveElement implements Parcelable {
    private int interactiveElementID; //DB
    private int xml_element_id; //DB
    private int slideNumber; //DB
    private boolean live; //db
    private int presentationID;
    private String interactiveElementQuestion;
    protected String type;
    private int responsesInterval;
    private String answers;

    public InteractiveElement() {
    }

    public InteractiveElement(int interactiveElementID, int presentationID, String interactiveElementQuestion,
                              String type, boolean live, int slideNumber, int xml_element_id) {
        this.interactiveElementID = interactiveElementID;
        this.presentationID = presentationID;
        this.interactiveElementQuestion = interactiveElementQuestion;
        this.type = type;
        this.live = live;
        this.slideNumber = slideNumber;
        this.xml_element_id = xml_element_id;
    }

    protected InteractiveElement(Parcel in) {
        interactiveElementID = in.readInt();
        xml_element_id = in.readInt();
        slideNumber = in.readInt();
        live = in.readByte() != 0;
        presentationID = in.readInt();
        interactiveElementQuestion = in.readString();
        type = in.readString();
        responsesInterval = in.readInt();
        answers = in.readString();
    }

    public static final Creator<InteractiveElement> CREATOR = new Creator<InteractiveElement>() {
        @Override
        public InteractiveElement createFromParcel(Parcel in) {
            return new InteractiveElement(in);
        }

        @Override
        public InteractiveElement[] newArray(int size) {
            return new InteractiveElement[size];
        }
    };

    public int getInteractiveElementID() {
        return interactiveElementID;
    }

    public void setInteractiveElementID(int interactiveElementID) {
        this.interactiveElementID = interactiveElementID;
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

    public int getXml_element_id() {
        return xml_element_id;
    }

    public void setXml_element_id(int xml_element_id) {
        this.xml_element_id = xml_element_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(interactiveElementID);
        dest.writeInt(xml_element_id);
        dest.writeInt(slideNumber);
        dest.writeByte((byte) (live ? 1 : 0));
        dest.writeInt(presentationID);
        dest.writeString(interactiveElementQuestion);
        dest.writeString(type);
        dest.writeInt(responsesInterval);
        dest.writeString(answers);
    }
}
