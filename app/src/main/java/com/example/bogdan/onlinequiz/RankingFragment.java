package com.example.bogdan.onlinequiz;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bogdan.onlinequiz.Common.Common;
import com.example.bogdan.onlinequiz.Interface.ItemClickListener;
import com.example.bogdan.onlinequiz.Interface.RankingCallBack;
import com.example.bogdan.onlinequiz.Model.QuestionScore;
import com.example.bogdan.onlinequiz.Model.Ranking;
import com.example.bogdan.onlinequiz.ViewHolder.RankingViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RankingFragment extends Fragment{

    View myFragment;

    RecyclerView rankingList;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Ranking,RankingViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference questions_score, rankingTable;

    int sum = 0;

    public static RankingFragment newInstance(){
        RankingFragment rankingFragment = new RankingFragment();
        return rankingFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        questions_score = database.getReference("Questions_Score");
        rankingTable = database.getReference("Ranking");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myFragment = inflater.inflate(R.layout.fragment_ranking,container,false);

        rankingList = (RecyclerView) myFragment.findViewById(R.id.rankingList);
        layoutManager = new LinearLayoutManager(getActivity());
        rankingList.setHasFixedSize(true);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rankingList.setLayoutManager(layoutManager);

        updateScore(Common.currentUser.getUserName(), new RankingCallBack<Ranking>() {
            @Override
            public void callBack(Ranking ranking) {

                rankingTable.child(ranking.getUserName()).setValue(ranking);

                showRanking();

            }
        });

        adapter = new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(
                        Ranking.class,R.layout.layout_ranking,RankingViewHolder.class,rankingTable.orderByChild("score")
                    ) {
                        @Override
                        protected void populateViewHolder(RankingViewHolder viewHolder, final Ranking model, int position) {
                            viewHolder.txt_name.setText(model.getUserName());
                            viewHolder.txt_score.setText(String.valueOf(model.getScore()));

                            viewHolder.setItemClickListener(new ItemClickListener() {
                                @Override
                                public void onClick(View view, int position, boolean isLongClick) {

                                    Intent intent = new Intent(getActivity(),ScoreDetail.class);
                                    intent.putExtra("viewUser",model.getUserName());
                                    startActivity(intent);

                                }
                            });

                        }
                    };


        adapter.notifyDataSetChanged();
        rankingList.setAdapter(adapter);

        return myFragment;
    }

    public void showRanking(){


        rankingTable.orderByChild("score")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot data:dataSnapshot.getChildren()){
                                    Ranking local = data.getValue(Ranking.class);
                                    Log.d("Debug", local.getUserName());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
    }

    public void updateScore(final String username, final RankingCallBack<Ranking> callback){

        questions_score.orderByChild("user").equalTo(username)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for(DataSnapshot data:dataSnapshot.getChildren()){
                                    QuestionScore ques = data.getValue(QuestionScore.class);
                                    sum+=Integer.parseInt(ques.getScore());
                                }

                                Ranking ranking = new Ranking(username,sum);
                                callback.callBack(ranking);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

    }

}
