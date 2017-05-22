package com.example.i2lc.edi;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.i2lc.edi.model.PresentationMod;
import com.example.i2lc.edi.presFragments.InteractionFragment;
import com.example.i2lc.edi.presFragments.MainPresentationFragment;

public class PresentationActivity extends AppCompatActivity implements InteractionFragment.OnFragmentInteractionListener,MainPresentationFragment.OnFragmentInteractionListener {
    private Fragment fragment;
    private PresentationMod presentation;
    boolean interactionAvailable = true;
    private ProgressBar progressBar;
    private int progress;
    private Button askButton;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pres);

        presentation = new PresentationMod();
        progress = presentation.calculateProgress();

        //Show Edit Text to type question
        editText = (EditText)findViewById(R.id.questionText);

        replaceFragment();
        if(interactionAvailable){
            runInteraction();
        }

    }

    private void runInteraction(){
        new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                interactionAvailable = false;
                replaceFragment();
            }
        }.start();
    }

    private void replaceFragment(){
        if(interactionAvailable == true){
            fragment = new InteractionFragment();
        }else{
            fragment = new MainPresentationFragment();
        }

        if(null!=fragment){
            FragmentManager fragmentManager = getFragmentManager();
            android.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_pres_content, fragment);
            transaction.commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //can be empty
    }

    public PresentationMod getPresentation(){
        return this.presentation;
    }

}
