package com.example.bogdan.onlinequiz;

import android.content.Intent;
import android.media.Image;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bogdan.onlinequiz.Common.Common;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Playing extends AppCompatActivity implements View.OnClickListener {

    final static long INTERVAL = 1000;
    final static long TIMEOUT = 7000;
    int progressValue = 0;

    CountDownTimer countDownTimer;

    int index = 0, score = 0, thisQuestion = 0, totalQuestion, correctAnswers;

    FirebaseDatabase database;
    DatabaseReference questions;

    ProgressBar progressBar;
    ImageView question_image;
    Button btnA, btnB, btnC, btnD;
    TextView txtScore, txtQuestionNum, questionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        //firebase

        database = FirebaseDatabase.getInstance();
        questions = database.getReference("Questions");

        //views

        txtScore = (TextView) findViewById(R.id.txtScore);
        txtQuestionNum = (TextView) findViewById(R.id.txtQuestionsTotal);
        questionText = (TextView) findViewById(R.id.question_text);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnA = (Button) findViewById(R.id.btnAnswerA);
        btnB = (Button) findViewById(R.id.btnAnswerB);
        btnC = (Button) findViewById(R.id.btnAnswerC);
        btnD = (Button) findViewById(R.id.btnAnswerD);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        countDownTimer.cancel();

        if (index < totalQuestion) { //still have questions

            Button clickedButton = (Button) view;
            if (clickedButton.getText().equals(Common.questionList.get(index).getCorrectAnswer())) {

                // Correct answer

                score += 10;
                correctAnswers++;
                showQuestion(++index);

            } else {

                // Incorrect answer

                Intent intent = new Intent(this, Done.class);
                Bundle data = new Bundle();
                data.putInt("SCORE", score);
                data.putInt("TOTAL", totalQuestion);
                data.putInt("CORRECT", correctAnswers);
                intent.putExtras(data);
                startActivity(intent);
                finish();
            }

            txtScore.setText(String.format("%d",score));

        }
    }

    private void showQuestion(int i) {

        if(index < totalQuestion){

            thisQuestion++;
            txtQuestionNum.setText(String.format("%d / %d",thisQuestion, totalQuestion));
            progressBar.setProgress(0);
            progressValue = 0;

            if(Common.questionList.get(index).getIsImageQuestion().equals("true")){

                // if is image

                Picasso.get().load(Common.questionList.get(index).getQuestion()).into(question_image);
                question_image.setVisibility(View.VISIBLE);
                questionText.setVisibility(View.INVISIBLE);

            }

            btnA.setText(Common.questionList.get(index).getAnswerA());
            btnB.setText(Common.questionList.get(index).getAnswerB());
            btnC.setText(Common.questionList.get(index).getAnswerC());
            btnD.setText(Common.questionList.get(index).getAnswerD());

            countDownTimer.start();

        }else {
            // final question
            Intent intent = new Intent(this, Done.class);
            Bundle data = new Bundle();
            data.putInt("SCORE", score);
            data.putInt("TOTAL", totalQuestion);
            data.putInt("CORRECT", correctAnswers);
            intent.putExtras(data);
            startActivity(intent);
            finish();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        totalQuestion = Common.questionList.size();

        countDownTimer = new CountDownTimer(TIMEOUT, INTERVAL) {
            @Override
            public void onTick(long minisec) {
                progressBar.setProgress(progressValue);
                progressValue++;
            }

            @Override
            public void onFinish() {
                countDownTimer.cancel();
                showQuestion(++index);
            }
        };

        showQuestion(++index);

    }
}