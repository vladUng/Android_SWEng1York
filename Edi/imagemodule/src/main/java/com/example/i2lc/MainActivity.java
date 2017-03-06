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
//        imageRender.setBorderColor(Color.RED);
//        imageRender.setBorderWidth(20);
//
        imageRender.setWidth(100);
        imageRender.setHeight(100);
        imageRender.setOpacity(1.0f);


//        imageRender.setxPosition(0.0f);
//        imageRender.setyPosition(0.0f);
//
        imageRender.onDraw(testCanvas);

        setContentView(imageRender);
    }
}
