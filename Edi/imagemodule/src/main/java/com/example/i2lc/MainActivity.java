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
        Canvas testCanvas = new Canvas();
        CustomTestView testView = new CustomTestView(this);
        testView.onDraw(testCanvas);
        setContentView(testView);
       // testView.setBackgroundColor(Color.YELLOW);
    }
}
