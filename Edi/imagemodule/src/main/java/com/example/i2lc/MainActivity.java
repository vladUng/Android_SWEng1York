package com.example.i2lc;

import android.graphics.Bitmap;
import android.graphics.Canvas;
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

//        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas testCanvas = new Canvas();

//        imageRender.draw(testCanvas);

        setContentView(imageRender);

//        LinearLayout layout = (LinearLayout)findViewById(R.id.test_layout);
//
//        layout.addView(imageRender);
    }
}
