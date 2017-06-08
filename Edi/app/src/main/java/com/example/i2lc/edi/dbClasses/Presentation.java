package com.example.i2lc.edi.dbClasses;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.i2lc.edi.utilities.Slide;

import java.net.URL;
import java.util.ArrayList;

/**
 *
 *
 * Created by vlad on 11/05/2017.
 */

public class Presentation implements Parcelable{

    private int presentationID;
    private int moduleID;
    private URL xmlURL;
    private int currentSlideNumber;
    private int totalSlideNumber;
    private String title = "N/A";
    private String moduleName =  "N/A";
    private String author = "N/A";
    protected String description = "N/A";
    private boolean live = true;
    private String folderPath;
    private String thumbnailPath;
    private ArrayList<Slide> slideList;

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
        moduleName = in.readString();
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
    /**
     *
     * @return the InteractiveElement that is live, from the current presentation, otherwise null
     */
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

    public int getModuleID() {
        return moduleID;
    }

    public URL getXmlURL() {
        return xmlURL;
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

    /**
     *
     * @return the current progress in %
     */
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

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
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
        dest.writeString(moduleName);
        dest.writeString(author);
        dest.writeString(description);
        dest.writeByte((byte) (live ? 1 : 0));
        dest.writeString(folderPath);
        dest.writeString(thumbnailPath);
        dest.writeTypedList(slideList);
    }
}
