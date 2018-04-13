package com.example.app.quickler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static android.media.MediaRecorder.VideoSource.CAMERA;

/**
 * Created by Yash Kumar Gupta on 4/10/2018.
 */

public class UploadActivity extends AppCompatActivity {

    /*private static final int GALLERY = 2;
    private Button mSelectBtn ;
    private Button mUploadBtn ;
    private EditText mCaption;

    private final static int FILE_SELECT_CODE = 1;

    private StorageReference mStorageRef;
*/
    // Folder path for Firebase Storage.
    String Storage_Path = "All_Image_Uploads/";

    // Root Database Name for Firebase Database.
    String Database_Path = "All_Image_Uploads_Database";

    // Creating button.
    ImageButton UploadButton;

    // Creating EditText.
    EditText ImageCaption ;

    // Creating ImageView.
    ImageView SelectImage;

    // Creating URI.
    Uri FilePathUri;

    // Creating StorageReference and DatabaseReference object.
    StorageReference storageReference;
    DatabaseReference databaseReference;

    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        /*mSelectBtn = findViewById(R.id.button2);
        mUploadBtn = findViewById(R.id.button3);
        mCaption = findViewById(R.id.textView7);
        mSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //showPictureDialog();
                openFileSelector();
            }
        });

        mStorageRef = FirebaseStorage.getInstance().getReference();
    */
        Bundle bundle=getIntent().getExtras();
        Intent data = (Intent) bundle.getParcelable("intentData");
        ImageCaption = findViewById(R.id.editText);
        SelectImage = findViewById(R.id.imageView6);
        UploadButton = findViewById(R.id.imageButton2);
        // Assign FirebaseStorage instance to storageReference.

        storageReference = FirebaseStorage.getInstance().getReference();

        // Assign FirebaseDatabase instance with root database name.
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        progressDialog = new ProgressDialog(UploadActivity.this);
        FilePathUri = data.getData();

        try {

            // Getting selected image into Bitmap.
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);

            // Setting up bitmap selected image into ImageView.
            SelectImage.setImageBitmap(bitmap);

        }
        catch (IOException e) {

            e.printStackTrace();
        }

        // Adding click listener to Upload image button.
        UploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Calling method to upload selected image on Firebase storage.
               UploadImageFileToFirebaseStorage();
               // Toast.makeText(UploadActivity.this,"It is working ",Toast.LENGTH_LONG).show();

            }
        });



    }
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }
    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    public void UploadImageFileToFirebaseStorage() {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {

            // Setting progressDialog Title.
            progressDialog.setTitle("Image is Uploading...");

            // Showing progressDialog.
            progressDialog.show();

            // Creating second StorageReference.
            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Getting image name from EditText and store into string variable.
                            String TempImageName = ImageCaption.getText().toString().trim();

                            // Hiding the progressDialog after done uploading.
                            progressDialog.dismiss();

                            // Showing toast message after done uploading.
                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();

                            @SuppressWarnings("VisibleForTests")
                            ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName, taskSnapshot.getDownloadUrl().toString());

                            // Getting image upload ID.
                            String ImageUploadId = databaseReference.push().getKey();

                            // Adding image upload id s child element into databaseReference.
                            databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            // Showing exception erro message.
                            Toast.makeText(UploadActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.
                            progressDialog.setTitle("Image is Uploading...");

                        }
                    });
        }
        else {

            Toast.makeText(UploadActivity.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }




    /*
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
    */
}
