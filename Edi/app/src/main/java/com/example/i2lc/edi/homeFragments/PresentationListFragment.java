package com.example.i2lc.edi.homeFragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.i2lc.edi.R;
import com.example.i2lc.edi.adapter.PresentationItemAdapter;
import com.example.i2lc.edi.dbClasses.Presentation;
import com.example.i2lc.edi.dbClasses.User;

import java.util.ArrayList;


public class PresentationListFragment extends Fragment{
    private ArrayList<Presentation> presentationList;
    private ListView listView;
    private User user;
    private View rootView;
    private PresentationItemAdapter adapter;
    private GetPresentationListInterface presentationListInterface;
    private GetUserInterface userInterface;

    public PresentationListFragment() {
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
        if(presentationListInterface != null){
            presentationList = presentationListInterface.getLivePresentationList();
        }
        if(userInterface != null){
            user = userInterface.getUserInterface();
        }
        if(presentationList.size() == 0) {
            rootView = inflater.inflate(R.layout.no_presentations_layout, container, false);
        }else {
            rootView = inflater.inflate(R.layout.presentation_list_fragment, container, false);
            //Create GUI for list of presentations
            listView = (ListView) rootView.findViewById(R.id.presentation_list);
            adapter = new PresentationItemAdapter(rootView.getContext(), presentationList, user);
            listView.setAdapter(adapter);
        }
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            presentationListInterface = (GetPresentationListInterface) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement GetDataInteface");
        }
        try{
            userInterface = (GetUserInterface) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement GetUserInterface");
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        updatePresentationListView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface GetPresentationListInterface {
        ArrayList<Presentation> getLivePresentationList();
    }
    public interface GetUserInterface{
        User getUserInterface();
    }

    protected void updatePresentationListView() {
        //Updates the list of presentations
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(presentationList.size() > 0) {
                    ListView newListView = (ListView) rootView.findViewById(R.id.presentation_list);
                    PresentationItemAdapter adapter = new PresentationItemAdapter(rootView.getContext(),presentationList, user);
                    newListView.setAdapter(adapter);
                }else{
                    System.out.print("Presentation List is Empty!");
                }
            }
        });
    }
}
