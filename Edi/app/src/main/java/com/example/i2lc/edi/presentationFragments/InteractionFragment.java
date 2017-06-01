package com.example.i2lc.edi.presentationFragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.i2lc.edi.R;
import com.example.i2lc.edi.adapter.AnswerItemAdapter;
import com.example.i2lc.edi.dbClasses.Interaction;
import com.example.i2lc.edi.dbClasses.InteractiveElement;
import com.example.i2lc.edi.dbClasses.User;

import org.w3c.dom.Text;

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
    private EditText editTextView;
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
        if(interactiveElementInterface != null){
            liveElement = interactiveElementInterface.getInteractiveElementInterface();
        }
        if(userInterface != null){
            user = userInterface.getUserInterface();
        }
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_interaction, container, false);
        sendButton = (Button) rootView.findViewById(R.id.sendAnswer);
        editTextView = (EditText)rootView.findViewById(R.id.answerText);
        questionTextView = (TextView) rootView.findViewById(R.id.questionText);
        answersListView = (ListView) rootView.findViewById(R.id.answersList);
        timerView = (TextView) rootView.findViewById(R.id.timerView);
        questionTextView.setText(liveElement.getInteractiveElementQuestion());
        if(liveElement.getType() == "poll") {
            answersList = liveElement.getAnswers().split(",");
            answersListView.setVisibility(ListView.VISIBLE);
            sendButton.setVisibility(View.INVISIBLE);
            AnswerItemAdapter adapter = new AnswerItemAdapter(rootView.getContext(), answersList, user, liveElement.getInteractiveElementID());
            answersListView.setAdapter(adapter);
            answersListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView,View v,int position, long l){
                    TextView itemTextView =(TextView) getViewByPosition(position,answersListView);
                    answer = itemTextView.getText().toString();
                    interaction = new Interaction(user.getUserID(), liveElement.getInteractiveElementID(), answer);
                    try {
                        interaction.sendInteraction();
                    } catch (Exception e) {
                        System.out.println("Sending Interaction Failed");
                        e.printStackTrace();
                    }
                }
            });
        }
        else {
            sendButton.setVisibility(View.VISIBLE);
            answersListView.setVisibility(ListView.INVISIBLE);
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    answer = editTextView.getText().toString();
                    System.out.println(answer);
                    interaction = new Interaction(user.getUserID(), liveElement.getInteractiveElementID(), answer);
                    try {
                        interaction.sendInteraction();
                    } catch (Exception e) {
                        System.out.println("Sending Interaction Failed");
                        e.printStackTrace();
                    }
                }
            });
        }
        new CountDownTimer(liveElement.getResponsesInterval(), TICK_TIME) {
            public void onTick(long millisUntilFinished) {
                seconds++;
                if(timerView != null){
                    timerView.setText(Integer.toString(seconds));
                }
                System.out.print(Integer.toString(seconds));
            }
            public void onFinish() {
                seconds = 0;
            }
        }.start();
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

//    private ArrayList<String> getAnswersList(String answers){
//        String[] answerList = answers.split(",");
//    }
    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
