package com.example.i2lc.edi.dbClasses;

import java.net.URL;

/**
 * Created by vlad on 11/05/2017.
 */

public class Thumbnail {

    protected int thumbnailID;
    protected int presentationID;
    protected URL thumbnailURL;
    protected int slideNumber;

    public Thumbnail(){

    }

    public Thumbnail(int thumbnailID, int presentationID, URL thumbnailURL, int slideNumber) {
        this.thumbnailID = thumbnailID;
        this.presentationID = presentationID;
        this.thumbnailURL = thumbnailURL;
        this.slideNumber = slideNumber;
    }

    public int getThumbnailID() {
        return thumbnailID;
    }

    public void setThumbnailID(int thumbnailID) {
        this.thumbnailID = thumbnailID;
    }

    public int getPresentationID() {
        return presentationID;
    }

    public void setPresentationID(int presentationID) {
        this.presentationID = presentationID;
    }

    public URL getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(URL thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public int getSlideNumber() {
        return slideNumber;
    }

    public void setSlideNumber(int slideNumber) {
        this.slideNumber = slideNumber;
    }
}
