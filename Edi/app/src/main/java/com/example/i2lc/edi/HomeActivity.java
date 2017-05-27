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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.i2lc.edi.adapter.SlidingMenuAdapter;
import com.example.i2lc.edi.backend.SocketClient;
import com.example.i2lc.edi.dbClasses.Interaction;
import com.example.i2lc.edi.dbClasses.InteractiveElement;
import com.example.i2lc.edi.dbClasses.Module;
import com.example.i2lc.edi.dbClasses.Presentation;
import com.example.i2lc.edi.dbClasses.Question;
import com.example.i2lc.edi.fragment.Fragment1;
import com.example.i2lc.edi.fragment.Fragment2;
import com.example.i2lc.edi.fragment.Fragment3;
import com.example.i2lc.edi.model.ItemSlideMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cosmin on 15/03/2017.
 */

public class HomeActivity extends AppCompatActivity implements Fragment1.OnFragmentInteractionListener, Fragment2.OnFragmentInteractionListener, Fragment3.OnFragmentInteractionListener {

    private List<ItemSlideMenu> listSliding;
    private SlidingMenuAdapter adapter;
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ActionBar actionBar;
    private Fragment fragment;

    private ArrayList<Module> modules;
    private ArrayList<Presentation> presentations;

    //these are not needed in this activity
    private ArrayList<InteractiveElement> interactiveElements;
    private ArrayList<Interaction> interactions;
    private ArrayList<Question> questions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

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
            modules = new ArrayList<>();
            getModules(userID);

            interactiveElements = new ArrayList<>();
            getInteractiveElements("1");

            interactions = new ArrayList<>();
            getInteractions("1");

              questions = new ArrayList<>();
              getQuestions("1");

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
                fragment = new Fragment1();
                break;
            case 1:
                fragment = new Fragment2();
                break;
            case 2:
                fragment = new Fragment3();
                break;
            default:
                fragment = new Fragment1();
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
//    public void joinPresentation(View view) {
//        Intent intent = new Intent(this, InitialPresentationActivity.class);
//        startActivity(intent);
//    }

}
