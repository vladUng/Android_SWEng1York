package com.example.i2lc.edi;


import android.app.Fragment;
import android.app.FragmentManager;
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
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.i2lc.edi.adapter.SlidingMenuAdapter;
import com.example.i2lc.edi.backend.DecompressFast;
import com.example.i2lc.edi.backend.SocketClient;
import com.example.i2lc.edi.backend.Utils;
import com.example.i2lc.edi.dbClasses.Interaction;
import com.example.i2lc.edi.dbClasses.InteractiveElement;
import com.example.i2lc.edi.dbClasses.Module;
import com.example.i2lc.edi.dbClasses.Presentation;
import com.example.i2lc.edi.dbClasses.Question;
import com.example.i2lc.edi.dbClasses.User;
import com.example.i2lc.edi.homeFragments.PresentationListFragment;
import com.example.i2lc.edi.homeFragments.Fragment2;
import com.example.i2lc.edi.homeFragments.UserFragment;
import com.example.i2lc.edi.model.ItemSlideMenu;
import com.example.i2lc.edi.utilities.ParserXML;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Cosmin on 15/03/2017.
 */

public class HomeActivity extends AppCompatActivity implements PresentationListFragment.OnFragmentInteractionListener, Fragment2.OnFragmentInteractionListener, UserFragment.OnFragmentInteractionListener, PresentationListFragment.GetPresentationListInterface,PresentationListFragment.GetUserInterface, UserFragment.GetUserInterface{

    private List<ItemSlideMenu> listSliding;
    private SlidingMenuAdapter adapter;
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ActionBar actionBar;
    private Fragment fragment;

    private ArrayList<Module> modules;
    private ArrayList<Presentation> livePresentations;

    //these are not needed in this activity, are used for debugging
    private ArrayList<InteractiveElement> interactiveElements;
    private ArrayList<Interaction> interactions;
    private ArrayList<Question> questions;
    private ArrayList<Presentation> presentations;

    //for establishing connection
    private Socket socket;
    private String serverIPAddress;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        user = intent.getExtras().getParcelable("user");
        if (user != null) {
            System.out.println("USER WITH ID = " + Integer.toString(user.getUserID()) + "just logged in.");
        }
        setContentView(R.layout.main_activity);

        livePresentations = new ArrayList<>();
        modules = new ArrayList<>();

        updatePresentationList();

        Toast.makeText(this, "You have successfully logged in!", Toast.LENGTH_LONG).show();
        createSlidingMenu();

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return true;
//    }

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

