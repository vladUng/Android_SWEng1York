package com.example.i2lc.edi.backend;


import com.example.i2lc.edi.dbClasses.Interaction;
import com.example.i2lc.edi.dbClasses.InteractiveElement;
import com.example.i2lc.edi.dbClasses.Module;
import com.example.i2lc.edi.dbClasses.Presentation;
import com.example.i2lc.edi.dbClasses.Question;

import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by vlad on 06/04/2017.
 */

public class SocketClient {
    private String serverIPAddress;


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
               System.out.println("Connected to socket");
            }

        });

        socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                System.out.println("For some reason the client is disconnected from the server. Some more info:" + Arrays.toString(args));
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

    public void disconnect(){
        socket.disconnect();
    }

    public void updateLocalTables(Object tableToUpdate) {

        //SocketIO will pass a generic object. But we know its a string because that's what DB_notify returns from com.i2lp.edi.server side
        switch ((String) tableToUpdate) {
            case "interactions":
                break;

            case "interactive_elements":
                break;

            case "users":
                break;

            case "presentations":
                //Update presentation list whilst no presentation is live
                break;

            case "jnct_users_modules":
                break;

            case "modules":
                break;

            case "questions":
                break;
        }
    }

    public ArrayList<String> userAuth(UserAuth toAuth) {

        ArrayList<String> retValue = new ArrayList<>();

        Statement st;

        try {
            st = connection.createStatement();

            //get the query fields for the sql statement
            QueryFields queryFields = new QueryFields("User");
            StringBuilder fieldsSB = queryFields.getSb();
            ArrayList<String> fieldsList = queryFields.getFields();

            //build the sql statement
            StringBuilder query = new StringBuilder("select" + fieldsSB + " from ");
            query.append("edi.public.sp_authuser(");
            query.append("'").append(toAuth.getUserToLogin()).append("',");
            query.append("'").append(toAuth.getPassword()).append("');");

            ResultSet queryResult = st.executeQuery(String.valueOf(query));

            String tmpString;

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

    //returns all the modules in which a user is involved including the presentations for each moduleName
    public ArrayList<Module> getModules(String forUserId) {

        ArrayList<Module> retModules = new ArrayList<>();
        Statement st;

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

            ArrayList<Presentation> dummyPresentation;
            Module dummyModule;

            //go through the query result
            while (queryResult.next()) {
                //int moduleID,  String moduleName, String subject, String description, Date timeLastUpdate, Date timeCreated
                dummyModule = new Module( queryResult.getInt(fieldsList.get(0)), queryResult.getString(fieldsList.get(1)),
                        queryResult.getString(fieldsList.get(2)), queryResult.getString(fieldsList.get(3)),
                        queryResult.getTimestamp(fieldsList.get(4)), queryResult.getTimestamp(fieldsList.get(5)));
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

    public ArrayList<Presentation> getPresentationsForUserId(String userID) {

        ArrayList<Presentation> retPresentations = new ArrayList<>();
        Statement st;

        try {

            st = connection.createStatement();

            //get the query fields for the sql statement
            QueryFields queryFields = new QueryFields("Presentation");
            StringBuilder fieldsSB = queryFields.getSb();
            ArrayList<String> fieldsList = queryFields.getFields();

            //build the sql statement
            StringBuilder query = new StringBuilder("select" + fieldsSB + " from ");
            query.append("edi.public.sp_getpresentationsforuser(");
            query.append("'" + userID + "');");

            ResultSet queryResult = st.executeQuery(String.valueOf(query));

            String tmpString;
            ArrayList<String> rowString = new ArrayList<>();

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
                Boolean isLive = false;
                if (rowString.get(3).contains("t")) {
                    isLive = true;
                }

                Presentation dummyPresentation = new Presentation(Integer.parseInt(rowString.get(0)), Integer.parseInt(rowString.get(1)),
                        new URL(rowString.get(2)), isLive);

                if (rowString.size() == 5) {
                    dummyPresentation.setCurrentSlideNumber(Integer.valueOf(rowString.get(4)));
                }
                retPresentations.add(dummyPresentation);
            }

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

    public ArrayList<Presentation> getPresentationsForModuleId(String moduleID) {

        ArrayList<Presentation> retPresentations = new ArrayList<>();
        Statement st;

        try {

            st = connection.createStatement();

            //get the query fields for the sql statement
            QueryFields queryFields = new QueryFields("Presentation");
            StringBuilder fieldsSB = queryFields.getSb();
            ArrayList<String> fieldsList = queryFields.getFields();

            //build the sql statement
            StringBuilder query = new StringBuilder("select" + fieldsSB + " from ");
            query.append("edi.public.sp_getpresentationsformodule(");
            query.append("'").append(moduleID).append("');");

            ResultSet queryResult = st.executeQuery(String.valueOf(query));

            String tmpString;
            ArrayList<String> rowString = new ArrayList<>();

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
                Boolean isLive = false;
                if (rowString.get(3).contains("t")) {
                    isLive = true;
                }

                Presentation dummyPresentation = new Presentation(Integer.parseInt(rowString.get(0)), Integer.parseInt(rowString.get(1)),
                        new URL(rowString.get(2)), isLive);

                if (rowString.size() == 5) {
                    dummyPresentation.setCurrentSlideNumber(Integer.valueOf(rowString.get(4)));
                }
                retPresentations.add(dummyPresentation);
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

        ArrayList<InteractiveElement> retInteractiveElements = new ArrayList<>();
        Statement st;

        try {

            st = connection.createStatement();

            //get the query fields for the sql statement
            QueryFields queryFields = new QueryFields("InteractiveElement");
            StringBuilder fieldsSB = queryFields.getSb();
            ArrayList<String> fieldsList = queryFields.getFields();

            //build the sql statement
            StringBuilder query = new StringBuilder("select" + fieldsSB + " from ");
            query.append("edi.public.sp_getinteractiveelementsforpresentation(");
            query.append("'").append(presentationID).append("');");

            ResultSet queryResult = st.executeQuery(String.valueOf(query));

            //go through the query results
            while (queryResult.next()) {

                // int interactiveElementID, int presentationID, String interactiveElementData,
                // String type, boolean live, int slideNumber, Timestamp responsesInterval)
                retInteractiveElements.add(new InteractiveElement(queryResult.getInt(fieldsList.get(0)), queryResult.getInt(fieldsList.get(1)),
                                                                    queryResult.getString(fieldsList.get(2)), queryResult.getString(fieldsList.get(3)),
                                                                    queryResult.getBoolean(fieldsList.get(4)), queryResult.getInt(fieldsList.get(5)),
                                                                    queryResult.getInt(fieldsList.get(6))));
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

    //Needed on the Java Client -- accidentally did it here
    public ArrayList<Interaction> getInteractions(String interactiveElementID) {

        ArrayList<Interaction> retInteractions = new ArrayList<>();
        Statement st;

        try {

            st = connection.createStatement();

            //get the query fields for the sql statement
            QueryFields queryFields = new QueryFields("Interaction");
            StringBuilder fieldsSB = queryFields.getSb();
            ArrayList<String> fieldsList = queryFields.getFields();

            //build the sql statement
            StringBuilder query = new StringBuilder("select" + fieldsSB + " from ");
            query.append("edi.public.sp_getinteractionsforinteractiveelement(");
            query.append("'").append(interactiveElementID).append("');");

            ResultSet queryResult = st.executeQuery(String.valueOf(query));

            //go through the query results
            while (queryResult.next()) {

                // int interactionID, int userID, int interactiveElementID, String Interaction Data, Date timeCreated
                retInteractions.add(new Interaction(queryResult.getInt(fieldsList.get(0)), queryResult.getInt(fieldsList.get(1)),
                                    queryResult.getInt(fieldsList.get(2)), queryResult.getString(fieldsList.get(3)),
                                    queryResult.getTimestamp(fieldsList.get(4)) ));
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

        return retInteractions;
    }

    //Needed on the Java Client -- accidentally did it here
    public ArrayList<Question> getQuestions(String presentationID) {
        ArrayList<Question> retQuestions = new ArrayList<>();
        Statement st;

        try {

            st = connection.createStatement();

            //get the query fields for the sql statement
            QueryFields queryFields = new QueryFields("Question");
            StringBuilder fieldsSB = queryFields.getSb();
            ArrayList<String> fieldsList = queryFields.getFields();

            //build the sql statement
            StringBuilder query = new StringBuilder("select" + fieldsSB + " from ");
            query.append("edi.public.sp_getquestionsforpresentation(");
            query.append("'").append(presentationID).append("');");

            ResultSet queryResult = st.executeQuery(String.valueOf(query));

            //go through the query results
            while (queryResult.next()) {

                //int questionID, int userID, int presentationID, Timestamp dateCreated, String questionData, int slideNumber
                //NOTE there is NO field for the time_answered !!!!
                retQuestions.add(new Question(queryResult.getInt(fieldsList.get(0)), queryResult.getInt(fieldsList.get(1)), queryResult.getInt(fieldsList.get(2)),
                            queryResult.getTimestamp(fieldsList.get(3)), queryResult.getString(fieldsList.get(5)), queryResult.getInt(fieldsList.get(6))));
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

        return retQuestions;
    }

    public boolean postQuestion(int userID, int presentationID, String questionData, int slideNumber) {
        Boolean retStatus = false;

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM public.sp_addquestion_to_questionqueue(?, ?, ?, ?);");

            //Fill prepared statements to avoid SQL injection
            statement.setInt(1, userID);
            statement.setInt(2, presentationID);
            statement.setString(3, questionData);
            statement.setInt(4, slideNumber);

            //Call stored procedure on database
            ResultSet rs = statement.executeQuery();

            String status = "failure";

            while (rs.next()) {
                status = rs.getString(1);
            }

            if (status.equals("success")){
                retStatus = true;
                System.out.print("Successfully added question to question queue.");
            }
            else System.out.print("Unable to add question: " + status);

            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.print("SQL query is wrong" + e.toString());
            e.printStackTrace();

        } catch (Exception e) {
            System.out.print("There was an unknown problem" + e.toString());
            e.printStackTrace();
        }

        return retStatus;
    }

    public String postInteraction(int userID, int interactiveElementID, String interactionData) {
        String retString = "failure";

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM public.sp_addinteraction_to_interactiveelemnt(?, ?, ?);");

            //Fill prepared statements to avoid SQL injection
            statement.setInt(1, userID);
            statement.setInt(2, interactiveElementID);
            statement.setString(3, interactionData);

            //Call stored procedure on database
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                retString = rs.getString(1);
            }


            switch (retString) {
                case "success":
                    System.out.print("Successfully added question to question queue.");
                    break;
                case "failure: Interaction already exists":
                    System.out.println(retString);
                    break;
                default:
                    System.out.print("Unable to add question: " + retString);
                    break;
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.print("SQL query is wrong" + e.toString());
            e.printStackTrace();

        } catch (Exception e) {
            System.out.print("There was an unknown problem" + e.toString());
            e.printStackTrace();
        }

        return retString;
    }

    //set presentationID to 0, to toggle off
    public boolean toggleUserActivePresentation(int presentationID, int userID){
        Boolean retStatus;

        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE users SET active_presentation_id = ? WHERE user_id = ?;");

            //If set to no presentation
            if (presentationID == 0) {
                statement.setNull(1, 0);
                statement.setInt(2, userID);
            } else {
                //Fill prepared statements to avoid SQL injection
                statement.setInt(1, presentationID);
                statement.setInt(2, userID);
            }

            retStatus = statement.execute();

            if (retStatus) {
                System.out.println("Unable to set active presentation for user.");
            } else {
                System.out.println("User: " + userID + " is now active in presentation: " + presentationID);
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.print("SQL query is wrong" + e.toString());
            e.printStackTrace();

        } catch (Exception e) {
            System.out.print("There was an unknown problem" + e.toString());
            e.printStackTrace();
        }

        return false;
    }

    public Presentation getPresentation(int forPresentationID) {
        Presentation retPresentation = new Presentation();

        try {

            //get the query fields for the sql statement
            QueryFields queryFields = new QueryFields("Presentation");
            StringBuilder fieldsSB = queryFields.getSb();
            ArrayList<String> fieldsList =  queryFields.getFields();

            StringBuilder query = new StringBuilder("select" + fieldsSB + " from ");
            query.append("edi.public.presentations ");
            query.append("where presentation_id = ?;");

            PreparedStatement statement = connection.prepareStatement(query.toString());

            //Fill prepared statements to avoid SQL injection
            statement.setInt(1, forPresentationID);

            //Call stored procedure on database
            ResultSet queryResult = statement.executeQuery();

            String tmpString;
            ArrayList<String> rowString = new ArrayList<>();

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
                Boolean isLive = false;
                if (rowString.get(3).contains("t")) {
                    isLive = true;
                }
                retPresentation = new Presentation(Integer.parseInt(rowString.get(0)), Integer.parseInt(rowString.get(1)),
                        new URL(rowString.get(2)), isLive);

                if (rowString.size() == 5) {
                    retPresentation.setCurrentSlideNumber(Integer.valueOf(rowString.get(4)));
                }
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.print("SQL query is wrong" + e.toString());
            e.printStackTrace();

        } catch (Exception e) {
            System.out.print("There was an unknown problem" + e.toString());
            e.printStackTrace();
        }

        return retPresentation;
    }

}
