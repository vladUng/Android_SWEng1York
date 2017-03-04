package com.example.i2lc.imagemodule;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.StrictMode;
import android.widget.ImageView;

/**
 * Created by vlad on 04/03/2017.
 */

public class ImageRender extends ImageView {

    private float xPosition;
    private float yPostion;
    private float width;
    private float height;
    private float actualWidth;
    private float actualHeight;
    private float actualXpos;
    private float actualYpos;
    private int elementID;
    private int layer;
    private int borderWidth;
    private String borderColor; //TODO tbd for the type
    private Bitmap Image;
    private String path = "";
    private boolean visibility = true;
    private int StartSequence = 0;
    private int endSequence = 0;
    private float duration;
    private float remaingDuration;
    private String onclickinfo = "";
    private String onclickaction = "";
    private String onDoubleClickAction = "";
    private String onLongClickAction = "";
    private boolean aspectratiolock = true ;
    private float elementaspectratio;
    private float opacity = 1;
    private boolean clickable = false;



    ImageRender (float xPosition, float yPostion, float width, float height, int elementID, int layer, String path) {

        this.xPosition = xPosition;
        this.yPostion = yPostion;
        this.width = width;
        this.height = height;
        this.elementID = elementID;
        this.layer = layer;
        this.path = path;

        setDefaultAspectRatio();
    }

    public void setDefaultAspectRatio() {
        //TODO do stuff here
        elementaspectratio = 0.0f;
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    //Getters and setters

    public float getxPosition() {
        return this.xPosition;
    }

    public void setxPosition(float xPosition) {
        this.xPosition = xPosition;
    }

    public float getyPostion() {
        return this.yPostion;
    }

    public void setyPostion(float yPostion) {
        this.yPostion = yPostion;
    }

    public float getWidthFloat() { //TODO check this
        return this.width;
    }

    public void setWidth(float width) {
        this.width = width;
    }


    public float getHeightFloat() { //TODO check this
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getActualWidth() {
        return this.actualWidth;
    }

    public void setActualWidth(float actualWidth) {
        this.actualWidth = actualWidth;
    }

    public float getActualHeight() {
        return this.actualHeight;
    }

    public void setActualHeight(float actualHeight) {
        this.actualHeight = actualHeight;
    }

    public float getActualXpos() {
        return this.actualXpos;
    }

    public void setActualXpos(float actualXpos) {
        this.actualXpos = actualXpos;
    }

    public float getActualYpos() {
        return this.actualYpos;
    }

    public void setActualYpos(float actualYpos) {
        this.actualYpos = actualYpos;
    }

    public int getElementID() {
        return this.elementID;
    }

    public int getLayer() {
        return this.layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public int getBorderWidth() {
        return this.borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    public String getBorderColor() {
        return this.borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public Bitmap getImage() {
        return this.Image;
    }

    public void setImage(Bitmap image) {
        Image = image;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isVisibility() {
        return this.visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public int getStartsequence() {
        return this.StartSequence;
    }

    public void setStartsequence(int startSequence) {
        this.StartSequence = startSequence;
    }

    public int getEndSequence() {
        return this.endSequence;
    }

    public void setEndSequence(int endSequence) {
        this.endSequence = endSequence;
    }

    public float getDuration() {
        return this.duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public float getRemaingDuration() {
        return this.remaingDuration;
    }

    public void setRemaingDuration(float remaingDuration) {
        this.remaingDuration = remaingDuration;
    }

    public String getOnclickinfo() {
        return this.onclickinfo;
    }

    public void setOnclickinfo(String onclickinfo) {
        this.onclickinfo = onclickinfo;
    }

    public String getOnclickaction() {
        return this.onclickaction;
    }

    public void setOnclickaction(String onclickaction) {
        this.onclickaction = onclickaction;
    }

    public String getOnDoubleClickAction() {
        return this.onDoubleClickAction;
    }

    public void setOnDoubleClickAction(String onDoubleClickAction) {
        this.onDoubleClickAction = onDoubleClickAction;
    }

    public String getOnLongClickAction() {
        return this.onLongClickAction;
    }

    public void setOnLongClickAction(String onLongClickAction) {
        this.onLongClickAction = onLongClickAction;
    }

    public boolean isAspectratiolock() {
        return this.aspectratiolock;
    }

    public void setAspectratiolock(boolean aspectratiolock) {
        this.aspectratiolock = aspectratiolock;
    }

    public float getElementaspectratio() {
        return this.elementaspectratio;
    }

    public void setElementaspectratio(float elementaspectratio) {
        this.elementaspectratio = elementaspectratio;
    }

    public float getOpacity() {
        return this.opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public boolean isClickable() {
        return this.clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }
}