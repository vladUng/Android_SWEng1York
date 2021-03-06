package com.example.i2lc.edi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.i2lc.edi.dbClasses.User;


public class SignUpActivity extends AppCompatActivity {
    private User newUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Button signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LogInActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    public void createNewUser(View view) {
        EditText firstNameText = (EditText) findViewById(R.id.firstNameNew);
        EditText lastNameText = (EditText) findViewById(R.id.lastNameNew);
        EditText emailText = (EditText) findViewById(R.id.emailNew);
        EditText usernameText = (EditText) findViewById(R.id.usernameNew);
        EditText passwordText = (EditText) findViewById(R.id.passwordNew);
        newUser.setFirstName(firstNameText.getText().toString());
        newUser.setSecondName(lastNameText.getText().toString());
        newUser.setEmailAddress(emailText.getText().toString());
        newUser.setUsername(usernameText.getText().toString());
        newUser.setPassword(passwordText.getText().toString());
    }
}
