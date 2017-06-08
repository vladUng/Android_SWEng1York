package com.example.i2lc.edi.model;

/**
 * Class which contains an image ID and a title
 * used to create an item on the SlideBar.
 * Created by Cosmin Frateanu on 15/03/2017.
 */

public class ItemSlideMenu {
    private int imgId;
    private String title;

    /**
     * ItemSlideMenu Constructor
     * @param imgId
     * @param title
     */
    public ItemSlideMenu(int imgId, String title) {
        this.imgId = imgId;
        this.title = title;
    }

    public int getImgId() {
        return imgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
