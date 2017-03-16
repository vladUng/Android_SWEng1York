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
 * This class creates an image object, which stores information
 * about the image and has the ability to draw the image onto a canvas.
 */
public class ImageRenderer extends SlideElement{
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

    /**
     * Creates new ImageRenderer
     */
    public ImageRenderer() {
    }

    /**
     * Creates new ImageRenderer, whose parameters are:
     * @param xPosition the x position of the element in terms of percentage of the slide width
     * @param yPosition the y position of the element in terms of percentage of the slide width
     * @param width     the width of the element as percentage of the slide width
     * @param height    the height of the element as percentage of the slide width
     * @param elementID identification number of the element within the slide
     * @param layer     draw order of the image
     * @param path      path to image location
     */
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

    /**
     * Checks if file at specified path is a valid image file.
     * @param path  path to image file
     * @return      true if file at path is a valid image file
     */
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

    /**
     * Scales bitmap to desired width and height.
     * Takes into account if aspect ratio is locked.
     */
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

    /**
     * Draws image on the canvas
     * within the bounds defined by actualWidth and actualHeight.
     * Draws border of specified width nd color around the image.
     * Displays error image if path is wrong or file at path is
     * not a valid image file
     * @param canvas canvas passed from SlideShow on which image is drawn
     */
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

    /**
     * Checks if coordinates x, y lie within the bounds
     * defined by actualWidth and actualHeight
     * @param x x coordinate
     * @param y y coordinate
     * @return  true if coorinates lie within the bounds
     */
    public boolean intersects(int x, int y) {
        boolean withinBounds = false;
        if ((float) x <= actualWidth && (float) y <= actualHeight) {
            withinBounds = true;
        }
        return withinBounds;
    }

    /**
     * Returns true if any part of this element lies within
     * the box described by the parameters
     * @param xPos         x coordinate
     * @param yPos         y coordinate
     * @param widthPixels  width in pixels
     * @param heightPixels height in pixels
     * @return true if element intersects box described by parameters
     */
    public boolean liesWithin(int xPos, int yPos, int widthPixels, int heightPixels) {
        Rect rect = new Rect(xPos, yPos, xPos + widthPixels, yPos + heightPixels);
        Rect imageRect = new Rect((int)actualXpos, (int)actualYpos,(int)(actualXpos+actualWidth), (int)(actualYpos+actualHeight));
        return rect.intersect(imageRect);
    }

    /**
     * Returns String onClickAction if its length is greater than 0.
     * @return String onClickInfo otherwise
     */
    public String onClick () {
        if(onClickAction.length() > 0) {
            return onClickAction;
        } else
        {
            return onClickInfo;
        }
    }

    /**
     * Loads image from the specific path in device memory
     */
    public void loadImage(){
        if(isValidImageFile(path)) {
            image = BitmapFactory.decodeFile(path);
        } else {
            Log.e(TAG , "Could not load image file!");
        }
    }

    /**
     * Sets the image field to null
     */
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


    /**
     * Gets position.
     *
     * @return the position
     */
//Getters and setters
    public float getxPosition() {
        return this.xPosition;
    }

    /**
     * Sets position.
     *
     * @param xPosition the x position
     */
    public void setxPosition(float xPosition) {
        if(xPosition >= 0) {
            this.xPosition = xPosition;
        } else {
            Log.e(TAG, "xPosition must be >= 0%");
            this.xPosition = 0;
        }
    }

    /**
     * Gets position.
     *
     * @return the position
     */
    public float getyPosition() {
        return this.yPosition;
    }

    /**
     * Sets position.
     *
     * @param yPosition the y position
     */
    public void setyPosition(float yPosition) {
        if(yPosition >= 0) {
            this.yPosition = yPosition;
        } else {
            Log.e(TAG, "yPosition must be >= 0%");
            this.yPosition = 0;
        }
    }

    /**
     * Gets width.
     *
     * @return the width
     */
    public float getWidth() {
        return this.width;
    }

    /**
     * Sets width.
     *
     * @param width the width
     */
    public void setWidth(float width) {
        if(width > 0.0f) {
            this.width = width;
        } else{
            Log.e(TAG,"width must be > 0%" );
            this.width = 0.1f;
        }
    }

