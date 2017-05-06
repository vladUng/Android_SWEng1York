package com.example.i2lc.edi.backend;


import java.net.URISyntaxException;
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

public class socketClient {
    //Timeout times for user addition/authorisation asynchronous functions
    private static final int LOGIN_TIMEOUT = 5;
    private static final int ADDITION_TIMEOUT = 5;

    //private Logger logger = LoggerFactory.getLogger(socketClient.class);
    private String serverIPAddress;

    //TODO: These will be filled by actual values, for now they are temp and meaningless
    private int current_presentation_id = 1;
    private int current_question_id = 1;

    Socket socket;
    private Connection conn;


    public void main(String[] args) { new socketClient("db.amriksadhra.com", 8080); }

    public socketClient(String serverIP, int serverPort) {
        serverIPAddress = Utils.buildIPAddress("db.amriksadhra.com", serverPort);

        connectToRemoteSocket();
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
            conn = DriverManager.getConnection(url, props);

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
        }).on("DB_Update", new Emitter.Listener()  {

            @Override
            public void call(Object... args) {

                System.out.println("Client knows DB has updated:  " + args[0]);
                //TODO add the code to deal with this scenario
            }
        });
    }


    //handling the authentication request
    //if successful returns the auth status and user type
    //otherwise error,error
    public ArrayList<String> userAuth(UserAuth toAuth) {

        ArrayList<String> retValue = new ArrayList<String>();

        Statement st = null;

        try {
            st = conn.createStatement();

            //TODO This should be parsed as an argument or to automatically handle this
            ArrayList<String> columns = new ArrayList<String>();
            columns.add("user_id");
            columns.add("user_type");
            columns.add("username");
            columns.add("first_name");
            columns.add("last_name");
            columns.add("email_address");
            columns.add("active_presentation_id");

            StringBuilder columnsSB = new StringBuilder();

            //construct the SB from the columns passed
            for (int idx=0; idx<columns.size()-1; idx++) {
                columnsSB.append(" " + columns.get(idx) + ",");
            }

            //last element doesn't have a , at the end
            columnsSB.append(" " + columns.get(columns.size()-1));

            StringBuilder query = new StringBuilder("select" + columnsSB + " from ");
            query.append("edi.public.sp_authuser(");
            query.append("'" + toAuth.getUserToLogin() + "',");
            query.append("'" + toAuth.getPassword() + "');");

            ResultSet queryResult = st.executeQuery(String.valueOf(query));

            String tmpString = new String();

            while (queryResult.next()) {

                for (int idx=0; idx<columns.size(); idx++) {

                    tmpString = queryResult.getString(columns.get(idx));

                    if (tmpString != null) {
                        System.out.println("For column " + columns.get(idx) + ", I got this:" + tmpString);
                        retValue.add(tmpString);
                    }
                }
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

        return retValue;
    }



}
