package com.example.app.quickler;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SplashScreen extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH= 3000;
    ImageView imageView;
    AnimationDrawable anim;

    LinearLayout layout;
    Animation myanim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        imageView = findViewById(R.id.applogo);
        imageView.setBackgroundResource(R.drawable.logo_anim);
        layout = findViewById(R.id.linearlayout);
        myanim = AnimationUtils.loadAnimation(this,R.anim.fadein);
        anim = (AnimationDrawable) imageView.getBackground();

        layout.startAnimation(myanim);
        anim.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent= new Intent(SplashScreen.this,LoginActivity.class);
                SplashScreen.this.startActivity(intent);
                SplashScreen.this.finish();
            }
        },SPLASH_DISPLAY_LENGTH);
    }
}
