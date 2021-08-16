package com.hankyo.jeong.lachapelle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.hankyo.jeong.lachapelle.databinding.PhotoeditMainBinding;
import com.hankyo.jeong.lachapelle.view.PhotoEditView;

import org.opencv.photo.Photo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PhotoEditActivity extends AppCompatActivity {

    private PhotoeditMainBinding binding;

    private Bitmap mOriginImage;

    private PhotoEditView photoEditView;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String imagePath = intent.getExtras().getString("photoResource");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        mOriginImage = BitmapFactory.decodeFile(imagePath, options);

        binding = PhotoeditMainBinding.inflate(getLayoutInflater());
        photoEditView = binding.photoEditView;
        photoEditView.startRenderingView(mOriginImage);

        setContentView(binding.getRoot());

//        PhotoEditView photoEditView = new PhotoEditView(this, mOriginImage);
//        setContentView(photoEditView);
    }
}
