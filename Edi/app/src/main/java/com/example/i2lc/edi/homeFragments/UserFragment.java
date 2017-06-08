package com.example.i2lc.edi.homeFragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.i2lc.edi.R;
import com.example.i2lc.edi.dbClasses.User;

import org.w3c.dom.Text;

public class UserFragment extends Fragment {
    private GetUserInterface userInterface;
    private User user;

    public UserFragment() {
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
        View rootView = inflater.inflate(R.layout.user_fragment, container, false);
        TextView usernameText = (TextView) rootView.findViewById(R.id.usernameText);
        TextView firstNameText = (TextView) rootView.findViewById(R.id.firstNameText);
        TextView surnameText = (TextView) rootView.findViewById(R.id.surnameText);
        TextView emailText = (TextView) rootView.findViewById(R.id.emailText);
        TextView userIDText = (TextView) rootView.findViewById(R.id.userIDText);

        if(userInterface != null){
            //Display user data onto GUI elements
            user = userInterface.getUserInterface();
            userIDText.setText("User ID: " +Integer.toString(user.getUserID()));
            usernameText.setText("Username: " + user.getUsername());
            firstNameText.setText("First Name: " +user.getFirstName());
            surnameText.setText(("Surname: " + user.getSecondName()));
            emailText.setText("E-mail: " + user.getEmailAddress());
        }
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            userInterface = (GetUserInterface) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement GetUserInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface GetUserInterface{
        User getUserInterface();
    }
}
