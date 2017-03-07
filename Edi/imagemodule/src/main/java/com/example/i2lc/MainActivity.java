package com.example.i2lc;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageRender imageRender = new ImageRender(this);

        Canvas testCanvas = new Canvas();

        //imageRender.setActualHeight(0.3f);
        imageRender.setBorderColor(Color.RED);
        imageRender.setBorderWidth(20);
//
        imageRender.setWidth(0.8f);
        imageRender.setHeight(0.9f);
        imageRender.setOpacity(1.0f);
        imageRender.setAspectratiolock(true);

        imageRender.setxPosition(0.1f);
        imageRender.setyPosition(0.1f);

        imageRender.onDraw(testCanvas);

        setContentView(imageRender);
    }
}
