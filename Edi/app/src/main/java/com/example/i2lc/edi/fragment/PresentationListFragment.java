package com.example.i2lc.edi.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.i2lc.edi.PresentationActivity;
import com.example.i2lc.edi.R;
import com.example.i2lc.edi.adapter.PresentationItemAdapter;
import com.example.i2lc.edi.backend.SocketClient;
import com.example.i2lc.edi.dbClasses.Module;
import com.example.i2lc.edi.dbClasses.Presentation;
import com.example.i2lc.edi.utilities.ParserXML;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
        finalPresentationList = new ArrayList<Presentation>();

        getPresentation(userID);
        getModules(userID);


        for(int i = 0; i < presentationList.size();i++){
            Presentation presentation = presentationList.get(i);
            //download and set the folder path for presentation
            if (i==2) {
                System.out.println("adasd");
            }
            downloadPresentation(presentation, rootView.getContext());//TODO: this is where it crashes

            //Create folder
            File presentationFolder = new File(presentation.getFolderPath()); //
            //Create list of files
            File[] directoryListing = presentationFolder.listFiles();
            if (directoryListing != null) {
                for (File child : directoryListing) {
                    if(child.getAbsolutePath().contains(".xml")){
                        ParserXML parser = new ParserXML(rootView,presentation, child);
                        finalPresentationList.add(i,parser.parsePresentation());
                    } else if(child.isDirectory()){
                        File[] thumbnails = child.listFiles();
                        if (thumbnails != null)  {
                            finalPresentationList.get(i).setThumbnailPath(thumbnails[0].getAbsolutePath());
                        }
                    }
                }
            } else {
                System.out.println("Folder doesn't exist/is empty!");
            }

            presentation = finalPresentationList.get(i);
            presentation.setModule(modules.get(presentation.getModuleID()).getModuleName());
        }

        //Create GUI
        listView = (ListView) rootView.findViewById(R.id.presentation_list);
        PresentationItemAdapter adapter = new PresentationItemAdapter(rootView.getContext(),presentationList);
        listView.setAdapter(adapter);
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

        FileOutputStream fos;
        String filename = "Presentation_" + presentation.getPresentationID();
        try {
            URL website = presentation.getXmlURL();
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            fos = c.openFileOutput(filename + ".zip", Context.MODE_PRIVATE);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            System.out.println("Download succesfull");
            rbc.close();
            fos.close();
        } catch(IOException e) {
            System.out.println("Error:" + e);
            e.printStackTrace();
        }

        unzipPresentation(filename + ".zip", filename + "_folder", presentation, c);
    }

    public void unzipPresentation(String zipFile, String outputFolder, Presentation presentation, Context c) {
        byte[] buffer = new byte[1024];
        String basePath = c.getFilesDir().getAbsolutePath() + "/";

        try {
            //create output directory is not exists
            File folder = new File(basePath + outputFolder);

            if (!folder.exists()) {
                if ( !(folder.mkdirs())) {
                    System.out.println("Unable to create folder" + folder.getName());
                }
            }

            //get the zip file content
            ZipInputStream zis = new ZipInputStream(new FileInputStream(basePath + zipFile));
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {
                String fileName = ze.getName();

                //skip CSS files,folder and MAC_OSX files
                if (fileName.contains("CSS") || fileName.contains("__MACOSX/") || fileName.contains(".DS_Store")) {
                    ze = zis.getNextEntry();
                    continue;
                }

                String tmpFileName = outputFolder + File.separator + fileName;
                File newFile = new File( basePath + tmpFileName);
                if(!newFile.exists()){
                    break;
                }
                if (!tmpFileName.contains(".")) {
                    if(!newFile.exists()) {
                        if (!newFile.mkdir()) {
                            System.out.println("Unable to create folder" + newFile.getName());
                        }
                    }
                    ze = zis.getNextEntry();
                    continue;
                }

                System.out.println("Unzipping to: " + newFile.getAbsoluteFile());

                File dummyFile = new File(newFile.getAbsolutePath());
                FileOutputStream fos = new FileOutputStream(dummyFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

            System.out.println("Yay zip succesfull");
            zis.closeEntry();
            zis.close();

            //now that everything went well, save the path
            presentation.setFolderPath(folder.getAbsolutePath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //Cleanup downloaded zip file by deleting
        new File(zipFile).delete();
    }

}
