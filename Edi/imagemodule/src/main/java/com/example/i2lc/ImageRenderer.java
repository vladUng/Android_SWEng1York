package com.example.i2lc;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;


/**
 * Created by vlad on 04/03/2017.
 */

public class ImageRenderer {

    private float xPosition;
    private float yPosition;
    private float width;
    private float height;
    private float actualWidth = 0.0f; //TODO Actual Width/height/pos in ints (cuz pixels...)
    private float actualHeight = 0.0f;
    private float actualXpos;
    private float actualYpos;
    private int elementID;
    private int layer;
    private int borderWidth;
    private int borderColor = Color.GREEN; //TODO tbd for the type, it should be int, they want the setter in string ....
    private Bitmap Image;
    private String path = "";
    private boolean visibility = true;
    private int startSequence = 0;
    private int endSequence = 0;
    private float duration;
    private float remainingDuration;
    private String onClickInfo = "";
    private String onClickAction = "";
    private String onDoubleClickAction = "";
    private String onLongClickAction = "";
    private boolean aspectRatioLock = true ;
    private float elementAspectRatio;
    private float opacity = 1.0f;
    private boolean clickable = false;

    public ImageRenderer() {
    }

    public ImageRenderer(float xPosition, float yPosition, float width, float height, int elementID, int layer, String path) {

        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
        this.elementID = elementID;
        this.layer = layer;
        this.path = path;
    }

    // Scales bitmap to desired width and height
    private void scaleBitmap() {
            int width = Image.getWidth();
            int height = Image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = actualWidth / actualHeight;
            float finalWidth = actualWidth;
            float finalHeight = actualHeight;

            if(aspectRatioLock) {
                if (ratioMax > 1) {
                    finalWidth = (int) ( actualHeight * ratioBitmap);
                } else {
                    finalHeight = (int) ( actualWidth / ratioBitmap);
                }
            }
           Image = Bitmap.createScaledBitmap(Image, (int)finalWidth, (int)finalHeight, true);
    }

    public void onDraw(Canvas canvas) {

        scaleBitmap(); //scale the current image bitmap
        //set opacity
        Paint alphaPaint = new Paint();
        alphaPaint.setAlpha((int) (opacity * 255));
        drawBorder(canvas);
        canvas.drawBitmap(Image, actualXpos, actualYpos, alphaPaint);
    }

    public boolean intersects(int x, int y) {
        boolean withinBounds = false;
        if ((float) x <= actualWidth && (float) y <= actualHeight) {
            withinBounds = true;
        }
        return withinBounds;
    }

    public Boolean liesWithin(int xPos, int yPos, int widthPixels, int heightPixels) {

        Rect rect = new Rect(xPos, yPos, xPos + widthPixels, yPos + heightPixels);

        return rect.contains((int)actualXpos, (int) actualYpos, (int) (actualXpos + actualWidth), (int) (actualYpos + actualHeight));
    }

    public String onClick () {
        if(onClickAction.length() > 0) {
            return onClickAction;
        } else
        {
            return onClickInfo;
        }
    }

    public void loadImage(){
        Image = BitmapFactory.decodeFile(path);
    }

    public void discardImage(){
        Image = null;
        //or Image.recycle();
    }


    private void drawBorder(Canvas canvas) {
        int xPos = (int) actualXpos;
        int yPos = (int) actualYpos;

        Paint paint = new Paint();
        paint.setColor(borderColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        paint.setAlpha((int) (opacity * 255));


        Rect rect = new Rect(xPos + borderWidth / 2, yPos + borderWidth / 2,
                xPos + Image.getWidth() - borderWidth / 2, yPos + Image.getHeight() - borderWidth /  2);

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

    public float getWidth() { //TODO check this
        return this.width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() { //TODO check this
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
        return this.startSequence;
    }

    public void setStartsequence(int startSequence) {
        this.startSequence = startSequence;
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

    public float getRemainingDuration() {
        return this.remainingDuration;
    }

    public void setRemainingDuration(float remainingDuration) {
        this.remainingDuration = remainingDuration;
    }

    public String getOnClickInfo() {
        return this.onClickInfo;
    }

    public void setOnClickInfo(String onClickInfo) {
        this.onClickInfo = onClickInfo;
    }

    public String getOnClickAction() {
        return this.onClickAction;
    }

    public void setOnClickAction(String onClickAction) {
        this.onClickAction = onClickAction;
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

    public boolean isAspectRatioLock() {
        return this.aspectRatioLock;
    }

    public void setAspectRatioLock(boolean aspectRatioLock) {
        this.aspectRatioLock = aspectRatioLock;
    }

    public float getElementAspectRatio() {
        return this.elementAspectRatio;
    }

    public void setElementAspectRatio(float elementAspectRatio) {
        this.elementAspectRatio = elementAspectRatio;
    }

    public float getOpacity() {
        return this.opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public boolean getClickable() {
        return this.clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }
}