    /**
     * Gets height.
     *
     * @return the height
     */
    public float getHeight() {
        return this.height;
    }

    /**
     * Sets height.
     *
     * @param height the height
     */
    public void setHeight(float height) {
        if(height > 0) {
            this.height = height;
        } else{
            Log.e(TAG,"height must be > 0%");
            this.height = 0.1f;
        }
    }

    /**
     * Gets actual width.
     *
     * @return the actual width
     */
    public float getActualWidth() {
        return this.actualWidth;
    }

    /**
     * Sets actual width.
     *
     * @param actualWidth the actual width
     */
    public void setActualWidth(float actualWidth) {
        if(actualWidth > 0) {
            this.actualWidth = actualWidth;
        } else {
            Log.e(TAG,"actualWidth must be > 0!");
            this.actualWidth = 1;
        }
    }

    /**
     * Gets actual height.
     *
     * @return the actual height
     */
    public float getActualHeight() {
        return this.actualHeight;
    }

    /**
     * Sets actual height.
     *
     * @param actualHeight the actual height
     */
    public void setActualHeight(float actualHeight) {
        if(actualHeight > 0) {
            this.actualHeight = actualHeight;
        } else{
            Log.e(TAG,"actualHeight must be > 0");
            this.actualHeight = 1;
        }
    }

    /**
     * Gets actual xpos.
     *
     * @return the actual xpos
     */
    public float getActualXpos() {
        return this.actualXpos;
    }

    /**
     * Sets actual xpos.
     *
     * @param actualXpos the actual xpos
     */
    public void setActualXpos(float actualXpos) {
        this.actualXpos = actualXpos;
    }

    /**
     * Gets actual ypos.
     *
     * @return the actual ypos
     */
    public float getActualYpos() {
        return this.actualYpos;
    }

    /**
     * Sets actual ypos.
     *
     * @param actualYpos the actual ypos
     */
    public void setActualYpos(float actualYpos) {
        this.actualYpos = actualYpos;
    }

    /**
     * Gets element id.
     *
     * @return the element id
     */
    public int getElementID() {
        return this.elementID;
    }

    /**
     * Gets layer.
     *
     * @return the layer
     */
    public int getLayer() {
        return this.layer;
    }

    /**
     * Sets layer.
     *
     * @param layer the layer
     */
    public void setLayer(int layer) {
        if(layer >= 0) {
            this.layer = layer;
        } else {
            Log.e(TAG, "layer must be >= 0");
            this.layer = 0;
        }
    }

    /**
     * Gets border width.
     *
     * @return the border width
     */
    public int getBorderWidth() {
        return this.borderWidth;
    }

    /**
     * Sets border width.
     *
     * @param borderWidth the border width
     */
    public void setBorderWidth(int borderWidth) {
        if(borderWidth >= 0) {
            this.borderWidth = borderWidth;
        } else {
            Log.e(TAG,"borderWidth must be >= 0");
            this.borderWidth = 0;
        }
    }

    /**
     * Gets border color.
     *
     * @return the border color
     */
    public int getBorderColor() {
        return this.borderColor;
    }

    /**
     * Sets border color.
     *
     * @param borderColor the border color
     */
    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    /**
     * Gets image.
     *
     * @return the image
     */
    public Bitmap getImage() {
        return this.image;
    }

    /**
     * Sets image.
     *
     * @param image the image
     */
    public void setImage(Bitmap image) {
        this.image = image;
    }

    /**
     * Gets path.
     *
     * @return the path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Sets path.
     *
     * @param path the path
     */
    public void setPath(String path) {
        if(isValidImageFile(path)) {
            this.path = path;
        } else{
            Log.e(TAG,"File at path: " + path + " is not a valid image file!");
        }
    }

    /**
     * Is visibility boolean.
     *
     * @return the boolean
     */
    public boolean isVisibility() {
        return this.visibility;
    }

    /**
     * Sets visibility.
     *
     * @param visibility the visibility
     */
    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public int getStartSequence() {
        return this.startSequence;
    }

