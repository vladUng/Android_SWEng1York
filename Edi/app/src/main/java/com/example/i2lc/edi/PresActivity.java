package com.example.i2lc.edi;

import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.i2lc.edi.presFragments.InteractionFragment;
import com.example.i2lc.edi.presFragments.MainPresentationFragment;
// This was added instead of Presentation Activity to implement in parallel while not losing Presentation Activity
//This will take Presentation Activity's place
public class PresActivity extends AppCompatActivity implements InteractionFragment.OnFragmentInteractionListener,MainPresentationFragment.OnFragmentInteractionListener {
    private Fragment fragment;
    boolean interactionAvailable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pres);
        replaceFragment(interactionAvailable);
        if(interactionAvailable){
            new CountDownTimer(5000, 1000) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    interactionAvailable = false;
                    replaceFragment(interactionAvailable);
                }
            }.start();
        }
    }

    private void replaceFragment(boolean interactionAvailable){
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

}
