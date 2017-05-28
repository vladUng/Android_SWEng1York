package com.example.i2lc.edi.dbClasses;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by vlad on 11/05/2017.
 */

public class Presentation {

    protected int presentationID;
    protected int moduleID;
    protected URL xmlURL;
    protected int currentSlideNumber;
    protected ArrayList<InteractiveElement> interactiveElements;
    protected int totalSlideNumber;
    protected Thumbnail thumbnail;
    protected String title = "N/A"; //TODO remove initiliasations when finished
    protected String module=  "N/A";
    protected String author = "N/A";
    protected String description = "N/A";
    protected boolean live = true;
    protected String folderPath;
    protected String thumbnailPath;



    public Presentation(){

    }

    public Presentation(int presentationID, int moduleID, URL xmlURL, boolean live) {
        this.presentationID = presentationID;
        this.moduleID = moduleID;
        this.xmlURL = xmlURL;
        this.live = live;
        //TODO presentation thumbnail parsing in constructor
    }


    public int getPresentationID() {
        return presentationID;
    }

    public void setPresentationID(int presentationID) {
        this.presentationID = presentationID;
    }

    public int getModuleID() {
        return moduleID;
    }

    public void setModuleID(int moduleID) {
        this.moduleID = moduleID;
    }

    public URL getXmlURL() {
        return xmlURL;
    }

    public void setXmlURL(URL xmlURL) {
        this.xmlURL = xmlURL;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public int getCurrentSlideNumber() {
        return currentSlideNumber;
    }

    public void setCurrentSlideNumber(int currentSlideNumber) {
        this.currentSlideNumber = currentSlideNumber;
    }

    public int getTotalSlideNumber() {
        return totalSlideNumber;
    }

    public void setTotalSlideNumber(int totalSlideNumber) {
        this.totalSlideNumber = totalSlideNumber;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public ArrayList<InteractiveElement> getInteractiveElements() {
        return interactiveElements;
    }

    public void setInteractiveElements(ArrayList<InteractiveElement> interactiveElements) {
        this.interactiveElements = interactiveElements;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }
}
