package com.example.app.quickler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedsActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds);

        recyclerView = findViewById(R.id.feedrecycler);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Quickler");
        mDatabase.keepSynced(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<CardData, MainActivity.RecyclerViewHolder> FBRA = new FirebaseRecyclerAdapter<CardData, MainActivity.RecyclerViewHolder>(
                CardData.class,
                R.layout.card_layout,
                MainActivity.RecyclerViewHolder.class,
                mDatabase)
        {
            @Override
            protected void populateViewHolder(final MainActivity.RecyclerViewHolder viewHolder, final CardData model, int position) {
                viewHolder.setTitle(model.getName());

                viewHolder.bulbbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        model.isbtn = !model.isbtn;

                        if(model.isbtn)
                        {
                            viewHolder.bulbbtn.setBackgroundResource(R.drawable.bulbstateon);
                        }
                        else
                            viewHolder.bulbbtn.setBackgroundResource(R.drawable.bulbstateoff);

                    }
                });

                viewHolder.markbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        model.ismark = !model.ismark;

                        if(model.ismark)
                        {
                            viewHolder.markbtn.setBackgroundResource(R.drawable.ic_bookmark_black_24dp);
                        }
                        else
                            viewHolder.markbtn.setBackgroundResource(R.drawable.ic_bookmark_border_black_24dp);
                    }
                });

            }
        };

        recyclerView.setAdapter(FBRA);
    }

    /*public static class RecyclerViewHolder extends RecyclerView.ViewHolder{

        Button bulbbtn = itemView.findViewById(R.id.bulbbtn);
        Button markbtn = itemView.findViewById(R.id.btnmark);

        public RecyclerViewHolder(View itemView) {
            super(itemView);
        }

        public void setTitle(String text)
        {
            TextView postTitle = itemView.findViewById(R.id.postTitle);
            postTitle.setText(text);
        }
    }*/

}
