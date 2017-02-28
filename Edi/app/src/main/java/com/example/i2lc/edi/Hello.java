package com.example.i2lc.edi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class Hello extends AppCompatActivity {

    public final static String EXTRA_USERNAME = "bla";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
        Log.i("Edi","Hello 1");

    }

    //called when the user clicks the log in button
    public  void logIn(View view) {

        Intent intent = new Intent(this, PresentationActivity.class);
        EditText editText = (EditText) findViewById(R.id.username);
        String username = editText.getText().toString();
        intent.putExtra(EXTRA_USERNAME, username);
        startActivity(intent);
    }

}
