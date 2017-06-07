package com.example.i2lc.edi.presentationFragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.i2lc.edi.R;
import com.example.i2lc.edi.adapter.AnswerItemAdapter;
import com.example.i2lc.edi.dbClasses.Interaction;
import com.example.i2lc.edi.dbClasses.InteractiveElement;
import com.example.i2lc.edi.dbClasses.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InteractionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InteractionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InteractionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int TICK_TIME =  1000;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button sendButton;
    private EditText answerTextView;
    private String answer;
    private OnFragmentInteractionListener mListener;
    private GetInteractiveElementInterface interactiveElementInterface;
    private MainPresentationFragment.GetUserInterface userInterface;
    private User user;
    private InteractiveElement liveElement;
    private String[] answersList;
    private ListView answersListView;
    private Interaction interaction;
    private TextView questionTextView;
    private String questionText;
    private int seconds = 0;
    private TextView timerView;
    private boolean isPollAnswerClicked = false;
    private View rootView;

    public InteractionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InteractionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InteractionFragment newInstance(String param1, String param2) {
        InteractionFragment fragment = new InteractionFragment();
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

        isPollAnswerClicked = false;
        if(interactiveElementInterface != null){
            liveElement = interactiveElementInterface.getInteractiveElementInterface();
        }
        if(userInterface != null){
            user = userInterface.getUserInterface();
        }
        if(liveElement.getType().toLowerCase().equals("wordcloud")) {
            // Inflate the layout for this fragment
            rootView = inflater.inflate(R.layout.wordcloud_fragment, container, false);
            sendButton = (Button) rootView.findViewById(R.id.sendWordcloudAnswer);
            answerTextView = (EditText) rootView.findViewById(R.id.answerText);
            questionTextView = (TextView) rootView.findViewById(R.id.questionTextWordcloud);
            answersListView = (ListView) rootView.findViewById(R.id.answersList);
            timerView = (TextView) rootView.findViewById(R.id.timerView);
            questionTextView.setText(liveElement.getInteractiveElementQuestion());
        }else if (liveElement.getType().toLowerCase().equals("poll")){
            // Inflate the layout for this fragment
            rootView = inflater.inflate(R.layout.poll_fragment, container, false);
            sendButton = (Button) rootView.findViewById(R.id.sendPollAnswer);
            questionTextView = (TextView) rootView.findViewById(R.id.questionTextPoll);
            answersListView = (ListView) rootView.findViewById(R.id.answersList);

            answersList = liveElement.getAnswers().split(",");
            //sendButton.setVisibility(View.INVISIBLE);
            final AnswerItemAdapter adapter = new AnswerItemAdapter(rootView.getContext(), answersList, user, liveElement.getInteractiveElementID());
            answersListView.setAdapter(adapter);
            answersListView.setVisibility(ListView.VISIBLE);
            answersListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView,View v,int position, long l){
                    answer = Integer.toString(position);
                    isPollAnswerClicked = true;
                    for(int i = 0;i<answersListView.getCount();i++){
                        if(i == position) {
                            answersListView.getChildAt(i).findViewById(R.id.answerItem).setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorAccent));
                        } else{
                            answersListView.getChildAt(i).findViewById(R.id.answerItem).setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimary));
                        }

                    }
                }
            });
        }else{
            System.out.println("Error when loading interaction fragment!");
        }
        questionTextView.setText(liveElement.getInteractiveElementQuestion());

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(liveElement.getType().toLowerCase().equals("wordcloud")) {
                    answer = answerTextView.getText().toString();
                }
                interaction = new Interaction(user.getUserID(), liveElement.getInteractiveElementID(), answer);
                try {
                    if(liveElement.getType().toLowerCase().equals("wordcloud")) {
                        interaction.sendInteraction();
                        answerTextView.setText("");
                        Toast.makeText(getActivity(), "Your answer has been sent!", Toast.LENGTH_LONG).show();
                        System.out.println("Sent Answer: " + answer);
                    }else if(liveElement.getType().toLowerCase().equals("poll") && isPollAnswerClicked){
                        interaction.sendInteraction();
                        Toast.makeText(getActivity(), "Your answer has been sent!", Toast.LENGTH_LONG).show();
                        sendButton.setVisibility(View.INVISIBLE);
                        System.out.println("Sent Answer: " + answer);
                    } else{
                        Toast.makeText(getActivity(), "Please select an answer first!", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    System.out.println("Sending Interaction Failed");
                    e.printStackTrace();
                }
            }
        });

        Vibrator v = (Vibrator) rootView.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(500);

        return rootView;
    }

    public interface GetInteractiveElementInterface{
        InteractiveElement getInteractiveElementInterface();
    }
    public interface GetUserInterface{
        User getUserInterface();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
            interactiveElementInterface = (InteractionFragment.GetInteractiveElementInterface) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement GetInteractiveElementInterface");
        }
        try{
            userInterface = (MainPresentationFragment.GetUserInterface) context;
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
}
