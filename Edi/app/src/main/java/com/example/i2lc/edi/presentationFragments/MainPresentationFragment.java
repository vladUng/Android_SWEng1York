package com.example.i2lc.edi.presentationFragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.i2lc.edi.PresentationActivity;
import com.example.i2lc.edi.R;
import com.example.i2lc.edi.backend.SocketClient;
import com.example.i2lc.edi.dbClasses.Presentation;
import com.example.i2lc.edi.dbClasses.Question;
import com.example.i2lc.edi.model.PresentationMod;

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
    private Presentation presentation;
    private Button askButton;
    private Button cancelButton;
    private EditText editText;
    private ProgressBar progressBar;
    private int progress;
    private boolean buttonPressed = true;
    private Question question;
    private boolean questionEnabled = true;

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
        View rootView = inflater.inflate(R.layout.activity_presentation, container, false);
        PresentationActivity activity = (PresentationActivity) getActivity();
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        askButton = (Button) rootView.findViewById(R.id.askButton);
        cancelButton = (Button) rootView.findViewById(R.id.cancelButton);
        editText = (EditText)rootView.findViewById(R.id.questionText);
        //Set progress
        progress = activity.getPresentation().calculateProgress();
        progressBar.setProgress(progress);
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
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
            question = new Question(1,1,"", 5);//TODO: remove this and link with the actual presentation data
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


}
