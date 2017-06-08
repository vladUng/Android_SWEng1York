package com.example.i2lc.edi.presentationFragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.i2lc.edi.PresentationActivity;
import com.example.i2lc.edi.R;
import com.example.i2lc.edi.backend.SocketClient;
import com.example.i2lc.edi.backend.Utils;
import com.example.i2lc.edi.dbClasses.Presentation;
import com.example.i2lc.edi.dbClasses.Question;
import com.example.i2lc.edi.dbClasses.User;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Main Presentation Fragment
 * This fragment is instantiated when user joins presentation
 * It shows the title of the presentation and the progress during the presentation
 * Contains the 'Ask' feature, which allows user to send a question to the server
 */
public class MainPresentationFragment extends Fragment {
    private static final int QUESTION_DISABLE_TIME =  30000;
    private static final int TICK_TIME =  1000;

    private Presentation currentPresentation;
    private Button askButton;
    private Button cancelButton;
    private EditText editText;
    private ProgressBar progressBar;
    private int progress;
    private boolean buttonPressed = true;
    private Question question;
    private boolean questionEnabled = true;
    private GetPresentationInterface presentationInterface;
    private GetUserInterface userInterface;
    private User user;
    private Socket socket;
    private String serverIPAddress;
    private View rootView;

    public MainPresentationFragment() {
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
        rootView = inflater.inflate(R.layout.activity_presentation, container, false);
        PresentationActivity activity = (PresentationActivity) getActivity();
        TextView testText = (TextView) rootView.findViewById(R.id.testData);
        TextView titleTextView = (TextView) rootView.findViewById(R.id.presentationTitle);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        askButton = (Button) rootView.findViewById(R.id.askButton);
        cancelButton = (Button) rootView.findViewById(R.id.cancelButton);
        editText = (EditText)rootView.findViewById(R.id.questionText);
        //Set progress
        if(presentationInterface != null){
            currentPresentation = presentationInterface.getLivePresentation();
        }
        if(userInterface != null){
            user = userInterface.getUserInterface();
        }

        titleTextView.setText(currentPresentation.getTitle());
        updateProgressBar();

        //When button is pressed
        askButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuestionTextBox(v);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                cancelQuestion(v);
            }
        });
        //Display test Data
        testText.setText("User ID:" + Integer.toString(user.getUserID()) + "Pres ID: " + Integer.toString(currentPresentation.getPresentationID())+ " Module ID: " + Integer.toString(currentPresentation.getModuleID()) + " Module: " + currentPresentation.getModuleName());
        return rootView;
    }

    /**
     * Updates the progress bar UI element
     */
    protected void updateProgressBar() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView progressBarText = (TextView) rootView.findViewById(R.id.progressBarText);
                progressBarText.setText("Slide " + Integer.toString(currentPresentation.getCurrentSlideNumber() + 1) + " of " + Integer.toString(currentPresentation.getTotalSlideNumber()));
                progress = currentPresentation.calculateProgress();
                progressBar.setProgress(progress);
            }
        });
    }

    public interface GetPresentationInterface{
        Presentation getLivePresentation();
    }

    public interface GetUserInterface{
        User getUserInterface();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            presentationInterface = (GetPresentationInterface) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement GetDataInterface");
        }
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

    @Override
    public void onResume() {
        //connect client
        serverIPAddress = Utils.buildIPAddress("db.amriksadhra.com", 8080);
        connectToRemoteSocket();

        super.onResume();
    }

    @Override
    public void onStop() {
        socket.disconnect();
        super.onStop();
    }

    /**
     * Displays text box for user to type in and send question
     * @param view
     */
    public void showQuestionTextBox(View view){
        if(buttonPressed == false){
            question = new Question(user.getUserID(), currentPresentation.getPresentationID(),"", currentPresentation.getCurrentSlideNumber());
            try {
                if(questionEnabled == true) {
                    //Question must contain more than 1 character to be sent
                    if (editText.getText().toString().length() > 1) {
                        question.setQuestionData(editText.getText().toString());
                        question.sendQuestion();
                        editText.setText("");
                        askButton.setText(" Ask  ");
                        //Hide question text box when question is sent
                        editText.setVisibility(view.INVISIBLE);
                        //Hide cancel button when question is sent
                        cancelButton.setVisibility(view.INVISIBLE);
                        buttonPressed = true;
                        //Display toast message to user
                        Toast.makeText(getActivity(), "Your question has been sent!", Toast.LENGTH_LONG).show();
                        System.out.println(question.getQuestionData());
                        //Timer which keeps asking question disabled until finish
                        new CountDownTimer(QUESTION_DISABLE_TIME, TICK_TIME) {
                            public void onTick(long millisUntilFinished) {
                                questionEnabled = false;
                            }
                            public void onFinish() {
                                questionEnabled = true;
                            }
                        }.start();
                    } else {
                        //Display toast message if text box is empty and send is clicked
                        Toast.makeText(getActivity(), "Please write a question first", Toast.LENGTH_LONG).show();
                    }
                }else{
                    //Display toast message if countdown timer has not finished
                    Toast.makeText(getActivity(), "Please wait for 30s to send new question", Toast.LENGTH_LONG).show();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        } else{
            askButton.setText(" Send ");
            cancelButton.setVisibility(view.VISIBLE);
            editText.setVisibility(view.VISIBLE);
            buttonPressed = false;
        }
    }

    /**
     * Hides textbox and cancel button and makes textbox content to be blank
     * @param view
     */
    public void cancelQuestion(View view){
        askButton.setText(" Ask  ");
        editText.setVisibility(view.INVISIBLE);
        editText.setText("");
        cancelButton.setVisibility(view.INVISIBLE);
        buttonPressed = true;
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
            case "presentations":
                try {
                    SocketClient mySocketClient = new SocketClient();

                    Presentation presentation;
                    //get a currentPresentation
                    presentation = mySocketClient.getPresentation(currentPresentation.getPresentationID());
                    //update just when the interactive elements are different than null
                    if(presentation != null) {
                        System.out.println("we are at slide number" + presentation.getCurrentSlideNumber());
                        currentPresentation.setCurrentSlideNumber(presentation.getCurrentSlideNumber());
                        updateProgressBar();
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


}
