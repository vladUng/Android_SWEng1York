package com.example.i2lc.edi;

import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.i2lc.edi.backend.SocketClient;
import com.example.i2lc.edi.backend.Utils;
import com.example.i2lc.edi.dbClasses.InteractiveElement;
import com.example.i2lc.edi.dbClasses.Presentation;
import com.example.i2lc.edi.model.PresentationMod;
import com.example.i2lc.edi.presentationFragments.InteractionFragment;
import com.example.i2lc.edi.presentationFragments.MainPresentationFragment;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class PresentationActivity extends AppCompatActivity implements InteractionFragment.OnFragmentInteractionListener,MainPresentationFragment.OnFragmentInteractionListener {
    private Fragment fragment;
    private PresentationMod presentation;
    boolean interactionAvailable = false;
    private ProgressBar progressBar;
    private int progress;
    private Button askButton;
    private EditText editText;
    private boolean questionEnabled = true;

    private Presentation currentPresentation;

    //if null, no interactive Element
    private InteractiveElement liveElement;

    //for establishing connection
    private Socket socket;
    private String serverIPAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO delete this, is just for testing
        try {
            currentPresentation = new Presentation(1,1, new URL("http://www.amriksadhra.com/Edi/sampleinput_69.zip"), true);
            SocketClient mySocketClient = new SocketClient();
            currentPresentation.setInteractiveElements(mySocketClient.getInteractiveElements(String.valueOf(currentPresentation.getPresentationID())));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void sendInteraction(int userID, int interactiveElementID, String interactionData) throws Exception {
        int SDK_INT = Build.VERSION.SDK_INT;
        // >SDK 8 support async operations
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            //connect client
            SocketClient socketClient = new SocketClient();
            String status = socketClient.postInteraction(userID, interactiveElementID, interactionData);

            if (status.equals("success")) {
                //for debug
                System.out.println("YAY the question was successfully sent");
            } else {
                //for debug
                System.out.println("There was an error sending the question to server: " + status);
            }
        } else {
            //for debug
            System.out.println("There was an error. SDK too old");
            throw new Exception();
        }
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
            }

        });

        socket.connect();
    }

    public void updateLocalTables(Object tableToUpdate) {

        System.out.println("Table: " + (String)tableToUpdate + " has been updated on the server");
        //SocketIO will pass a generic object. But we know its a string because that's what DB_notify returns from com.i2lp.edi.server side
        switch ((String) tableToUpdate) {
            case "interactive_elements":
                try {
                    SocketClient mySocketClient = new SocketClient();

                    ArrayList<InteractiveElement> interactiveElements = new ArrayList<>();
                    interactiveElements = mySocketClient.getInteractiveElements(String.valueOf(currentPresentation.getPresentationID()));

                    //update just when the interactive elements are different than null
                    if(interactiveElements != null) {
                        currentPresentation.setInteractiveElements(interactiveElements);
                        liveElement = currentPresentation.getLiveElement();

                        if (liveElement != null) {
                            replaceFragment();
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Ooops! There was a problem");
                    e.printStackTrace();
                }

                break;
            case "presentations":
                //go back to homeActivity, if the presentation is not liver anymore
                break;
            default:
                System.out.println("Other table than interactive_elements was updated");
                break;
        }
    }

    public Presentation getCurrentPresentation() {
        return currentPresentation;
    }

    public void setCurrentPresentation(Presentation currentPresentation) {
        this.currentPresentation = currentPresentation;
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
                 status = socketClient.toggleUserActivePresentation(currentPresentation.getPresentationID(), Integer.valueOf("14"));
            } else {
                status = socketClient.toggleUserActivePresentation(0, Integer.valueOf("14"));
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
        socket.disconnect();

        //set user inactive
        setUserActiveStatus(false);

        super.onStop();
    }
}
