package com.example.i2lc.edi.dbClasses;

import java.util.ArrayList;

/**
 * Created by vlad on 11/05/2017.
 */

public class Module {

    protected int moduleID;
    protected String moduleName;
    protected String subject;
    protected String description;
    protected String timeLastUpdate; //let it be string for now, in DB is interval type
    protected String timeCreated; //TODO revert this change
    protected ArrayList<Presentation> presentations;

    public Module() {

    }

    public Module(int moduleID,  String moduleName, String subject, String description, String timeLastUpdate, String timeCreated) {
        this.moduleID = moduleID;
        this.moduleName = moduleName;
        this.subject = subject;
        this.description = description;
        this.timeLastUpdate = timeLastUpdate;
        this.timeCreated = timeCreated;
    }

    public int getModuleID() {
        return moduleID;
    }

    public void setModuleID(int moduleID) {
        this.moduleID = moduleID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimeLastUpdate() {
        return timeLastUpdate;
    }

    public void setTimeLastUpdate(String timeLastUpdate) {
        this.timeLastUpdate = timeLastUpdate;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public ArrayList<Presentation> getPresentations() {
        return presentations;
    }

    public void setPresentations(ArrayList<Presentation> presentations) {
        this.presentations = presentations;
    }
}
