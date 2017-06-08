package com.example.i2lc.edi.dbClasses;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by vlad on 11/05/2017.
 */

public class Module {

    private int moduleID;
    private String moduleName;
    private String subject;
    private String description;
    private Timestamp timeLastUpdate; //let it be string for now, in DB is interval type
    private Timestamp timeCreated;
    private ArrayList<Presentation> presentations;


    public Module(int moduleID,  String moduleName, String subject, String description, Timestamp timeLastUpdate, Timestamp timeCreated) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getSubject() {
        return subject;
    }

    public ArrayList<Presentation> getPresentations() {
        return presentations;
    }

    public void setPresentations(ArrayList<Presentation> presentations) {
        this.presentations = presentations;
    }
}
