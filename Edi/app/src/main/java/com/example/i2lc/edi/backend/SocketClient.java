package com.example.i2lc.edi.backend;


import com.example.i2lc.edi.dbClasses.InteractiveElement;
import com.example.i2lc.edi.dbClasses.Module;
import com.example.i2lc.edi.dbClasses.Presentation;

import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by vlad on 06/04/2017.
 */

public class SocketClient {
    //Timeout times for user addition/authorisation asynchronous functions
    private static final int LOGIN_TIMEOUT = 5;
    private static final int ADDITION_TIMEOUT = 5;

    //private Logger logger = LoggerFactory.getLogger(SocketClient.class);
    private String serverIPAddress;

    //TODO: These will be filled by actual values, for now they are temp and meaningless
    private int current_presentation_id = 1;
    private int current_question_id = 1;

    Socket socket;
    private Connection connection;

    public void main(String[] args) {
        new SocketClient("db.amriksadhra.com", 8080);
    }

    public SocketClient(String serverIP, int serverPort) {
        serverIPAddress = Utils.buildIPAddress("db.amriksadhra.com", serverPort);

        connectToRemoteSocket();
        connectToRemoteDB();
    }

    public SocketClient() {
        connectToRemoteDB();
    }

    public void connectToRemoteDB() {
        //Connect to PostgresSQL Instance

        String url = "jdbc:postgresql://db.amriksadhra.com:5432/edi";
        Properties props = new Properties();
        props.setProperty("user", "iilp");
        props.setProperty("password", "group1SWENG");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            DriverManager.setLoginTimeout(5);
            connection = DriverManager.getConnection(url, props);

            System.out.print("Successful connection from client to PostgreSQL database instance \n");

        } catch (SQLException e) {
            System.out.print("Unable to connect to PostgreSQL on port 5432");
            System.out.print(e.toString());
            e.printStackTrace();
        }
    }

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
                //TODO understand this part. I think this is used when you want to send some data to the server (?)
                socket.emit("foo", "hi");
                socket.disconnect();
            }

        }).on("event", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                System.out.println("Client connected! Spitting bars.");
            }

        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                System.out.println("For some reason the client is disconnected from the server. Some more info:" + args.toString());
            }
        }).on("DB_Update", new Emitter.Listener() {

            @Override
            public void call(Object... args) {

                System.out.println("Client knows DB has updated:  " + args[0]);
                //TODO add the code to deal with this scenario
            }
        });
    }

    public ArrayList<String> userAuth(UserAuth toAuth) {

        ArrayList<String> retValue = new ArrayList<String>();

        Statement st = null;

        try {
            st = connection.createStatement();

            //get the query fields for the sql statement
            QueryFields queryFields = new QueryFields("User");
            StringBuilder fieldsSB = queryFields.getSb();
            ArrayList<String> fieldsList = queryFields.getFields();

            //build the sql statement
            StringBuilder query = new StringBuilder("select" + fieldsSB + " from ");
            query.append("edi.public.sp_authuser(");
            query.append("'" + toAuth.getUserToLogin() + "',");
            query.append("'" + toAuth.getPassword() + "');");

            ResultSet queryResult = st.executeQuery(String.valueOf(query));

            String tmpString = new String();

            //go through the query result
            while (queryResult.next()) {

                //create an ArrayList of strings, that stores the fields from a row
                for (int idx = 0; idx < fieldsList.size(); idx++) {
                    tmpString = queryResult.getString(fieldsList.get(idx));
                    if (tmpString != null) {
                        retValue.add(tmpString);
                    }
                }
            }

            queryResult.close();
            st.close();

            //close connection
            connection.close();
        } catch (SQLException e) {
            System.out.print("SQL query is wrong" + e.toString());
            e.printStackTrace();

        } catch (Exception e) {
            System.out.print("There was an unknown problem" + e.toString());
            e.printStackTrace();
        }

        return retValue;
    }

    //returns all the modules in which a user is involved including the presentations for each module
    public ArrayList<Module> getModules(String forUserId) {

        ArrayList<Module> retModules = new ArrayList<Module>();
        Statement st = null;

        try {
            st = connection.createStatement();

            //get the query fields for the sql statement
            QueryFields queryFields = new QueryFields("Module");
            StringBuilder fieldsSB = queryFields.getSb();
            ArrayList<String> fieldsList = queryFields.getFields();

            //build the sql statement
            StringBuilder query = new StringBuilder("select" + fieldsSB + " from ");
            query.append("edi.public.sp_getmodulesforuser(");
            query.append("'" + forUserId + "');");

            ResultSet queryResult = st.executeQuery(String.valueOf(query));

            String tmpString = new String();
            ArrayList<String> rowString = new ArrayList<String>();

            ArrayList<Presentation> dummyPresentation = new ArrayList<Presentation>();
            Module dummyModule = new Module();

            //go through the query result
            while (queryResult.next()) {
                rowString.clear();

                //create an ArrayList of strings, that stores the fields from a row
                for (int idx = 0; idx < fieldsList.size(); idx++) {
                    tmpString = queryResult.getString(fieldsList.get(idx));
                    if (tmpString != null) {
                        rowString.add(tmpString);
                    }
                }

                //int moduleID,  String moduleName, String subject, String description, String timeLastUpdate, String timeCreated
                dummyModule = (new Module(Integer.parseInt(rowString.get(0)), rowString.get(1), rowString.get(2),
                        rowString.get(3), rowString.get(4), rowString.get(5)));
                retModules.add(dummyModule);

                //get all the presentations
                dummyPresentation = getPresentationsForModuleId(String.valueOf(dummyModule.getModuleID()));
                retModules.get(retModules.size() - 1).setPresentations(dummyPresentation);
            }

            queryResult.close();
            st.close();

            //close connection
            connection.close();
        } catch (SQLException e) {
            System.out.print("SQL query is wrong" + e.toString());
            e.printStackTrace();

        } catch (Exception e) {
            System.out.print("There was an unknown problem" + e.toString());
            e.printStackTrace();
        }

        return retModules;
    }

    public ArrayList<Presentation> getPresentationsForUserId(String userId) {

        ArrayList<Presentation> retPresentations = new ArrayList<Presentation>();
        Statement st = null;

        try {

            st = connection.createStatement();

            //get the query fields for the sql statement
            QueryFields queryFields = new QueryFields("Presentation");
            StringBuilder fieldsSB = queryFields.getSb();
            ArrayList<String> fieldsList = queryFields.getFields();

            //build the sql statement
            StringBuilder query = new StringBuilder("select" + fieldsSB + " from ");
            query.append("edi.public.sp_getpresentationsforuser(");
            query.append("'" + userId + "');");

            ResultSet queryResult = st.executeQuery(String.valueOf(query));

            String tmpString = new String();
            ArrayList<String> rowString = new ArrayList<String>();

            //go through the query results
            while (queryResult.next()) {
                rowString.clear();

                //create an ArrayList of strings, that stores the fields from a row
                for (int idx = 0; idx < fieldsList.size(); idx++) {
                    tmpString = queryResult.getString(fieldsList.get(idx));
                    if (tmpString != null) {
                        rowString.add(tmpString);
                    }
                }

                //int presentationID, int moduleID, URL xmlURL, boolean live
                retPresentations.add(new Presentation(Integer.parseInt(rowString.get(0)), Integer.parseInt(rowString.get(1)),
                        new URL(rowString.get(2)), Boolean.valueOf(rowString.get(3))));
            }

            queryResult.close();
            st.close();

            //close connection
            connection.close();
        } catch (SQLException e) {
            System.out.print("SQL query is wrong" + e.toString());
            e.printStackTrace();

        } catch (Exception e) {
            System.out.print("There was an unknown problem" + e.toString());
            e.printStackTrace();
        }

        return retPresentations;
    }

    public ArrayList<Presentation> getPresentationsForModuleId(String moduleId) {

        ArrayList<Presentation> retPresentations = new ArrayList<Presentation>();
        Statement st = null;

        try {

            st = connection.createStatement();

            //get the query fields for the sql statement
            QueryFields queryFields = new QueryFields("Presentation");
            StringBuilder fieldsSB = queryFields.getSb();
            ArrayList<String> fieldsList = queryFields.getFields();

            //build the sql statement
            StringBuilder query = new StringBuilder("select" + fieldsSB + " from ");
            query.append("edi.public.sp_getpresentationsformodule(");
            query.append("'" + moduleId + "');");

            ResultSet queryResult = st.executeQuery(String.valueOf(query));

            String tmpString = new String();
            ArrayList<String> rowString = new ArrayList<String>();

            //go through the query results
            while (queryResult.next()) {
                rowString.clear();

                //create an ArrayList of strings, that stores the fields from a row
                for (int idx = 0; idx < fieldsList.size(); idx++) {
                    tmpString = queryResult.getString(fieldsList.get(idx));
                    if (tmpString != null) {
                        rowString.add(tmpString);
                    }
                }

                //public Presentation(int presentationID, int moduleID, URL xmlURL, boolean live)
                retPresentations.add(new Presentation(Integer.parseInt(rowString.get(0)), Integer.parseInt(rowString.get(1)),
                        new URL(rowString.get(2)), Boolean.valueOf(rowString.get(3))));
            }

            queryResult.close();
            st.close();
        } catch (SQLException e) {
            System.out.print("SQL query is wrong" + e.toString());
            e.printStackTrace();

        } catch (Exception e) {
            System.out.print("There was an unknown problem" + e.toString());
            e.printStackTrace();
        }

        return retPresentations;
    }

    public ArrayList<InteractiveElement> getInteractiveElements(String presentationID) {

        ArrayList<InteractiveElement> retInteractiveElements = new ArrayList<InteractiveElement>();
        Statement st = null;

        try {

            st = connection.createStatement();

            //get the query fields for the sql statement
            QueryFields queryFields = new QueryFields("InteractiveElement");
            StringBuilder fieldsSB = queryFields.getSb();
            ArrayList<String> fieldsList = queryFields.getFields();

            //build the sql statement
            StringBuilder query = new StringBuilder("select" + fieldsSB + " from ");
            query.append("edi.public.sp_getinteractiveelementsforpresentation(");
            query.append("'" + presentationID + "');");

            ResultSet queryResult = st.executeQuery(String.valueOf(query));

            String tmpString = new String();
            ArrayList<String> rowString = new ArrayList<String>();

            //go through the query results
            while (queryResult.next()) {
                rowString.clear();

                //create an ArrayList of strings, that stores the fields from a row
                for (int idx = 0; idx < fieldsList.size(); idx++) {
                    tmpString = queryResult.getString(fieldsList.get(idx));
                    if (tmpString != null) {
                        rowString.add(tmpString);
                    }
                }

                // int interactiveElementID, int presentationID, String interactiveElementData,
                // String type, boolean live, int slideNumber, String responsesInterval)
                retInteractiveElements.add(new InteractiveElement(Integer.valueOf(rowString.get(0)), Integer.valueOf(rowString.get(1)),
                                                rowString.get(2), rowString.get(3), Boolean.valueOf(rowString.get(4)),
                                                Integer.valueOf(rowString.get(5)), rowString.get(6)));
            }

            queryResult.close();
            st.close();
        } catch (SQLException e) {
            System.out.print("SQL query is wrong" + e.toString());
            e.printStackTrace();

        } catch (Exception e) {
            System.out.print("There was an unknown problem" + e.toString());
            e.printStackTrace();
        }

        return retInteractiveElements;
    }

}
