package com.example.i2lc.edi.presentationFragments;

import android.app.Fragment;
import android.content.Context;
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
 * Interaction Fragment
 * Fragment which gets instantiated when an interactive element is live
 * It checks for the type of interactive element and shows the corresponding GUI
 * User input is sent to the database.
 */
public class InteractionFragment extends Fragment {
    private String[] answersList;
    private String answer;
    private User user;
    private Interaction interaction;
    private InteractiveElement liveElement;
    private Button sendButton;
    private EditText answerTextView;
    private ListView answersListView;
    private TextView questionTextView;
    private boolean isPollAnswerClicked = false;
    private View rootView;

    private GetInteractiveElementInterface interactiveElementInterface;
    private MainPresentationFragment.GetUserInterface userInterface;

    public InteractionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            //Create views
            sendButton = (Button) rootView.findViewById(R.id.sendWordcloudAnswer);
            answerTextView = (EditText) rootView.findViewById(R.id.answerText);
            questionTextView = (TextView) rootView.findViewById(R.id.questionTextWordcloud);
            answersListView = (ListView) rootView.findViewById(R.id.answersList);
            questionTextView.setText(liveElement.getInteractiveElementQuestion());
        }else if (liveElement.getType().toLowerCase().equals("poll")){
            // Inflate the layout for this fragment
            rootView = inflater.inflate(R.layout.poll_fragment, container, false);
            sendButton = (Button) rootView.findViewById(R.id.sendPollAnswer);
            questionTextView = (TextView) rootView.findViewById(R.id.questionTextPoll);
            answersListView = (ListView) rootView.findViewById(R.id.answersList);
            //Split answers string into answers stored in array of strings
            answersList = liveElement.getAnswers().split(",");
            //Create poll answers list GUI
            final AnswerItemAdapter adapter = new AnswerItemAdapter(rootView.getContext(), answersList);
            answersListView.setAdapter(adapter);
            answersListView.setVisibility(ListView.VISIBLE);
            answersListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView,View v,int position, long l){
                    //Detected which answer was clicked
                    answer = Integer.toString(position);
                    isPollAnswerClicked = true;
                    for(int i = 0;i<answersListView.getCount();i++){
                        if(i == position) {
                            //Highlight clicked answer
                            answersListView.getChildAt(i).findViewById(R.id.answerItem).setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorAccent));
                        } else{
                            //Change colour of answers not clicked
                            answersListView.getChildAt(i).findViewById(R.id.answerItem).setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimary));
                        }
                    }
                }
            });
        }else{
            System.out.println("Error when loading interaction fragment!");
        }
        //get question to display on UI
        questionTextView.setText(liveElement.getInteractiveElementQuestion());

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set answer from editTextView if interactive element is wordcloud
                if(liveElement.getType().toLowerCase().equals("wordcloud")) {
                    answer = answerTextView.getText().toString();
                }

                interaction = new Interaction(user.getUserID(), liveElement.getInteractiveElementID(), answer);
                try {
                    if(liveElement.getType().toLowerCase().equals("wordcloud")) {
                        interaction.sendInteraction();
                        //Set the editText box to be blank
                        answerTextView.setText("");
                        //Display toast message to user
                        Toast.makeText(getActivity(), "Your answer has been sent!", Toast.LENGTH_LONG).show();
                        System.out.println("Sent Answer: " + answer);
                    }else if(liveElement.getType().toLowerCase().equals("poll") && isPollAnswerClicked){
                        interaction.sendInteraction();
                        //Display toast message to user
                        Toast.makeText(getActivity(), "Your answer has been sent!", Toast.LENGTH_LONG).show();
                        //For polls user can only send one answer, so button is hidden on click
                        sendButton.setVisibility(View.INVISIBLE);
                        System.out.println("Sent Answer: " + answer);
                    } else{
                        //Display toast message if user doesn't click an answer
                        Toast.makeText(getActivity(), "Please select an answer first!", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    System.out.println("Sending Interaction Failed");
                    e.printStackTrace();
                }
            }
        });
        // Vibrate for 500 milliseconds
        vibrate(500);
        return rootView;
    }

    /**
     * Makes device vibrate
     * @param time - vibration duration
     */
    public void vibrate(int time){
        Vibrator v = (Vibrator) rootView.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(time);
    }

    public interface GetInteractiveElementInterface{
        InteractiveElement getInteractiveElementInterface();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
    }
}
