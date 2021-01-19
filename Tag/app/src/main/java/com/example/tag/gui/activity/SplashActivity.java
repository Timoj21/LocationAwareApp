package com.example.tag.gui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tag.R;

public class SplashActivity extends AppCompatActivity {
    private static int splashTime = 3000;

    Animation leftAnim;
    ImageView imageView;
    TextView splashTitle;

    CharSequence charSequence;
    int index;
    long delay = 200;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashTitle = findViewById(R.id.splashTitle);
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

        animateText("TAG");
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            splashTitle.setText(charSequence.subSequence(0, index++));

            if (index <= charSequence.length()){
                handler.postDelayed(runnable,delay);
            }
        }
    };

    public void animateText(CharSequence cs){
        charSequence = cs;

        index = 0;

        splashTitle.setText("");

        handler.removeCallbacks(runnable);

        handler.postDelayed(runnable, delay);
    }
}