package com.example.i2lc.edi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.i2lc.edi.model.PresentationMod;

public class PresentationActivity extends AppCompatActivity {
    private PresentationMod presentation;
    private ProgressBar progressBar;
    private int progress;
    private Button askButton;
    private EditText editText;
    boolean isTextBoxVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);
        //Calculate progress and set in Progress Bar
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        presentation = new PresentationMod();
        presentation.setCurrentSlideNum(36);
        presentation.setTotalSlideNum(50);
        progress = (presentation.getCurrentSlideNum()*100)/presentation.getTotalSlideNum();
        progressBar.setProgress(progress);
        //AskButton
        askButton = (Button)findViewById(R.id.askButton);
        Intent intent = getIntent();
        //Show Edit Text to type question
        editText = (EditText)findViewById(R.id.questionText);
        final Animation animTranslate_r = AnimationUtils.loadAnimation(this,R.anim.anim_translate_r);
        final Animation animTranslate_l = AnimationUtils.loadAnimation(this,R.anim.anim_translate_l);

        animTranslate_l.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
                //askButton.setVisibility(askButton.getRootView().INVISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                askButton.setX(askButton.getX()-450);
                askButton.setText("Ask");
                //askButton.setVisibility(askButton.getRootView().VISIBLE);
            }
        });

        animTranslate_r.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
                //askButton.setVisibility(askButton.getRootView().INVISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                askButton.setX(askButton.getX()+450);
                askButton.setText("Send");
               // askButton.setVisibility(askButton.getRootView().VISIBLE);
            }
        });

        askButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //v.startAnimation(animTranslate);
                dispQuestionTextBox(v,animTranslate_l,animTranslate_r);
            }
        });
    }

    public void dispQuestionTextBox(View view,Animation a1, Animation a2){
        if(isTextBoxVisible == true){
            view.startAnimation(a2);
            editText.setVisibility(View.VISIBLE);
            isTextBoxVisible = false;
            //askButton.setX(askButton.getX()+400);
        } else{
            view.startAnimation(a1);
            editText.setVisibility(View.INVISIBLE);
            isTextBoxVisible = true;
        }
    }
}
