package com.example.i2lc.edi.dbClasses;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.i2lc.edi.utilities.Slide;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by vlad on 11/05/2017.
 */

public class Presentation implements Parcelable{

    protected int presentationID;
    protected int moduleID;
    protected URL xmlURL;
    protected int currentSlideNumber;
//    protected ArrayList<InteractiveElement> interactiveElements;
    protected int totalSlideNumber;
    //protected Thumbnail thumbnail;
    protected String title = "N/A";
    protected String module=  "N/A";
    protected String author = "N/A";
    protected String description = "N/A";
    protected boolean live = true; //TODO remove initiliasations when finished
    protected String folderPath;
    protected String thumbnailPath;
    protected ArrayList<Slide> slideList;

    public Presentation(){
        slideList = new ArrayList<>();
    }

    public Presentation(int presentationID, int moduleID, URL xmlURL, boolean live) {
        this.presentationID = presentationID;
        this.moduleID = moduleID;
        this.xmlURL = xmlURL;
        this.live = live;
    }

    protected Presentation(Parcel in) {
        this();
        presentationID = in.readInt();
        moduleID = in.readInt();
        currentSlideNumber = in.readInt();
        totalSlideNumber = in.readInt();
        title = in.readString();
        module = in.readString();
        author = in.readString();
        description = in.readString();
        live = in.readByte() != 0;
        folderPath = in.readString();
        thumbnailPath = in.readString();
        in.readTypedList(slideList, Slide.CREATOR);
    }

    public static final Creator<Presentation> CREATOR = new Creator<Presentation>() {
        @Override
        public Presentation createFromParcel(Parcel in) {
            return new Presentation(in);
        }

        @Override
        public Presentation[] newArray(int size) {
            return new Presentation[size];
        }
    };



//    public static final Creator<Slide> CREATOR_SLIDE = new Creator<Slide>() {
//        @Override
//        public Slide createFromParcel(Parcel in) {
//            return new Slide(in);
//        }
//
//        @Override
//        public Slide[] newArray(int size) {
//            return new Slide[size];
//        }
//    };

    public InteractiveElement getLiveElement() {

        for (Slide slide: slideList) {
            for (InteractiveElement interactiveElement : slide.getSlideElementList()) {
                if (interactiveElement != null) {
                    if (interactiveElement.isLive()) {
                        return interactiveElement;
                    }
                }
            }
        }
        return null;
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

    public ArrayList<Slide> getSlideList() {
        return slideList;
    }

    public void setSlideList(ArrayList<Slide> slideList) {
        this.slideList = slideList;
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

    public int calculateProgress(){
        int progress = 0;
        if(totalSlideNumber!=0) {
            progress = (currentSlideNumber + 1) * 100 / (totalSlideNumber);
        }
        return progress;
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

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(presentationID);
        dest.writeInt(moduleID);
        dest.writeInt(currentSlideNumber);
        dest.writeInt(totalSlideNumber);
        dest.writeString(title);
        dest.writeString(module);
        dest.writeString(author);
        dest.writeString(description);
        dest.writeByte((byte) (live ? 1 : 0));
        dest.writeString(folderPath);
        dest.writeString(thumbnailPath);
        dest.writeTypedList(slideList);
    }
}
