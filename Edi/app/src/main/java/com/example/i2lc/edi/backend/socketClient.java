package com.example.i2lc.edi.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by vlad on 06/04/2017.
 */

public class socketClient {
    //Timeout times for user addition/authorisation asynchronous functions
    private static final int LOGIN_TIMEOUT = 5;
    private static final int ADDITION_TIMEOUT = 5;

    private Logger logger = LoggerFactory.getLogger(socketClient.class);
    private String serverIPAddress;

    //TODO: These will be filled by actual values, for now they are temp and meaningless
    private int current_presentation_id = 1;
    private int current_question_id = 1;

    Socket socket;
    private Connection conn;


    public void main(String[] args) { new socketClient("127.0.0.1", 8080); }

    public socketClient(String serverIP, int serverPort) {
        serverIPAddress = Utils.buildIPAddress("127.0.0.1", serverPort);

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

            Statement st = conn.createStatement();

            StringBuilder query = new StringBuilder("SELECT (f).auth_result_return, (f).user_type_return FROM");
            query.append("(SELECT edi.public.sp_authuser(");
            query.append("'LoginName',");
            query.append("'password') as f) AS x;");

            try {

                ResultSet queryResult = st.executeQuery(String.valueOf(query));

                while (queryResult.next()) {
                    String authStatus  = queryResult.getString("user_type_return");
                    String userType = queryResult.getString("auth_result_return");

                    System.out.println("\n\n auth status: " + authStatus + " userType: " + userType + "\n\n");
                }


                queryResult.close();
                st.close();
            } catch (SQLException e) {
                System.out.print("There was a sql problem" + e.toString());
            } catch (Exception e) {
                System.out.print("There was an unknown problem" + e.toString());
            }


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









//
//        socket.on(Socket.EVENT_CONNECT, args -> logger.info("Client connected! Spitting bars.")).on("DB_Update", args -> {
//            System.out.println("Client knows DB has updated:  " + args[0]);
//
//            //TODO add the code to deal with this scenario
//            System.out.println("A table has been updated");
//        }).on(Socket.EVENT_DISCONNECT, args -> {
//
//        });
//
//        //Attempt Socket connection
//        socket.connect();
    }


    /**
     * Calls userAuthAsync function but with a LOGIN_TIMEOUT second timeout. If we hit timeout, return false, else wait for server
     * to respond with response.
     * @param toAuth User details to authenticate
     * @return Boolean corrsponding to whether authentication was successful or not
     * @author Amrik Sadhra
     */
    public String userAuth(UserAuth toAuth) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new UserAuthTask(toAuth));

        try {
            System.out.print("Attempting login of User:" + toAuth.getUserToLogin());
            return future.get(LOGIN_TIMEOUT, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            System.out.print("Connection to server timed out.");
        } catch (InterruptedException e) {
            System.out.print("Connection to server was interrupted.");
        } catch (ExecutionException e) {
            System.out.print("Connection to server failed. (tragically)");
        }

        //If any of tge catch statement was hit
        executor.shutdown();
        return "false";
    }


    class UserAuthTask implements Callable<String> {
        UserAuth toAuth;

        public UserAuthTask(UserAuth toAuth){
            this.toAuth = toAuth;
        }

        @Override
        public String call() throws Exception {
            return userAuthAsync(toAuth);
        }
    }

    public String userAuthAsync(UserAuth toAuth) {
        //Hack to bypass final requirement for Lambda anon methods
        final FinalWrapper loginSuccessFinal = new FinalWrapper("no_response");
//
//        JSONObject obj = new JSONObject();
//        try {
//            obj.put("userToLogin", toAuth.getUserToLogin());
//            obj.put("password", toAuth.getPassword());
//            socket.emit("AuthUser", obj);
//            socket.on("AuthUser", objects -> {
//                if (!(objects[0]).equals("auth_fail")) {
//                    System.out.print("User " + toAuth.getUserToLogin() + " has successfully logged in");
//                } else {
//                    System.out.print("Incorrect username/password for login.");
//                }
//
//                loginSuccessFinal.setNonFinal(objects[0]);
//            });
//        } catch (JSONException e) {
//            System.out.print("Unable to generate JSON object for passing user authentication details. ");
//        }

        //Spinlock method until our final fake boolean has been modified
        while(loginSuccessFinal.getNonFinal().equals("no_response")){
            System.out.print("JVM optimises out empty while loops. Waiting for server response.");
        }

        //Convert the string we used to store 3 data types in (no resp/true/false) down to boolean now that no resp has been removed as possibility
        return (String) loginSuccessFinal.getNonFinal();
    }
}
