package com.example.flatz;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginscreen.R;
import com.hanks.htextview.fall.FallTextView;

public class splashActivity extends AppCompatActivity{

    private final int DURACION_SPLASH = 4000;
    Animation topAnimation;
    ImageView image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splashscreen);

        FallTextView textViewFall1 = (FallTextView) findViewById(R.id.textViewFall1);
        textViewFall1.animateText("Tomas Giordano 4°A");
        textViewFall1.animate();

        topAnimation = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        image = findViewById(R.id.imageView2);
        image.setAnimation(topAnimation);

        new Handler().postDelayed(new Runnable() {
            public void run()
            {
                Intent intent = new Intent(splashActivity.this, AuthActivity.class);
                startActivity(intent);
                finish();
            }
        }, DURACION_SPLASH);
    }
}