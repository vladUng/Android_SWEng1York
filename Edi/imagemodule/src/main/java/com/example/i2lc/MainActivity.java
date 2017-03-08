package com.example.i2lc;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import android.net.Uri;

import static java.net.URI.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        URI path = URI.create("android.resource://com.example.i2lc/" + R.drawable.edi_v2);
//        String path = getResources().getResourceName(R.drawable.edi_v2);
//        Uri uri = Uri.parse("R.R.drawable.edi_v2");
//
//        int identifier = getResources().getIdentifier("edi_v2", "drawable", getPackageName());
//        InputStream ins = getResources().openRawResource(identifier);
//        Bitmap bads = BitmapFactory.decodeStream(ins);
//
//        ImageRender imageRender = new ImageRender(0.0f, 0.0f, 100f,100f, 999, 0, path);

//        Bitmap blabla = bads.copy(Bitmap.Config.ARGB_8888, true);
        Canvas testCanvas = new Canvas();

        //imageRender.loadImage();

        //imageRender.setActualHeight(0.3f);
//        imageRender.setBorderColor(Color.RED);
//        imageRender.setBorderWidth(20);
////
//        imageRender.setWidth(0.8f);
//        imageRender.setHeight(0.9f);
//        imageRender.setOpacity(1.0f);
//        imageRender.setAspectratiolock(true);
//
//        imageRender.setxPosition(0.1f);
//        imageRender.setyPosition(0.1f);

        CustomTestView testView = new CustomTestView(this);
        testView.onDraw(testCanvas);

        setContentView(testView);

       // testView.setBackgroundColor(Color.YELLOW);
    }
}
