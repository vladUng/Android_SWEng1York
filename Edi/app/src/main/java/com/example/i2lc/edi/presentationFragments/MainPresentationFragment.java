package com.example.i2lc.edi.presentationFragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainPresentationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainPresentationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainPresentationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int QUESTION_DISABLE_TIME =  30000;
    private static final int TICK_TIME =  1000;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
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

    //for establishing connection
    private Socket socket;
    private String serverIPAddress;

    private View rootView;

    public MainPresentationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainPresentationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainPresentationFragment newInstance(String param1, String param2) {
        MainPresentationFragment fragment = new MainPresentationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        View rootView = inflater.inflate(R.layout.activity_presentation, container, false);
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
//        progressBarText.setText("Slide " + Integer.toString(currentPresentation.getCurrentSlideNumber() + 1) + " of " + Integer.toString(currentPresentation.getTotalSlideNumber() + 1));
//        progress = currentPresentation.calculateProgress();
//        //
//        progressBar.setProgress(progress);

        updateProgressBar();

        //When button is pressed
        askButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispQuestionTextBox(v);
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

    protected void updateProgressBar() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView progressBarText = (TextView) rootView.findViewById(R.id.progressBarText);
                progressBarText.setText("Slide " + Integer.toString(currentPresentation.getCurrentSlideNumber() + 1) + " of " + Integer.toString(currentPresentation.getTotalSlideNumber()));
                progress = currentPresentation.calculateProgress();
                //
                progressBar.setProgress(progress);

            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
    public interface GetPresentationInterface{
        Presentation getLivePresentation();
    }

    public interface GetUserInterface{
        User getUserInterface();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

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
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void dispQuestionTextBox(View view){
        if(buttonPressed == false){
            question = new Question(user.getUserID(), currentPresentation.getPresentationID(),"", currentPresentation.getCurrentSlideNumber());
            try {
                if(questionEnabled == true) {
                    if (editText.getText().toString().length() > 3) {
                        question.setQuestionData(editText.getText().toString());
                        question.sendQuestion();
                        editText.setText("");
                        askButton.setText(" Ask  ");
                        editText.setVisibility(view.INVISIBLE);
                        cancelButton.setVisibility(view.INVISIBLE);
                        buttonPressed = true;
                        Toast.makeText(getActivity(), "Your question has been sent!", Toast.LENGTH_LONG).show();
                        System.out.println(question.getQuestionData());
                        new CountDownTimer(QUESTION_DISABLE_TIME, TICK_TIME) {
                            public void onTick(long millisUntilFinished) {
                                questionEnabled = false;
                            }
                            public void onFinish() {
                                questionEnabled = true;
                            }
                        }.start();
                    } else {
                        Toast.makeText(getActivity(), "Please write a question first", Toast.LENGTH_LONG).show();
                    }
                }else{
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
                //TODO: ADD METHOD HERE TO UPDATE THE SCREEN AS WELL
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
}
