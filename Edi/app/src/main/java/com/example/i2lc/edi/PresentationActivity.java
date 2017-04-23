package com.example.i2lc.edi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

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
        final Animation animTranslate = AnimationUtils.loadAnimation(this,R.anim.anim_translate);
        askButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animTranslate);
                dispQuestionTextBox(v);
            }
        });
    }

    public void dispQuestionTextBox(View view){
        if(isTextBoxVisible == true){
            editText.setVisibility(View.VISIBLE);
            isTextBoxVisible = false;
            askButton.setText("Send");
        } else{
            editText.setVisibility(View.INVISIBLE);
            isTextBoxVisible = true;
            askButton.setText("Ask");
        }
    }
}
