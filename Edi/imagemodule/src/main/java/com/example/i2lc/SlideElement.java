package com.example.i2lc;

import android.graphics.Canvas;

public abstract class SlideElement {

    public SlideElement(){
    }

    //sequence and drawing
    public abstract void onDraw(Canvas canvas);
    public abstract int getStartSequence();
    public abstract void setStartSequence(int s);
    public abstract int getEndSequence();
    public abstract void setEndSequence(int s);


    //click related
    public abstract  boolean liesWithin(int x,int y,int width,int height);
    public abstract boolean intersects(int x,int y);
    public abstract String onClick();
//    public abstract String onDoubleClickAction();
//    public abstract String onLongClickAction();
//    public abstract boolean isClickable();
//    public abstract void setClickable(boolean clickable);


}
