package com.example.i2lc.edi;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.i2lc.edi.adapter.SlidingMenuAdapter;
import com.example.i2lc.edi.backend.SocketClient;
import com.example.i2lc.edi.backend.Utils;
import com.example.i2lc.edi.dbClasses.Interaction;
import com.example.i2lc.edi.dbClasses.InteractiveElement;
import com.example.i2lc.edi.dbClasses.Module;
import com.example.i2lc.edi.dbClasses.Presentation;
import com.example.i2lc.edi.dbClasses.Question;
import com.example.i2lc.edi.homeFragments.PresentationListFragment;
import com.example.i2lc.edi.homeFragments.Fragment2;
import com.example.i2lc.edi.homeFragments.UserFragment;
import com.example.i2lc.edi.model.ItemSlideMenu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Cosmin on 15/03/2017.
 */

public class HomeActivity extends AppCompatActivity implements PresentationListFragment.OnFragmentInteractionListener, Fragment2.OnFragmentInteractionListener, UserFragment.OnFragmentInteractionListener {

    private List<ItemSlideMenu> listSliding;
    private SlidingMenuAdapter adapter;
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ActionBar actionBar;
    private Fragment fragment;

    private ArrayList<Module> modules;
    private ArrayList<Presentation> livePresentations = new ArrayList<>();

    //these are not needed in this activity, are used for debugging
    private ArrayList<InteractiveElement> interactiveElements;
    private ArrayList<Interaction> interactions;
    private ArrayList<Question> questions;
    private ArrayList<Presentation> presentations;

    //for establishing connection
    private Socket socket;
    private String serverIPAddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Toast.makeText(this, "You have successfully logged in!", Toast.LENGTH_LONG).show();

