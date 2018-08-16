package com.example.bogdan.onlinequiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.bogdan.onlinequiz.Common.Common;
import com.example.bogdan.onlinequiz.Model.Category;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Start extends AppCompatActivity {

    Button btnPlay;

    FirebaseDatabase database;
    DatabaseReference questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        database = FirebaseDatabase.getInstance();
        questions = database.getReference();

        loadQuestion(Common.categoryId);

    }

    public void loadQuestion(String categoryId){
        questions.orderByChild("categoryId");

    }
}
