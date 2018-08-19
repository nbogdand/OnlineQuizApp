package com.example.bogdan.onlinequiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.bogdan.onlinequiz.Model.QuestionScore;
import com.example.bogdan.onlinequiz.ViewHolder.ScoreDetailViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ScoreDetail extends AppCompatActivity {

    String viewUser="";

    FirebaseDatabase database;
    DatabaseReference question_score;

    RecyclerView scoreList;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<QuestionScore, ScoreDetailViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_detail);

        database = FirebaseDatabase.getInstance();
        question_score = database.getReference("Questions_Score");

        scoreList = (RecyclerView) findViewById(R.id.scoreList);
        scoreList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        scoreList.setLayoutManager(layoutManager);



        if(getIntent() != null){
            viewUser = getIntent().getStringExtra("viewUser");
        }

        if(!viewUser.isEmpty())
            loadScoreDetail(viewUser);

    }

    public void loadScoreDetail(String viewUser){

        adapter = new FirebaseRecyclerAdapter<QuestionScore, ScoreDetailViewHolder>(
                QuestionScore.class,
                R.layout.score_detail_layout,
                ScoreDetailViewHolder.class,
                question_score.orderByChild("user").equalTo(viewUser)) {
            @Override
            protected void populateViewHolder(ScoreDetailViewHolder viewHolder, QuestionScore model, int position) {
                viewHolder.txt_name.setText(model.getCategoryName());
                Log.d("question_Score::::model::", model.getCategoryName()+"");
                viewHolder.txt_score.setText(model.getScore());
            }
        };

        adapter.notifyDataSetChanged();
        scoreList.setAdapter(adapter);

    }
}
