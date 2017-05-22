package com.example.i2lc.edi.model;

import android.content.Context;

import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.example.i2lc.edi.R;
import com.example.i2lc.edi.dbClasses.Presentation;

/**
 * Created by Cosmin Frateanu on 22/05/2017.
 */

public class PresentationDisplay extends View {
    private Presentation presentation;
    private LinearLayout horizontalLayout;
    private LinearLayout infoLayout;
    private TextView[] infoText = new TextView[4];
    private Button joinButton;
    private ImageView presentationImage;

//This may or may not be used to construct the presentation Display list dynamically
    public PresentationDisplay(Context c) {
        super(c);
        horizontalLayout = new LinearLayout(c);
        infoLayout = new LinearLayout(c);
        presentationImage = new ImageView(c);
        for(int i = 0; i<4;i++){
            infoText[i] = new TextView(c);
        }
        constructDisplay();
    }

    private void constructDisplay(){
        //Construct parent layout which holds presentation textviews and button on the left
        //and the presentation thumbnail on the right
        horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams hLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        horizontalLayout.setWeightSum(2f);
        horizontalLayout.setLayoutParams(hLayoutParams);

        infoLayout.setOrientation(LinearLayout.VERTICAL);
        LayoutParams infoLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1f);
        infoLayout.setLayoutParams(infoLayoutParams);

        presentationImage.setImageResource(R.drawable.edi);
        LayoutParams imageParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f);
        presentationImage.setLayoutParams(imageParams);

        horizontalLayout.addView(infoLayout);
        horizontalLayout.addView(presentationImage);
    }
}