    public void createSlidingMenu(){
        //Intro Component
        listViewSliding = (ListView) findViewById(R.id.lv_sliding_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listSliding = new ArrayList<>();
        //Add item for sliding list
        listSliding.add(new ItemSlideMenu(R.drawable.presentation_icon, "Live Presentations"));
        //listSliding.add(new ItemSlideMenu(R.drawable.ic_settings_black_24dp, "Settings"));
        listSliding.add(new ItemSlideMenu(R.drawable.user_icon, "User Details"));
        adapter = new SlidingMenuAdapter(this, listSliding);
        listViewSliding.setAdapter(adapter);
        //Display icon to open/ close sliding list
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                | ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView imageView = new ImageView(actionBar.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.drawable.edi_small);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
                | Gravity.CENTER_VERTICAL);
        layoutParams.rightMargin = 40;
        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);
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
    }

    //Create method replace fragment
    private void replaceFragment(int pos) {
        //Fragment fragment = null;
        switch (pos) {
            case 0:
                fragment = new PresentationListFragment();
                break;
            case 1:
                fragment = new UserFragment();
                break;
            case 2:
                fragment = new Fragment2();
                break;
            default:
                fragment = new PresentationListFragment();
                break;
        }

        FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_content, fragment);
        transaction.commit();

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

    public void downloadPresentation(Presentation presentation) {

        String filename = "Presentation_" + presentation.getPresentationID();
        String basePath = getFilesDir().getAbsolutePath() + "/";

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
        String zipFolderName =  basePath + filename +"_folder/";

        try {
            File destinationFolder = new File(zipFolderName);

            if (destinationFolder.exists()) {
                String deleteCmd = "sudo rm -r " + destinationFolder.getAbsolutePath();
                Runtime runtime = Runtime.getRuntime();
                try {
                    runtime.exec(deleteCmd);
                } catch (IOException e) { }
            }

//            //clear folder
//            if (destinationFolder.isDirectory()) {
//                String[] children = destinationFolder.list();
//                for (int i = 0; i < children.length; i++) {
//                    new File(destinationFolder, children[i]).delete();
//                }
//            }

            DecompressFast.unzip(new File(zipFileName), destinationFolder);
            System.out.println("Extracted to \n"+ zipFolderName);
            presentation.setFolderPath(destinationFolder.getAbsolutePath());
        } catch (ZipException e) {
            Log.e("Problems with zip", e.getMessage());
        } catch (IOException e) {
            Log.e("We got a problem", e.getMessage());
        }

        //Create folder
        File presentationFolder = new File(presentation.getFolderPath()); //
        //Create list of files
        File[] directoryListing = presentationFolder.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                //Check if file in directory is an xml file
                if (child.getAbsolutePath().contains(".xml")) {
                    ParserXML parser = new ParserXML(presentation, child);
                    livePresentations.add(parser.parsePresentation());
                }
            }

            //set thumbnail path
            for (File child: directoryListing){
                if (child.isDirectory() && child.getAbsolutePath().contains("Thumbnails")) {
                    File[] thumbnails = child.listFiles();
                    if (thumbnails != null) {
                        String thumbnailPath;
                        for (File thumbnail : thumbnails) {
                            thumbnailPath = thumbnail.getAbsolutePath();
                            if (!thumbnail.isHidden() && thumbnailPath.contains("slide0")) {
                                if(livePresentations.size() > 0) {
                                    livePresentations.get(livePresentations.size() - 1).setThumbnailPath(thumbnailPath);
                                }
                            }
                        }
                    }
                }
            }
        } else {
            System.out.println("Folder doesn't exist/is empty!");
        }
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
                System.out.println("For some reason the client is disconnected from the server. Some more info:" + args);
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

        System.out.println("Table: " + tableToUpdate + " has been updated on the server");
        //SocketIO will pass a generic object. But we know its a string because that's what DB_notify returns from com.i2lp.edi.server side
        switch ((String) tableToUpdate) {
            case "presentations":
                updatePresentationList();
                break;
            default:
                System.out.println("Other table than interactive_elements was updated");
                break;
        }
    }

    protected void updatePresentationList(){
        try {
            //if there are any elements clear moduleName array
            //TODO delete this as modules is already cleared in getModules
            if (modules != null) {
                modules.clear();
            }
            //if there are any elements clear livePresentation array
            if(livePresentations != null) {
                livePresentations.clear();
            }
            getModules(Integer.toString(user.getUserID()));
            for(Module module: modules) {
                for (Presentation presentation : module.getPresentations()) {
                    if(presentation.isLive()) {
                        System.out.println("Presentation " + Integer.toString(presentation.getPresentationID()) + " is live.");
                        presentation.setModuleName(module.getModuleName());
                        downloadPresentation(presentation);
                    }else{
                        System.out.println("Presentation " + Integer.toString(presentation.getPresentationID()) + " is not live.");
                    }
                }
            }
            replaceFragment(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //connect client
        serverIPAddress = Utils.buildIPAddress("db.amriksadhra.com", 8080);
        connectToRemoteSocket();
        updatePresentationList();
    }

    @Override
    protected void onPause() {
        socket.disconnect(); //to avoid having issues with other instances of socketClient
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //when coming back check if a presentation live status has changed
    }
    //    public void joinPresentation(View view) {
//        Intent intent = new Intent(this, InitialPresentationActivity.class);
//        startActivity(intent);
//    }

    @Override
    public ArrayList<Presentation> getLivePresentationList() {
        return livePresentations;
    }
    @Override
    public User getUserInterface() {
        return user;
    }

    //    public void joinPresentation(View view) {
//        Intent intent = new Intent(fragment.getActivity(), PresentationActivity.class);
//        intent.putExtra("presentation", livePresentations);
//        startActivity(intent);
//    }

    //*************************** methods not use
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
                    System.out.println("ID: " + elem.getInteractiveElementID() + " Data: " + elem.getInteractiveElementQuestion() + " Type: " + elem.getType() + " slide number: "
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

}
