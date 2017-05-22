package com.example.i2lc.edi;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.i2lc.edi.dbClasses.Presentation;
import com.example.i2lc.edi.model.PresentationMod;
import com.example.i2lc.edi.presFragments.InteractionFragment;
import com.example.i2lc.edi.presFragments.MainPresentationFragment;

import java.util.ArrayList;

public class InitialPresentationActivity extends AppCompatActivity{
    private Fragment fragment;
    private PresentationMod presentation;
    private ProgressBar progressBar;
    private int progress;
    private Button askButton;
    private EditText editText;
    boolean isTextBoxVisible = false;
    boolean buttonPressed = false;


    private ArrayList<Presentation> presentations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);
        //replaceFragment(interactionAvailable);
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


        askButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //v.startAnimation(animTranslate);
                dispQuestionTextBox(v);
            }
        });
    }


    public void dispQuestionTextBox(View view){
        if(buttonPressed == false){
            askButton.setText(" Send ");
            buttonPressed = true;
        } else{
            askButton.setText(" Ask  ");
            buttonPressed = false;
        }
        if(isTextBoxVisible == true){
            editText.setVisibility(View.VISIBLE);
            isTextBoxVisible = false;
        } else{
            editText.setVisibility(View.INVISIBLE);
            isTextBoxVisible = true;
        }
    }



    //Method that populates the presentation arraylist
    //This should happen in HomeActivity
    private void getPresentations(String ID) {
        //TODO the magic
        ArrayList<Presentation> presentations;
    }
}
