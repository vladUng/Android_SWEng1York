package com.example.i2lc.edi.model;

/**
 * Created by Cosmin Frateanu on 22/04/2017.
 */

public class PresentationMod {
    private String title = "Very Cool Title";
    private String author = "Joe Bloggs";
    private String date = "22/04/2017";
    private int currentSlideNum = 0;
    private int totalSlideNum = 0;
    private boolean isActive = false;

    public PresentationMod(){
    }
    public PresentationMod(String title, String author, String date){
        this.title = title;
        this.author = author;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCurrentSlideNum() {
        return currentSlideNum;
    }

    public void setCurrentSlideNum(int slideNum) {
        this.currentSlideNum = slideNum;
    }

    public int getTotalSlideNum() {
        return totalSlideNum;
    }

    public void setTotalSlideNum(int totalSlideNum) {
        this.totalSlideNum = totalSlideNum;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
