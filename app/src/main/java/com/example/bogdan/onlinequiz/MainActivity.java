package com.example.bogdan.onlinequiz;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bogdan.onlinequiz.BroadcastReceiver.AlarmReceiver;
import com.example.bogdan.onlinequiz.Common.Common;
import com.example.bogdan.onlinequiz.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    MaterialEditText newUserNameEditText, newPasswordEditText, newEmailEditText;
    MaterialEditText userNameEditText, passwordEditText;

    Button signUpButton, signInButton;

    FirebaseDatabase database;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerAlarm();
        
        // FIREBASE

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        userNameEditText = (MaterialEditText) findViewById(R.id.userName);
        passwordEditText = (MaterialEditText) findViewById(R.id.password);

        signUpButton = (Button) findViewById(R.id.sign_up);
        signInButton = (Button) findViewById(R.id.sign_in);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignUpDialog();
            }

        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(userNameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });

    }

    private void registerAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,20);
        calendar.set(Calendar.MINUTE,30);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)this.getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
    }

    private void signIn(final String userName, final String password){

        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(userName).exists()){

                    if(!userName.isEmpty()){

                        User login = dataSnapshot.child(userName).getValue(User.class);

                        if(login.getPassword().equals(password)) {

                            Intent homeActivity = new Intent(MainActivity.this, Home.class);
                            Common.currentUser = login;
                            startActivity(homeActivity);
                            finish();

                        }
                        else {
                            final Toast toast = Toast.makeText(MainActivity.this, "Wrong combination!", Toast.LENGTH_SHORT);
                            toast.show();
                        }

                    }else{

                        final Toast toast = Toast.makeText(MainActivity.this,"Please enter username!",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }else {
                   final Toast toast = Toast.makeText(MainActivity.this,"User doesn't exist!",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void showSignUpDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("SignUp");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View sign_up_layout = inflater.inflate(R.layout.sign_up_layout,null);

        newUserNameEditText = (MaterialEditText) sign_up_layout.findViewById(R.id.newUserName);
        newPasswordEditText = (MaterialEditText) sign_up_layout.findViewById(R.id.newPassword);
        newEmailEditText = (MaterialEditText) sign_up_layout.findViewById(R.id.newEmail);

        alertDialog.setView(sign_up_layout);
        alertDialog.setIcon(R.drawable.ic_person_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                final User user = new User(newUserNameEditText.getText().toString(),
                                    newPasswordEditText.getText().toString(),
                                    newEmailEditText.getText().toString());

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child(user.getUserName()).exists()) {
                            final Toast toast = Toast.makeText(MainActivity.this, "User already exists !", Toast.LENGTH_SHORT);
                            toast.show();
                        }else{

                            users.child(user.getUserName()).setValue(user);
                            final Toast toast = Toast.makeText(MainActivity.this,"User registration successful!", Toast.LENGTH_SHORT);
                            toast.show();
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        final Toast error = Toast.makeText(MainActivity.this,"Database error", Toast.LENGTH_SHORT);
                        error.show();
                    }
                });

                dialogInterface.dismiss();
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();

    }
}
