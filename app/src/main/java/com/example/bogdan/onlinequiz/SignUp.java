package com.example.bogdan.onlinequiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bogdan.onlinequiz.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {

    MaterialEditText newUsernameEditText,newPassEditText,confrimPassEditText,newEmailEditText;
    Button createButton, cancelButton;
    User newUser;

    FirebaseDatabase database;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        newUsernameEditText = (MaterialEditText) findViewById(R.id.newUserName);
        newPassEditText = (MaterialEditText) findViewById(R.id.newPassword);
        confrimPassEditText = (MaterialEditText) findViewById(R.id.repeatPassword);
        newEmailEditText = (MaterialEditText) findViewById(R.id.newEmail);

        createButton = (Button) findViewById(R.id.signUpButton);
        cancelButton = (Button) findViewById(R.id.cancel);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });

    }

    public void createUser(){
        if( String.valueOf(newUsernameEditText.getText()).isEmpty()){
            Toast.makeText(SignUp.this, "Please enter your username",Toast.LENGTH_SHORT).show();
        }else
            if( String.valueOf(newPassEditText.getText()).isEmpty()){
                Toast.makeText(SignUp.this, "Please enter a password",Toast.LENGTH_SHORT).show();
            }else
                if( String.valueOf(confrimPassEditText.getText()).isEmpty()){
                    Toast.makeText(SignUp.this, "Please confirm your password",Toast.LENGTH_SHORT).show();
                }else
                    if( String.valueOf(newEmailEditText.getText()).isEmpty()){
                        Toast.makeText(SignUp.this, "Please enter your email",Toast.LENGTH_SHORT).show();
                    }


        if(!newPassEditText.getText().toString().equals(confrimPassEditText.getText().toString()) &&
                !String.valueOf(newPassEditText.getText()).isEmpty()){

            Toast.makeText(SignUp.this,"Passwords must match",Toast.LENGTH_LONG).show();
        }

        if(!String.valueOf(newUsernameEditText.getText()).isEmpty() &&
                !String.valueOf(newPassEditText.getText()).isEmpty() &&
                !String.valueOf(confrimPassEditText.getText()).isEmpty() &&
                !String.valueOf(newEmailEditText.getText()).isEmpty()){

            newUser = new User(newUsernameEditText.getText().toString(),newPassEditText.getText().toString(),
                    newEmailEditText.getText().toString());

            users.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(newUser.getUserName()).exists()){
                        Toast.makeText(SignUp.this,"User already exists",Toast.LENGTH_SHORT);
                    }else {
                        users.child(newUser.getUserName()).setValue(newUser);
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(SignUp.this, databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }

    }
}

