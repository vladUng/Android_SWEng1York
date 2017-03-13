package com.example.i2lc;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.io.File;


/**
 * Created by vlad on 04/03/2017.
 */

public class ImageRenderer {

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
    private int borderColor = Color.GREEN;
    private Bitmap image = Bitmap.createBitmap(500,500,Bitmap.Config.ARGB_8888);
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

    private static final String TAG = "ImageRenderer";

    public ImageRenderer() {
    }

    public ImageRenderer(float xPosition, float yPosition, float width, float height, int elementID, int layer, String path) {
        setxPosition(xPosition);
        setyPosition(yPosition);
        setWidth(width);
        setHeight(height);
        setLayer(layer);
        setPath(path);
        if (elementID >= 0){
            this.elementID = elementID;
        } else {
            Log.e(TAG, "elemendID must be >= 0");
            this.elementID = 0;
        }
    }

    //Checks if file at specified path is a valid image file
    private boolean isValidImageFile (String path) {
        File file = new File(path);
        String[] imageExtensions = new String[] {"jpg", "png", "gif", "jpeg"};
        boolean isValidImageFile = false;
        for (String extension : imageExtensions) {
            if (path.endsWith(extension) && file.exists()) {
                isValidImageFile = true;
            }
        }
        return isValidImageFile;
    }

    // Scales bitmap to desired width and height
    private void scaleBitmap() {
        float imageWidth = image.getWidth();
        float imageHeight = image.getHeight();
        float widthRatio = actualWidth/imageWidth;
        float heightRatio = actualHeight/imageHeight;

        if(aspectRatioLock) {
            if (imageWidth > actualWidth) {
                imageWidth *= widthRatio;
                imageHeight *= widthRatio;
                heightRatio = actualHeight/imageHeight;
            }
            if (imageHeight > actualHeight) {
                imageHeight *=  heightRatio;
                imageWidth *=  heightRatio;
            }
        } else {
            imageWidth = actualWidth;
            imageHeight = actualHeight;
        }
        image = Bitmap.createScaledBitmap(image, (int)imageWidth, (int)imageHeight, true);
    }

    public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        scaleBitmap(); //scale the current image bitmap
        paint.setAlpha((int) (opacity * 255)); //set opacity
        drawBorder(canvas);
        if (isValidImageFile(path)) {
            canvas.drawBitmap(image, actualXpos ,actualYpos , paint);
        } else {
            paint.setColor(Color.RED);
            canvas.drawText("Wrong path/Not a valid image file", actualXpos+2*borderWidth,
                           actualYpos+2*borderWidth + paint.getTextSize(), paint);
        }
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
        Rect imageRect = new Rect((int)actualXpos, (int)actualYpos,(int)(actualXpos+actualWidth), (int)(actualYpos+actualHeight));
        return rect.contains(imageRect);
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
        if(isValidImageFile(path)) {
            image = BitmapFactory.decodeFile(path);
        } else {
            Log.e(TAG , "Could not load image file!");
        }
    }

    public void discardImage(){
        image = null;
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
                xPos + image.getWidth() - borderWidth / 2, yPos + image.getHeight() - borderWidth /  2);
        canvas.drawRect(rect, paint);
    }

    //Getters and setters
    public float getxPosition() {
        return this.xPosition;
    }

    public void setxPosition(float xPosition) {
        if(xPosition >= 0) {
            this.xPosition = xPosition;
        } else {
            Log.e(TAG, "xPosition must be >= 0%");
            this.xPosition = 0;
        }
    }

    public float getyPosition() {
        return this.yPosition;
    }

    public void setyPosition(float yPosition) {
        if(yPosition >= 0) {
            this.yPosition = yPosition;
        } else {
            Log.e(TAG, "yPosition must be >= 0%");
            this.yPosition = 0;
        }
    }

    public float getWidth() {
        return this.width;
    }

    public void setWidth(float width) {
        if(width > 0.0f) {
            this.width = width;
        } else{
            Log.e(TAG,"width must be > 0%" );
            this.width = 0.1f;
        }
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        if(height > 0) {
            this.height = height;
        } else{
            Log.e(TAG,"height must be > 0%");
            this.height = 0.1f;
        }
    }

    public float getActualWidth() {
        return this.actualWidth;
    }

    public void setActualWidth(float actualWidth) {
        if(actualWidth > 0) {
            this.actualWidth = actualWidth;
        } else {
            Log.e(TAG,"actualWidth must be > 0!");
            this.actualWidth = 1;
        }
    }

    public float getActualHeight() {
        return this.actualHeight;
    }

    public void setActualHeight(float actualHeight) {
        if(actualHeight > 0) {
            this.actualHeight = actualHeight;
        } else{
            Log.e(TAG,"actualHeight must be > 0");
            this.actualHeight = 1;
        }
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
        if(layer >= 0) {
            this.layer = layer;
        } else {
            Log.e(TAG, "layer must be >= 0");
            this.layer = 0;
        }
    }

    public int getBorderWidth() {
        return this.borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        if(borderWidth >= 0) {
            this.borderWidth = borderWidth;
        } else {
            Log.e(TAG,"borderWidth must be >= 0");
            this.borderWidth = 0;
        }
    }

    public int getBorderColor() {
        return this.borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public Bitmap getImage() {
        return this.image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        if(isValidImageFile(path)) {
            this.path = path;
        } else{
            Log.e(TAG,"File at path: " + path + " is not a valid image file!");
        }
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
        if(startSequence >= 0) {
            this.startSequence = startSequence;
        } else {
            System.out.print("startSequence cannot be smaller than zero!");
            this.startSequence = 0;
        }
    }

    public int getEndSequence() {
        return this.endSequence;
    }

    public void setEndSequence(int endSequence) {
        if(endSequence >= 0 && endSequence >= startSequence) {
            this.endSequence = endSequence;
        } else {
            System.out.println("endSequence cannot be smaller than zero or smaller than startSequence!");
            this.endSequence = startSequence;
        }

    }

    public float getDuration() {
        return this.duration;
    }

    public void setDuration(float duration) {
        if(duration >= 0) {
            this.duration = duration;
        } else {
            System.out.println("duration must be >= 0!");
            this.duration = 0;
        }
    }

    public float getRemainingDuration() {
        return this.remainingDuration;
    }

    public void setRemainingDuration(float remainingDuration) {
        if (remainingDuration >= 0) {
            this.remainingDuration = remainingDuration;
        } else{
            System.out.println("remainingDuration must be >= 0!");
            this.remainingDuration = 0;
        }
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
        if (elementAspectRatio >= 0) {
            this.elementAspectRatio = elementAspectRatio;
        } else {
            System.out.println("elementAspectRatio cannot be < 0! ");
            this.elementAspectRatio = 0;
        }
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