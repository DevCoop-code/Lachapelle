package com.hankyo.jeong.lachapelle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.hankyo.jeong.lachapelle.databinding.IntroMainBinding;
import com.hankyo.jeong.lachapelle.databinding.StartMainBinding;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    private IntroMainBinding binding;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide Status Bar
        View decoView = getWindow().getDecorView();
        int uiOption = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decoView.setSystemUiVisibility(uiOption);

        // Hide Action Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        binding = IntroMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AlphaAnimation alphaAnim = new AlphaAnimation(0.0f, 1.0f);
        alphaAnim.setDuration(3000);
//        alphaAnim.setStartOffset(1000);
        alphaAnim.setFillAfter(true);
        alphaAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent startIntent = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(startIntent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        binding.appTitleTxt.setAnimation(alphaAnim);
    }
}