        //Intro Component
        listViewSliding = (ListView) findViewById(R.id.lv_sliding_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listSliding = new ArrayList<>();
        //Add item for sliding list
        listSliding.add(new ItemSlideMenu(R.drawable.ic_school_black_24dp, "Live Presentations"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_settings_black_24dp, "Settings"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_launcher, "User Details"));
        adapter = new SlidingMenuAdapter(this, listSliding);
        listViewSliding.setAdapter(adapter);

        //Display icon to open/ close sliding list
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Set title
        setTitle(listSliding.get(0).getTitle());
        //Item Selected
        listViewSliding.setItemChecked(0, true);
        //Close menu
        drawerLayout.closeDrawer(listViewSliding);

        //Display fragment 1 when start
        replaceFragment(0);

        //Handle on item click

        listViewSliding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Set title
                setTitle(listSliding.get(position).getTitle());
                //item selected
                listViewSliding.setItemChecked(position, true);
                //Replace fragment
                replaceFragment(position);
                //Close menu
                drawerLayout.closeDrawer(listViewSliding);
            }
        });
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_opened, R.string.drawer_closed) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        try {
            String userID = "1";

//            modules = new ArrayList<>();
//            getModules(userID);

//            sendInteraction(3, 1, "Testing interaction");
//
//            for (Module module: modules) {
//                for (Presentation presentation: module.getPresentations()) {
//                    downloadPresentation(presentation);
//                }
//            }
//
//            interactiveElements = new ArrayList<>();
//            getInteractiveElements("1");
//
//            interactions = new ArrayList<>();
//            getInteractions("1");
//
//              questions = new ArrayList<>();
//              getQuestions("1");

        } catch (Exception e) {
            System.out.println("Bla bla bla it crashed");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //can be empty
    }

    //Create method replace fragment

    private void replaceFragment(int pos) {
        //Fragment fragment = null;
        switch (pos) {
            case 0:
                fragment = new PresentationListFragment();
                break;
            case 1:
                fragment = new Fragment2();
                break;
            case 2:
                fragment = new UserFragment();
                break;
            default:
                fragment = new PresentationListFragment();
                break;
        }
        if (null != fragment) {
            FragmentManager fragmentManager = getFragmentManager();
            android.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_content, fragment);
            transaction.commit();
        }
    }

    public void joinPresentation(View view) {
        Intent intent = new Intent(fragment.getActivity(), PresentationActivity.class);
        startActivity(intent);
    }

    //populates the modules array list also it gets the info for the presentations
    private void getModules(String userID) throws Exception {

        int SDK_INT = Build.VERSION.SDK_INT;
        // >SDK 8 support async operations
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            //connect client
            SocketClient socketClient = new SocketClient();

            //clear the modules first
            modules.clear();
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
                throw new Exception();
            }
        } else {
            //for debug
            System.out.println("There was an error. SDK too old");
            throw new Exception();
        }
    }

    //TODO not needed here, but may be useful somewhere else
    private void getPresentation(String forUserID) throws Exception {

        int SDK_INT = Build.VERSION.SDK_INT;
        // >SDK 8 support async operations
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            //connect client
            SocketClient socketClient = new SocketClient();

            //clear the presentation first
            presentations.clear();
            presentations = socketClient.getPresentationsForUserId(forUserID);

            if (!presentations.isEmpty()) {
                //for debug
                System.out.println("YAY I have all the presentations for userID: " + forUserID);

                for (Presentation presentation : presentations) {
                    System.out.println("ID: " + presentation.getPresentationID() + " ModuleID: " + presentation.getModuleID() + " Subject: " + presentation.getXmlURL());
                }
            } else {
                //for debug
                System.out.println("There was an error from getting the presentations from server");
                throw new Exception();
            }
        } else {
            //for debug
            System.out.println("There was an error. SDK too old");
            throw new Exception();
        }
    }

    //Method used, when user goes to a presentation and we need to know where on the presentation (slinde_number),
    //the interactive elements are
    private void getInteractiveElements(String presentationID) throws Exception {
        int SDK_INT = Build.VERSION.SDK_INT;
        // >SDK 8 support async operations
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            //connect client
            SocketClient socketClient = new SocketClient();

            //clear the Interactive Elements first
            interactiveElements.clear();
            interactiveElements = socketClient.getInteractiveElements(presentationID);

            if (!interactiveElements.isEmpty()) {
                //for debug
                System.out.println("YAY I have all the interactive elements for userID: " + presentationID);

                InteractiveElement dummyInteractiveElement = new InteractiveElement();
                for (InteractiveElement elem : interactiveElements) {
                    System.out.println("ID: " + elem.getInteractiveElementID() + " Data: " + elem.getInteractiveElementData() + " Type: " + elem.getType() + " slide number: "
                            + elem.getSlideNumber());
                }
            } else {
                //for debug
                System.out.println("There was an error from getting the interactive elements from server");
                throw new Exception();
            }
        } else {
            //for debug
            System.out.println("There was an error. SDK too old");
            throw new Exception();
        }
    }

    private void sendQuestion(int userID, int presentationID, String questionData, int slideNumber) throws Exception {
        int SDK_INT = Build.VERSION.SDK_INT;
        // >SDK 8 support async operations
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            //connect client
            SocketClient socketClient = new SocketClient();
            Boolean success = socketClient.postQuestion(userID,presentationID, questionData, slideNumber);

            if (success) {
                //for debug
                System.out.println("YAY the question was successfully sent");
            } else {
                //for debug
                System.out.println("There was an error sending the question to server");
            }
        } else {
            //for debug
            System.out.println("There was an error. SDK too old");
            throw new Exception();
        }
    }

    private void sendInteraction(int userID, int interactiveElementID, String interactionData) throws Exception {
        int SDK_INT = Build.VERSION.SDK_INT;
        // >SDK 8 support async operations
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            //connect client
            SocketClient socketClient = new SocketClient();
            String status = socketClient.postInteraction(userID, interactiveElementID, interactionData);

            if (status.equals("success")) {
                //for debug
                System.out.println("YAY the question was successfully sent");
            } else {
                //for debug
                System.out.println("There was an error sending the question to server: " + status);
            }
        } else {
            //for debug
            System.out.println("There was an error. SDK too old");
            throw new Exception();
        }
    }

    //TODO not needed here, but may be useful somewhere else
    private void getInteractions(String interactiveElementID) throws Exception {
        int SDK_INT = Build.VERSION.SDK_INT;
        // >SDK 8 support async operations
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            //connect client
            SocketClient socketClient = new SocketClient();

            //clear the Interactive Elements first
            interactions.clear();
            interactions = socketClient.getInteractions(interactiveElementID);

            if (!interactions.isEmpty()) {
                //for debug
                System.out.println("YAY I have all the interactive elements for userID: " + interactiveElementID);

                for (Interaction elem : interactions) {
                    System.out.println("ID: " + elem.getInteractionID() + " Interactive Elem ID: " + elem.getInteractiveElementID()
                            + " User: " + elem.getUserID() + " Time: " + elem.getTimeCreated());
                }
            } else {
                //for debug
                System.out.println("There was an error from getting the interactive elements from server");
                throw new Exception();
            }
        } else {
            //for debug
            System.out.println("There was an error. SDK too old");
            throw new Exception();
        }
    }

    //TODO not needed here, but may be useful somewhere else
    private void getQuestions(String presentationID) throws Exception {
        int SDK_INT = Build.VERSION.SDK_INT;
        // >SDK 8 support async operations
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            //connect client
            SocketClient socketClient = new SocketClient();

            //clear the Interactive Elements first
            questions.clear();
            questions = socketClient.getQuestions(presentationID);

            if (!questions.isEmpty()) {
                //for debug
                System.out.println("YAY I have all the interactive elements for userID: " + presentationID);

                for (Question  question: questions) {
                    System.out.println("ID: " + question.getQuestionID() + " Presentation ID: " + question.getPresentationID()
                            + " User: " + question.getUserID() + " Time: " + question.getDateCreated());
                }
            } else {
                //for debug
                System.out.println("There was an error from getting the interactive elements from server");
                throw new Exception();
            }
        } else {
            //for debug
            System.out.println("There was an error. SDK too old");
            throw new Exception();
        }
    }

    public void downloadPresentation(Presentation presentation) {

        //the path if is set only when a succesfull unzip is made
        if (presentation.getFolderPath() != null) {

            FileOutputStream fos;
            String filename = "Presentation_" + presentation.getPresentationID();
            try {
                URL website = presentation.getXmlURL();
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                fos = openFileOutput(filename + ".zip", Context.MODE_PRIVATE);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

                System.out.println("Download succesfull");
                rbc.close();
                fos.close();
            } catch (IOException e) {
                System.out.println("Error:" + e);
                e.printStackTrace();
            }

            unzipPresentation(filename + ".zip", filename + "_folder", presentation);
        }
    }

    public void unzipPresentation(String zipFile, String outputFolder, Presentation presentation) {
        byte[] buffer = new byte[1024];
        String basePath = getFilesDir().getAbsolutePath() + "/";

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
                if (fileName.contains("CSS") || fileName.contains("__MACOSX/") || fileName.contains("/.DS_Store")) {
                    ze = zis.getNextEntry();
                    continue;
                }

                String tmpFileName = outputFolder + File.separator + fileName;
                File newFile = new File( basePath + tmpFileName);

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

    //server stuff


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
                //go back to homeActivity, if the presentation is not liver anymore
                SocketClient mySocketClient = new SocketClient();

                ArrayList<Module> modules = mySocketClient.getModules("1");

                livePresentations.clear();

                for(Module module: modules) {
                    for (Presentation presentation : module.getPresentations()){
                        if(presentation.isLive()) {
                            livePresentations.add(presentation);
                            System.out.println(" Presentation: " + presentation.getPresentationID() + "is live");
                        }
                    }
                }
                break;
            default:
                System.out.println("Other table than interactive_elements was updated");
                break;
        }
    }

    @Override
    protected void onResume() {
        //connect client
        serverIPAddress = Utils.buildIPAddress("db.amriksadhra.com", 8080);
        connectToRemoteSocket();
        super.onResume();
    }

    @Override
    protected void onPause() {
        socket.disconnect(); //to avoid having issues with other instances of socketClient
        super.onPause();
    }

    //    public void joinPresentation(View view) {
//        Intent intent = new Intent(this, InitialPresentationActivity.class);
//        startActivity(intent);
//    }
}
