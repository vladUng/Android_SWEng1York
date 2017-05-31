package com.example.i2lc.edi.utilities;

import com.example.i2lc.edi.dbClasses.InteractiveElement;


import java.util.ArrayList;

/**
 * Created by habl on 23/02/2017.
 */
public class Slide{
    protected ArrayList<InteractiveElement> slideElementList;
    protected int slideID;

    public Slide() {
        slideElementList = new ArrayList<>();
    }

    public ArrayList<InteractiveElement> getSlideElementList() {
        return slideElementList;
    }

    public void setSlideElementList(ArrayList<InteractiveElement> slideElementList) {
        this.slideElementList = slideElementList;
    }

    public int getSlideID() {
        return slideID;
    }

    public void setSlideID(int slideID) {
        this.slideID = slideID;
    }
}

