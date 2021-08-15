package com.hankyo.jeong.lachapelle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.hankyo.jeong.lachapelle.databinding.PhotoeditMainBinding;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PhotoEditActivity extends AppCompatActivity {

    private PhotoeditMainBinding binding;

    private Bitmap mOriginImage;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = PhotoeditMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String imagePath = intent.getExtras().getString("photoResource");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        mOriginImage = BitmapFactory.decodeFile(imagePath, options);

        ImageView originView = binding.originImage;
        originView.setImageBitmap(mOriginImage);
    }
}
