package com.example.i2lc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

/**
 * Created by vlad on 08/03/2017.
 */

public class CustomTestView extends View {

    String path = "/storage/emulated/0/DCIM/Camera/IMG_20170309_163351";

    public CustomTestView(Context context) {
        super(context);
        //identifier = getResources().getIdentifier("edi_v2", "drawable", context.getPackageName());
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ImageRenderer customImg = new ImageRenderer(0.0f, 0.0f, 0.5f, 0.5f, 999, 0, path);
        customImg.loadImage();
        customImg.setActualXpos(0.0f);
        customImg.setActualYpos(0.0f);
        customImg.setActualWidth(1000);
        customImg.setActualHeight(500);
        customImg.setAspectRatioLock(true);
        customImg.setBorderWidth(20);
        customImg.setBorderColor(Color.GREEN);
        customImg.setOpacity(1.0f);
        customImg.onDraw(canvas);
        customImg.discardImage();
    }
}
