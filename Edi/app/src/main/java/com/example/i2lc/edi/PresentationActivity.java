package com.example.i2lc.edi;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.EditText;

import com.example.i2lc.edi.backend.SocketClient;
import com.example.i2lc.edi.backend.Utils;
import com.example.i2lc.edi.dbClasses.InteractiveElement;
import com.example.i2lc.edi.dbClasses.Presentation;
import com.example.i2lc.edi.dbClasses.User;
import com.example.i2lc.edi.presentationFragments.InteractionFragment;
import com.example.i2lc.edi.presentationFragments.MainPresentationFragment;
import com.example.i2lc.edi.utilities.Slide;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class PresentationActivity extends AppCompatActivity implements InteractionFragment.OnFragmentInteractionListener,MainPresentationFragment.OnFragmentInteractionListener,MainPresentationFragment.GetPresentationInterface, MainPresentationFragment.GetUserInterface, InteractionFragment.GetUserInterface, InteractionFragment.GetInteractiveElementInterface{
    private Fragment fragment;
    boolean isInteractiveElementLive = false;
    private EditText editText;
    private User user;
    private Presentation currentPresentation;
    private InteractiveElement liveElement;

    //server socket variable
    private Socket socket;
    private String serverIPAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        currentPresentation = intent.getExtras().getParcelable("presentation");
        user = intent.getExtras().getParcelable("user");
        System.out.println("Presentation Activity: Received Presentation ID: " + Integer.toString(currentPresentation.getPresentationID()));

        setContentView(R.layout.activity_pres);
        //Show Edit Text to type question
        editText = (EditText) findViewById(R.id.questionText);

        checkLiveInteractiveElements();
    }

    private void replaceFragment(){
        if(isInteractiveElementLive == true){
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    //better to do the connection here, because we can directly triggered the UI
    public void connectToRemoteSocket() {
        //Alert tester that connection is being attempted
        System.out.println("Client: Attempting Connection to " + serverIPAddress);

        try {
            socket = IO.socket(serverIPAddress);
        } catch (URISyntaxException e) {
            System.out.println("Couldn't create client port");
        }

        //Handling socket events
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                System.out.println("Connected to socket");
            }

        });

        socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                System.out.println("For some reason the client is disconnected from the server. Some more info:" + args.toString());
            }
        });

        socket.on("DB_Update", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                System.out.println("Client knows DB has updated:  " + args[0]);
                updateLocalTables(args[0]);
                //TODO: ADD METHOD HERE TO UPDATE THE SCREEN AS WELL
            }

        });

        socket.connect();
    }

    public void updateLocalTables(Object tableToUpdate) {

        System.out.println("Table: " + (String)tableToUpdate + " has been updated on the server");
        //SocketIO will pass a generic object. But we know its a string because that's what DB_notify returns from com.i2lp.edi.server side
        switch ((String) tableToUpdate) {
            case "interactive_elements":
                checkLiveInteractiveElements();
                break;

            case "presentations":
                try {
                    SocketClient mySocketClient = new SocketClient();

                    Presentation presentation;

                    //get the latest version of the current presentation on the DB
                    presentation = mySocketClient.getPresentation(currentPresentation.getPresentationID());

                    //check if the current presentation has been updated
                    if(presentation != null) {

                        if (presentation.isLive()) {
                            System.out.println("we are at slide number" + presentation.getCurrentSlideNumber());
                            currentPresentation.setCurrentSlideNumber(presentation.getCurrentSlideNumber());
                            currentPresentation.calculateProgress();
                        } else {
                            System.out.println("Presentation is not live anymore, go to the presentation list");
                            currentPresentation.setLive(false);
                            super.finish();
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Ooops! There was a problem");
                    e.printStackTrace();
                }
                break;

            default:
                System.out.println("Other table than interactive_elements was updated");
                break;
        }
    }

    // Method that checks if there are any interactive elements live for the current presentation
    // updates the  UI accordingly
    private void checkLiveInteractiveElements(){
        try {
            SocketClient mySocketClient = new SocketClient();

            ArrayList<InteractiveElement> interactiveElementsDB;
            interactiveElementsDB = mySocketClient.getInteractiveElements(String.valueOf(currentPresentation.getPresentationID()));

            //update just when the interactive elements are different than null
            if(interactiveElementsDB != null) {                     //get the interactive element that has been updated

                for(Slide slide: currentPresentation.getSlideList()) {
                    for (InteractiveElement interactiveElement: slide.getSlideElementList()) {
                        for (InteractiveElement dummyElement : interactiveElementsDB) {
                            if (interactiveElement != null ) {
                                if (slide.getSlideID() == dummyElement.getSlideNumber()){
                                    if (interactiveElement.getXml_element_id() == dummyElement.getXml_element_id()) {
                                        interactiveElement.setLive(dummyElement.isLive());
                                        interactiveElement.setInteractiveElementID(dummyElement.getInteractiveElementID());
                                    }
                                }
                            }
                        }
                    }
                }

                liveElement = currentPresentation.getLiveElement();

                if(liveElement != null && isInteractiveElementLive == false){
                    isInteractiveElementLive = true;

                } else{
                    isInteractiveElementLive = false;
                }
                replaceFragment();
            }
        } catch (Exception e) {
            System.out.println("Ooops! There was a problem in checking interactive elements");
            e.printStackTrace();
        }
    }

    private void setUserActiveStatus(Boolean isInPresentation) {

        int SDK_INT = Build.VERSION.SDK_INT;
        // >SDK 8 support async operations
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            //connect client
            SocketClient socketClient = new SocketClient();
            boolean status;
            if (isInPresentation) {
                status = socketClient.toggleUserActivePresentation(currentPresentation.getPresentationID(), user.getUserID());
            } else {
                status = socketClient.toggleUserActivePresentation(0, user.getUserID());
            }

            if (status) {
                //for debug
                System.out.println("YAY the user status has been toggled");
            } else {
                //for debug
                System.out.println("There was an error ");
            }
        } else {
            //for debug
            System.out.println("There was an error. SDK too old");
        }
    }

    @Override
    protected void onResume() {

        //connect client
        serverIPAddress = Utils.buildIPAddress("db.amriksadhra.com", 8080);
        connectToRemoteSocket();

        //set user active
        setUserActiveStatus(true);

        super.onResume();
    }

    @Override
    protected void onStop() {

        //disconnect from server
        socket.disconnect();

        //set user inactive
        setUserActiveStatus(false);

        super.onStop();
    }

    @Override
    public Presentation getLivePresentation() {
        return currentPresentation;
    }
    @Override
    public User getUserInterface() {
        return user;
    }
    @Override
    public InteractiveElement getInteractiveElementInterface() {
        return liveElement;
    }


}
