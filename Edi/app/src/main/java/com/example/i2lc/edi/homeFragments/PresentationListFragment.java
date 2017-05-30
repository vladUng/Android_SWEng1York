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
        View rootView = inflater.inflate(R.layout.presentation_list_fragment, container, false);
        View itemView = inflater.inflate(R.layout.presentation_item, container, false);
//        presentationList = new ArrayList<Presentation>();
//        //getPresentation(userID);
//        getModules(userID);
//        for(Module module: modules) {
//            for (Presentation presentation : module.getPresentations()) {
//                if(presentation.isLive() == true) {
//                    System.out.println("Presentation " + Integer.toString(presentation.getPresentationID()) + " is live.");
//                    downloadPresentation(presentation, rootView.getContext());
//
//                    //Create folder
//                    File presentationFolder = new File(presentation.getFolderPath()); //
//                    //Create list of files
//                    File[] directoryListing = presentationFolder.listFiles();
//                    if (directoryListing != null) {
//                        for (File child : directoryListing) {
//                            //Check if file in directory is an xml file
//                            if (child.getAbsolutePath().contains(".xml")) {
//                                ParserXML parser = new ParserXML(presentation, child);
//                                presentationList.add(parser.parsePresentation());
//                            }
//                        }
//                        for (File child: directoryListing){
//                            if (child.isDirectory() && child.getAbsolutePath().contains("Thumbnails")) {
//                                File[] thumbnails = child.listFiles();
//                                if (thumbnails != null) {
//                                    String thumbnailPath;
//                                    for (File thumbnail : thumbnails) {
//                                        thumbnailPath = thumbnail.getAbsolutePath();
//                                        if (thumbnail.isHidden() == false && thumbnailPath.contains("slide0")) {
//                                            presentationList.get(presentationList.size() - 1).setThumbnailPath(thumbnailPath);
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    } else {
//                        System.out.println("Folder doesn't exist/is empty!");
//                    }
//                }else{
//                    System.out.println("Presentation " + Integer.toString(presentation.getPresentationID()) + "is not live.");
//                }
//            }
//        }

        if(presentationListInterface != null){
            presentationList = presentationListInterface.getLivePresentationList();
        }
        //Create GUI
        listView = (ListView) rootView.findViewById(R.id.presentation_list);
        PresentationItemAdapter adapter = new PresentationItemAdapter(rootView.getContext(),presentationList);
        listView.setAdapter(adapter);
//        in
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
    }

    @Override
    public void onResume(){
        super.onResume();
        if(presentationListInterface != null){
            presentationList = presentationListInterface.getLivePresentationList();
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

    public interface GetPresentationListInterface {
        ArrayList<Presentation> getLivePresentationList();
    }

    public void joinPresentation(View view) {
        Intent intent = new Intent(this.getActivity(), PresentationActivity.class);
        startActivity(intent);
    }

    //TODO not needed here, but may be useful somewhere else
    private void getPresentation(String forUserId){

        int SDK_INT = Build.VERSION.SDK_INT;
        // >SDK 8 support async operations
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            //connect client
            SocketClient socketClient = new SocketClient();

            //clear the presentation first
            //presentationList.clear();
            presentationList = socketClient.getPresentationsForUserId(forUserId);

            if (!presentationList.isEmpty()) {
                //for debug
                System.out.println("YAY I have all the presentations for userID: " + forUserId);

                for (Presentation presentation : presentationList) {
                    System.out.println("ID: " + presentation.getPresentationID() + " ModuleID: " + presentation.getModuleID() + " Subject: " + presentation.getXmlURL());
                }
            } else {
                //for debug
                System.out.println("There was an error from getting the presentations from server");
            }
        } else {
            //for debug
            System.out.println("There was an error. SDK too old");
        }
    }

    private void getModules(String userID){

        int SDK_INT = Build.VERSION.SDK_INT;
        // >SDK 8 support async operations
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            //connect client
            SocketClient socketClient = new SocketClient();

            //clear the modules first
            //modules.clear();
            modules = socketClient.getModules(userID);

            if (!modules.isEmpty()) {
                //for debug
                System.out.println("YAY I have all the modules for userID: " + userID);

                for (Module module : modules) {
                    System.out.println("ID: " + module.getModuleID() + " Name: " + module.getModuleName() + " Subject: " + module.getSubject() + " Description: "
                            + module.getDescription());
                }
            } else {
                //for debug
                System.out.println("There was an error from getting he modules from server");
            }
        } else {
            //for debug
            System.out.println("There was an error. SDK too old");
        }
    }

    public void downloadPresentation(Presentation presentation, Context c) {

        String filename = "Presentation_" + presentation.getPresentationID();
        String basePath = c.getFilesDir().getAbsolutePath() + "/";

        try {
            URL u = presentation.getXmlURL();
            InputStream is = u.openStream();

            DataInputStream dis = new DataInputStream(is);

            byte[] buffer = new byte[1024];
            int length;

            FileOutputStream fos = new FileOutputStream(new File(basePath+ "/" + filename + ".zip"));
            while ((length = dis.read(buffer))>0) {
                fos.write(buffer, 0, length);
            }

        } catch (MalformedURLException mue) {
            Log.e("SYNC getUpdate", "malformed url error", mue);
        } catch (IOException ioe) {
            Log.e("SYNC getUpdate", "io error", ioe);
        } catch (SecurityException se) {
            Log.e("SYNC getUpdate", "security error", se);
        }

        String zipFileName = basePath + filename + ".zip";
        String zipFolder =  basePath + filename +"_folder/";

        try {
            File destinationFolder = new File(zipFolder);

            DecompressFast.unzip(new File(zipFileName),destinationFolder);
            System.out.println("Extracted to \n"+ zipFolder);
            presentation.setFolderPath(destinationFolder.getAbsolutePath());
        } catch (ZipException e) {
            Log.e("Problems with zip", e.getMessage());
        } catch (IOException e) {
            Log.e("We got a problem", e.getMessage());
        }
    }
}
