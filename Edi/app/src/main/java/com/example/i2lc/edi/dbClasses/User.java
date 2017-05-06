package com.example.i2lc.edi.dbClasses;

/**
 * Created by vlad on 06/05/2017.
 */

public class User {

    protected Integer userID;
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

    //Constructor used when adding Users
    public User(Integer userID, String userType, String username, String firstName, String secondName, String emailAddress) {
        super();
        this.userID = userID;
        this.userType = userType;
        this.username = username;
        this.firstName = firstName;
        this.secondName = secondName;
        this.emailAddress = emailAddress;

        if(userType.equals("teacher")) this.teacher=true;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public boolean isTeacher() {
        return teacher;
    }

    public void setTeacher(boolean teacher) {
        this.teacher = teacher;
    }

    public int getActivePresentationID() {
        return activePresentationID;
    }

    public void setActivePresentationID(int activePresentationID) {
        this.activePresentationID = activePresentationID;
    }
}

