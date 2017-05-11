package com.example.i2lc.edi.dbClasses;

import java.util.Date;

/**
 * Created by vlad on 11/05/2017.
 */

public class Module {

    protected int moduleID;
    protected String description;
    protected String timeLastUpdate; //let it be string for now, in DB is interval type
    protected String moduleName;
    protected Date timeCreated;

    public Module() {

    }

    public Module(int moduleID, String description, String timeLastUpdate, String moduleName, Date timeCreated) {
        this.moduleID = moduleID;
        this.description = description;
        this.timeLastUpdate = timeLastUpdate;
        this.moduleName = moduleName;
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

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }
}
