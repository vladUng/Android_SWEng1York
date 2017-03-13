package com.example.i2lc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

/**
 * Created by vlad on 08/03/2017.
 */

public class CustomTestView extends View {

    String path = "/storage/emulated/0/DCIM/Camera/edi_v2.png";

    public CustomTestView(Context context) {
        super(context);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ImageRenderer customImg = new ImageRenderer(0.0f, 0.0f, 0.5f, 0.5f, 999, 0, path);
        customImg.loadImage();
        customImg.setActualXpos(50.0f);
        customImg.setActualYpos(50.0f);
        customImg.setActualWidth(500);
        customImg.setActualHeight(900);
        customImg.setAspectRatioLock(true);
        customImg.setBorderWidth(10);
        customImg.setBorderColor(Color.BLACK);
        customImg.setOpacity(1f);
        customImg.onDraw(canvas);
        customImg.discardImage();
    }
}
