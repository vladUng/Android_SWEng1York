package com.example.i2lc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.widget.ImageView;
import java.io.File;

/**
 * Created by vlad on 04/03/2017.
 */

public class ImageRender extends ImageView {

    private float xPosition;
    private float yPosition;
    private float width;
    private float height;
    private float actualWidth;
    private float actualHeight;
    private float actualXpos;
    private float actualYpos;
    private int elementID;
    private int layer;
    private int borderWidth;
    private int borderColor; //TODO tbd for the type, it should be int, they want the setter in string ....
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
    private float opacity = 1.0f;
    private boolean clickable = false;

    public ImageRender(Context context) {
        super(context);

        this.xPosition = 0.0f;
        this.yPosition = 0.0f;
        this.width = 1.0f;
        this.height = 1.0f;
        this.elementID = 1;
        this.layer = 0;
        this.path = "edi_v2";


        this.Image = BitmapFactory.decodeResource(getResources(), R.drawable.edi_v2);

        //TODO check if they want to keep this
        setBorderColor(Color.GREEN);
    }

    public void setDefaultAspectRatio() {
        //TODO do stuff here
        elementaspectratio = 0.0f;
    }


    // Scales bitmap to desired width and height
    public Bitmap scaleBitmap(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;
            int finalWidth = maxWidth;
            int finalHeight = maxHeight;

            if(aspectratiolock == true) {
                if (ratioMax > 1) {
                    finalWidth = (int) ((float) maxHeight * ratioBitmap);
                } else {
                    finalHeight = (int) ((float) maxWidth / ratioBitmap);
                }
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
        }
        return image;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);

        this.Image = scaleBitmap(this.Image, this.getWidth(), this.getHeight());

        //set opacity
        setAlpha(this.opacity);

        if (getBorderWidth() >= 0 ) {
            drawBorder(canvas);
        }

        float newPosY = this.getHeight() * yPosition;
        float newPosX = this.getWidth() *  xPosition;

        canvas.drawBitmap(this.Image, newPosX, newPosY, null);
}



    private void drawBorder(Canvas canvas) {

        Paint paint = new Paint();
        paint.setColor(borderColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);

        Rect rect = new Rect(borderWidth / 2, borderWidth / 2, Image.getWidth() - borderWidth/2, Image.getHeight() - borderWidth /  2);
        canvas.drawRect(rect, paint);
    }

    //Getters and setters

    public float getxPosition() {
        return this.xPosition;
    }

    public void setxPosition(float xPosition) {
        this.xPosition = xPosition;
    }

    public float getyPosition() {
        return this.yPosition;
    }

    public void setyPosition(float yPosition) {
        this.yPosition = yPosition;
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

    public int getBorderColor() {
        return this.borderColor;
    }

    public void setBorderColor(int borderColor) {
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