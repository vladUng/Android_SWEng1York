package com.example.i2lc;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;


/**
 * Created by vlad on 04/03/2017.
 */

public class ImageRender {

    private float xPosition;
    private float yPosition;
    private float width;
    private float height;
    private float actualWidth = 0.0f;
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

    ImageRender(float xPosition, float yPosition, float width, float height, int elementID, int layer, String path ) {

        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
        this.elementID = elementID;
        this.layer = layer;
        this.path = path;
    }

    // Scales bitmap to desired width and height
    private void scaleBitmap(float maxWidth, float maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = Image.getWidth();
            int height = Image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = maxWidth / maxHeight;
            float finalWidth = maxWidth;
            float finalHeight = maxHeight;

            if(aspectratiolock) {
                if (ratioMax > 1) {
                    finalWidth = (int) ( maxHeight * ratioBitmap);
                } else {
                    finalHeight = (int) ( maxWidth / ratioBitmap);
                }
            }
           Image = Bitmap.createScaledBitmap(Image, (int)finalWidth, (int)finalHeight, true);
        }
    }

    protected void onDraw(Canvas canvas, Bitmap bitmap) {

        Image = bitmap.copy(Bitmap.Config.ARGB_8888, true); //needed this to transform it in a mutable bitmap, shouldn't needeed in the final version

//      loadImage(); TODO: uncomment on the release version

        float newWidth = Image.getWidth() * width;
        float newHeight =  Image.getHeight() * height;

        scaleBitmap(newWidth, newHeight); //scale the current image bitmap

        //set opacity
        Paint alphaPaint = new Paint();
        alphaPaint.setAlpha((int) (opacity * 255));


        float newPosY = Image.getHeight() *  yPosition;
        float newPosX = Image.getWidth()  *  xPosition;

        if (borderWidth > 0 ) {
            drawBorder(canvas, (int)newPosX, (int)newPosY);
        }

        canvas.drawBitmap(this.Image, newPosX, newPosY, alphaPaint);
    }

    public boolean intersects(int x, int y) {
        boolean withinBounds = false;
        if ((float) x <= actualWidth && (float) y <= actualHeight) {
            withinBounds = true;
        }
        return withinBounds;
    }

    public Boolean liesWithin(int xPos, int yPos, int widthPixels, int heightPixels) {

        int xPositionPixel = (int) (Image.getWidth() * xPosition);
        int yPositionPixel = (int) (Image.getHeight() * yPosition);

        Rect rect = new Rect(xPositionPixel, yPositionPixel, xPositionPixel + widthPixels, yPositionPixel + heightPixels);

        return rect.contains(xPos, yPos, widthPixels, heightPixels);
    }

    public String onClick () {
        if(onclickaction.length() > 0) {
            return onclickaction;
        } else
        {
            return onclickinfo;
        }
    }

    public void loadImage(){
        //      Image = BitmapFactory.decodeFile(path); TODO: uncomment on the release version
    }

    public void discardImage(){
        Image = null;
        //or Image.recycle();
    }


    private void drawBorder(Canvas canvas, int xPosition, int yPosition) {

        Paint paint = new Paint();
        paint.setColor(borderColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        paint.setAlpha((int) (opacity * 255));


        Rect rect = new Rect(xPosition + borderWidth / 2, yPosition + borderWidth / 2,
                xPosition + Image.getWidth() - borderWidth / 2, yPosition + Image.getHeight() - borderWidth /  2);

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

    public boolean getClickable() {
        return this.clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }
}