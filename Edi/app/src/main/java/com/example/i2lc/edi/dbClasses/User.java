package com.example.i2lc.edi.dbClasses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vlad on 06/05/2017.
 */

public class User implements Parcelable {

    protected int userID;
    protected String firstName;
    protected String secondName;
    protected String username;
    protected String password;
    protected String userType;
    protected String emailAddress;
    protected int activePresentationID;
    protected boolean teacher;

    public User(){

    }

    /**
     *  Constructor
     *
     * @param userID
     * @param userType
     * @param username
     * @param firstName
     * @param secondName
     * @param emailAddress
     */
    public User(int userID, String userType, String username, String firstName, String secondName, String emailAddress) {
        super();
        this.userID = userID;
        this.userType = userType;
        this.username = username;
        this.firstName = firstName;
        this.secondName = secondName;
        this.emailAddress = emailAddress;

        if(userType.equals("teacher")) this.teacher=true;
    }

    protected User(Parcel in) {
        userID = in.readInt();
        firstName = in.readString();
        secondName = in.readString();
        username = in.readString();
        password = in.readString();
        userType = in.readString();
        emailAddress = in.readString();
        activePresentationID = in.readInt();
        teacher = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userID);
        dest.writeString(firstName);
        dest.writeString(secondName);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(userType);
        dest.writeString(emailAddress);
        dest.writeInt(activePresentationID);
        dest.writeByte((byte) (teacher ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int getUserID() {
        return userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}

