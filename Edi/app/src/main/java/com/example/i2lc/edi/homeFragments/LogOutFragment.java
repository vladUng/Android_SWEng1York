package com.example.i2lc.edi.homeFragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.i2lc.edi.LogInActivity;
import com.example.i2lc.edi.R;

/**
 * Log Out Fragment
 * Contains a simple button which sends the user back to the Log In Activity
 */
public class LogOutFragment extends Fragment {
    private Button logOutButton;
    private View rootView;

    public LogOutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.log_out_fragment, container, false);
        logOutButton = (Button) rootView.findViewById(R.id.logOutButton);
        //Start LogInActivity on button click
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LogInActivity.class);
                intent.putExtra("logOut", "logOut");
                v.getContext().startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
