package com.example.i2lc.edi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class PresentationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);

        Intent intent = getIntent();
        String username = intent.getStringExtra(Hello.EXTRA_USERNAME);

        EditText someText = (EditText) findViewById(R.id.someText);
        someText.setText(username);
    }
}
