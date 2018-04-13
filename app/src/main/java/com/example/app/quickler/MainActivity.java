package com.example.app.quickler;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    String[] typeOfOptions = {"Profile", "Explore", "Upload", "Notification", "Home", "Bookmark"};
    FloatingActionButton fab;
    RecyclerView recyclerView;
    private final static int FILE_SELECT_CODE = 1;

    private StorageReference mStorageRef;

    DatabaseReference mDatabase;
    //Button uploadBtn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                openFileSelector();
            }
        });
        // uploadBtn = findViewById(R.id.uploadBtn);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Quickler");
        mDatabase.keepSynced(true);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        /*uploadBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }));*/
    }

    private void openFileSelector(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try{
            startActivityForResult(
                    Intent.createChooser(intent,"Select a file to upload "),
                    FILE_SELECT_CODE);

        } catch ( android.content.ActivityNotFoundException ex ){
            Toast.makeText(this,"Please Install a file Manager. ",Toast.LENGTH_SHORT).show();

        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == FILE_SELECT_CODE && resultCode== RESULT_OK){

            Uri fileUri = data.getData();

            String urlString = fileUri.toString();

            File myFile = new File(urlString);
            String path = myFile.getAbsolutePath();

            String displayName = null;

            if(urlString.startsWith("content://")){
                Cursor cursor = null;
                try{
                    cursor = this.getContentResolver().query(fileUri,null, null,null );
                    if ( cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }


                }finally {
                    cursor.close();

                }
            } else if(urlString.startsWith("file://")) {

                displayName = myFile.getName();


            }





            StorageReference riversRef = mStorageRef.child("files/"+displayName);

            riversRef.putFile(fileUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Toast.makeText(MainActivity.this,"File Uploaded !. ",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            Toast.makeText(MainActivity.this,"There was an error in uploading. ",Toast.LENGTH_SHORT).show();
                        }
                    });
        }




        super.onActivityResult(requestCode, resultCode, data);



    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<CardData, RecyclerViewHolder> FBRA = new FirebaseRecyclerAdapter<CardData, RecyclerViewHolder>(
                CardData.class,
                R.layout.card_layout,
                RecyclerViewHolder.class,
                mDatabase)
        {
            @Override
            protected void populateViewHolder(final RecyclerViewHolder viewHolder, final CardData model, int position) {
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

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{

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
    }
}



    /*viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {

        }
        });*/