    public void setStartSequence(int startSequence) {
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

    /**
     * Gets duration.
     *
     * @return the duration
     */
    public float getDuration() {
        return this.duration;
    }

    /**
     * Sets duration.
     *
     * @param duration the duration
     */
    public void setDuration(float duration) {
        if(duration >= 0) {
            this.duration = duration;
        } else {
            System.out.println("duration must be >= 0!");
            this.duration = 0;
        }
    }

    /**
     * Gets remaining duration.
     *
     * @return the remaining duration
     */
    public float getRemainingDuration() {
        return this.remainingDuration;
    }

    /**
     * Sets remaining duration.
     *
     * @param remainingDuration the remaining duration
     */
    public void setRemainingDuration(float remainingDuration) {
        if (remainingDuration >= 0) {
            this.remainingDuration = remainingDuration;
        } else{
            System.out.println("remainingDuration must be >= 0!");
            this.remainingDuration = 0;
        }
    }

    /**
     * Gets on click info.
     *
     * @return the on click info
     */
    public String getOnClickInfo() {
        return this.onClickInfo;
    }

    /**
     * Sets on click info.
     *
     * @param onClickInfo the on click info
     */
    public void setOnClickInfo(String onClickInfo) {
        this.onClickInfo = onClickInfo;
    }

    /**
     * Gets on click action.
     *
     * @return the on click action
     */
    public String getOnClickAction() {
        return this.onClickAction;
    }

    /**
     * Sets on click action.
     *
     * @param onClickAction the on click action
     */
    public void setOnClickAction(String onClickAction) {
        this.onClickAction = onClickAction;
    }

    /**
     * Gets on double click action.
     *
     * @return the on double click action
     * NOTE: It doesn't respect the naming convention as it was defined as an abstract in the
     *     SlideElement class
     */
    public String onDoubleClickAction() {
        return this.onDoubleClickAction;
    }

    /**
     * Sets on double click action.
     *
     * @param onDoubleClickAction the on double click action
     */
    public void setOnDoubleClickAction(String onDoubleClickAction) {
        this.onDoubleClickAction = onDoubleClickAction;
    }

    /**
     * Gets on long click action.
     *
     * @return the on long click action
     * NOTE: It doesn't respect the naming convention as it was defined as an abstract in the
     *     SlideElement class
     */
    public String onLongClickAction() {
        return this.onLongClickAction;
    }

    /**
     * Sets on long click action.
     *
     * @param onLongClickAction the on long click action
     */
    public void setOnLongClickAction(String onLongClickAction) {
        this.onLongClickAction = onLongClickAction;
    }

    /**
     * Is aspect ratio lock boolean.
     *
     * @return the boolean
     */
    public boolean isAspectRatioLock() {
        return this.aspectRatioLock;
    }

    /**
     * Sets aspect ratio lock.
     *
     * @param aspectRatioLock the aspect ratio lock
     */
    public void setAspectRatioLock(boolean aspectRatioLock) {
        this.aspectRatioLock = aspectRatioLock;
    }

    /**
     * Gets element aspect ratio.
     *
     * @return the element aspect ratio
     */
    public float getElementAspectRatio() {
        return this.elementAspectRatio;
    }

    /**
     * Sets element aspect ratio.
     *
     * @param elementAspectRatio the element aspect ratio
     */
    public void setElementAspectRatio(float elementAspectRatio) {
        if (elementAspectRatio >= 0) {
            this.elementAspectRatio = elementAspectRatio;
        } else {
            System.out.println("elementAspectRatio cannot be < 0! ");
            this.elementAspectRatio = 0;
        }
    }

    /**
     * Gets opacity.
     *
     * @return the opacity
     */
    public float getOpacity() {
        return this.opacity;
    }

    /**
     * Sets opacity.
     *
     * @param opacity the opacity
     */
    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    /**
     * Gets clickable.
     *
     * @return the clickable
     *
     *
     */
    public  boolean isClickable() {
        return this.clickable;
    }

    /**
     * Sets clickable.
     *
     * @param clickable the clickable
     *
     * NOTE: It doesn't respect the naming convention as it was defined as an abstract in the
     *     SlideElement class
     */
    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }
}