package com.example.i2lc.edi;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.i2lc.edi.backend.SocketClient;
import com.example.i2lc.edi.backend.UserAuth;
import com.example.i2lc.edi.dbClasses.User;

import java.util.ArrayList;

public class LogInActivity extends AppCompatActivity {

    //TODO Change this to user instead of String
    public final static String EXTRA_USERNAME = "username";

    private boolean loginSuccessful;
    private boolean isTeacher;

    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }

    //called when the user clicks the log in button
    public void logIn(View view) {
        EditText usernameEditText = (EditText) findViewById(R.id.username);
        EditText passwordEditText = (EditText) findViewById(R.id.password);
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        try {
            username = "Student1"; //TODO: Remove username and password on finish
            password = "password";
            tryLogin(username, password);
            System.out.print("USER WITH ID = " + Integer.toString(user.getUserID()) + " IS LOGGING IN");
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        } catch (Exception e) {
            System.out.print("Error while performing login operation");
            Toast.makeText(this, "Username/Password Incorrect", Toast.LENGTH_LONG).show();
        }
    }

    public void tryLogin(String username, String password) throws Exception {

        //check if the fields contain white spaces {
        if (!username.contains(" ") || !password.contains(" ")) {

            int SDK_INT = Build.VERSION.SDK_INT;
            // >SDK 8 support async operations
            if (SDK_INT > 8) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                //connect client
                SocketClient mySocketClient;
                mySocketClient = new SocketClient();

                ArrayList<String> userAuthResponse = new ArrayList<String>();
                userAuthResponse = mySocketClient.userAuth(new UserAuth(username, password));


                if (!userAuthResponse.isEmpty()) {
                    //for debug
                    System.out.println("Login Successful");
                    //construct User obj
                    user = new User(Integer.parseInt(userAuthResponse.get(0)), userAuthResponse.get(1), userAuthResponse.get(2),
                            userAuthResponse.get(3), userAuthResponse.get(4), userAuthResponse.get(5));

                    System.out.print("I got a User: " + user.getUsername());
                } else {
                    //for debug
                    System.out.print("There was an error from getting the user details from servers");
                    throw new Exception();
                }
            } else {
                //for debug
                System.out.println("There was an error. SDK too old");
                throw new Exception();
            }
        } else {
            //for debug
            System.out.println("There was an error; fields were empty");
            throw new Exception();
        }
    }

    public void signUp(View v){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
