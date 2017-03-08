package com.example.i2lc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

import java.io.InputStream;

/**
 * Created by vlad on 08/03/2017.
 */

public class CustomTestView extends View {

    int identifier;

    public CustomTestView(Context context) {
        super(context);
        identifier = getResources().getIdentifier("edi_v2", "drawable", context.getPackageName());
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        ImageRender customImg = new ImageRender(0.0f, 0.0f, 0.2f, 0.2f, 999, 0, "adsas");

        InputStream ins = getResources().openRawResource(identifier);
        Bitmap bads = BitmapFactory.decodeStream(ins);

        customImg.setAspectratiolock(true);
        customImg.setBorderWidth(20);
        customImg.setBorderColor(Color.GRAY);

        customImg.setOpacity(1.0f);

        customImg.onDraw(canvas, bads);

        Boolean bla = customImg.liesWithin(0, 0, 700, 800);
        Boolean bla2 = customImg.intersects(0,4);


        customImg.discardImage();

        boolean asda = true;
    }
}
