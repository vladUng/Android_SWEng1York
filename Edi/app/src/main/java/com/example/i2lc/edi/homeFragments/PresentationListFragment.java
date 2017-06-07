package com.example.i2lc.edi.homeFragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.i2lc.edi.PresentationActivity;
import com.example.i2lc.edi.R;
import com.example.i2lc.edi.adapter.PresentationItemAdapter;
import com.example.i2lc.edi.backend.DecompressFast;
import com.example.i2lc.edi.backend.SocketClient;
import com.example.i2lc.edi.dbClasses.Module;
import com.example.i2lc.edi.dbClasses.Presentation;
import com.example.i2lc.edi.dbClasses.User;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.ZipException;

//import static com.example.i2lc.edi.LogInActivity.EXTRA_USERNAME;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PresentationListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PresentationListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PresentationListFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ArrayList<Presentation> presentationList;
    private ArrayList<Presentation> finalPresentationList;
    private ArrayList<Module> modules;
    private ListView listView;
    private String userID = "1";
    private Bitmap presentationThumbnail;
    private GetPresentationListInterface presentationListInterface;
    private Button joinButton;
    private GetUserInterface userInterface;
    private User user;
    //for establishing connection
//    private Socket socket;
//    private String serverIPAddress;
    private View rootView;
    private PresentationItemAdapter adapter;


    public PresentationListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PresentationListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PresentationListFragment newInstance(String param1, String param2) {
        PresentationListFragment fragment = new PresentationListFragment();
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
            //Create GUI
            listView = (ListView) rootView.findViewById(R.id.presentation_list);
            adapter = new PresentationItemAdapter(rootView.getContext(),presentationList, user);
            //adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        }

        return rootView;
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

    public interface GetPresentationListInterface {
        ArrayList<Presentation> getLivePresentationList();
    }
    public interface GetUserInterface{
        User getUserInterface();
    }

    protected void updatePresentationListView() {
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

    @Override
    public void onPause() {
//        socket.disconnect(); //to avoid having issues with other instances of socketClient
        super.onPause();
    }

}
