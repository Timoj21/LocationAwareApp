package com.example.tag.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.tag.R;

public class SplashActivity extends AppCompatActivity {
    private static int splashTime = 3000;

    Animation leftAnim;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        leftAnim = AnimationUtils.loadAnimation(this, R.anim.left_animation);
        imageView = findViewById(R.id.splashImageView);

        imageView.setAnimation(leftAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, splashTime);
    }
}