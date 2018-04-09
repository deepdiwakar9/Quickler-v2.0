package com.example.app.quickler;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    boolean hide = false;
    ImageView image;
    EditText editTextEmail, editTextPassword;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextPassword = (EditText) findViewById(R.id.PasswordeditTextS);
        editTextEmail = (EditText) findViewById(R.id.EmaileditTextS);
        image = (ImageView) findViewById(R.id.imageViewS);
        progressBar = (ProgressBar) findViewById(R.id.progressBarS);

        mAuth = FirebaseAuth.getInstance();
    }

    public void signUp(View view){

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Minimum lenght of password should be 6");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            finish();
                            startActivity(new Intent(SignUpActivity.this, FeedsActivity.class));
                        } else {
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.i("I","   "+task.getException().getMessage());
                        }
                    }
                });
    }

    public void showHide(View view){
        hide = !hide;

        if (hide){
            editTextPassword.setTransformationMethod(null);
            image.setImageResource(R.mipmap.ic_launcher_hide);
        }else {
            editTextPassword.setTransformationMethod(new PasswordTransformationMethod());
            image.setImageResource(R.mipmap.ic_launcher_views);
        }

    }


    public void loginPage(View view){
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
    }

}
