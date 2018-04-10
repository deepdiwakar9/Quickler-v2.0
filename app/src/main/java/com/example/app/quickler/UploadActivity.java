package com.example.app.quickler;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

/**
 * Created by Yash Kumar Gupta on 4/10/2018.
 */

public class UploadActivity extends AppCompatActivity {

    private Button mSelectBtn ;
    private Button mUploadBtn ;
    private EditText mCaption;

    private final static int FILE_SELECT_CODE = 1;

    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        mSelectBtn = findViewById(R.id.button2);
        mUploadBtn = findViewById(R.id.button3);
        mCaption = findViewById(R.id.textView7);
        mSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileSelector();
            }
        });

        mStorageRef = FirebaseStorage.getInstance().getReference();
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
                    cursor = UploadActivity.this.getContentResolver().query(fileUri,null, null,null );
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
                            Toast.makeText(UploadActivity.this,"File Uploaded !. ",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            Toast.makeText(UploadActivity.this,"There was an error in uploading. ",Toast.LENGTH_SHORT).show();
                        }
                    });
        }




        super.onActivityResult(requestCode, resultCode, data);



    }
}
