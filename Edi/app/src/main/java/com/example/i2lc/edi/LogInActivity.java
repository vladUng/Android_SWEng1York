package com.example.i2lc.edi;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.i2lc.edi.backend.UserAuth;
import com.example.i2lc.edi.backend.socketClient;

import java.util.ArrayList;

public class LogInActivity extends AppCompatActivity {

    public final static String EXTRA_USERNAME = "bla";

    socketClient mySocketClient;

    private boolean loginSuccessful;
    private boolean isTeacher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Database stuff
        mySocketClient = new socketClient("db.amriksadhra.com", 8080);

        //TODO move this to logiIN action & parse the expected values to it
        ArrayList<String> userAuthResponse = new ArrayList<String>();
        userAuthResponse = mySocketClient.userAuth(new UserAuth("userToLogin", "password"));

        if (!userAuthResponse.contains("error")){
            String authStatus = userAuthResponse.get(0);
            String userType = userAuthResponse.get(1);

            switch (userType) {
                case "admin":
                    loginSuccessful = true;
                    break;
                case "teacher":
                    isTeacher = true;
                    loginSuccessful = true;
                    break;
                case "student":
                    loginSuccessful = true;
                    break;
                case "auth_fail":
                    loginSuccessful = false;
                    break;
            }
            //for debug
            System.out.println("LoginSuccessfull: " + loginSuccessful + "\n userType:" + userType);
        } else {
            //for debug
            System.out.println("There was an error");
        }
    }

    //called when the user clicks the log in button
    public  void logIn(View view) {

        Intent intent = new Intent(this, HomeActivity.class);
        EditText editText = (EditText) findViewById(R.id.username);
        String username = editText.getText().toString();
        intent.putExtra(EXTRA_USERNAME, username);
        startActivity(intent);
    }
}
