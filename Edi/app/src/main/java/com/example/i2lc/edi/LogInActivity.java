package com.example.i2lc.edi;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.i2lc.edi.backend.socketClient;

public class LogInActivity extends AppCompatActivity {

    public final static String EXTRA_USERNAME = "bla";

    socketClient mySocketClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Database stuff
        mySocketClient = new socketClient("db.amriksadhra.com", 8080);
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
