package com.hankyo.jeong.lachapelle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.hankyo.jeong.lachapelle.databinding.ActivityMainBinding;
import com.hankyo.jeong.lachapelle.databinding.StartMainBinding;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    static final String TAG = "StartActivity";

    private StartMainBinding binding;

    ActivityResultLauncher<Intent> requestActivity;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = StartMainBinding.inflate(getLayoutInflater());
        setContentView(binding.startMain);

        requestActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK)
                        {
                            Log.d(TAG, "result ok");
                        }
                    }
                });
    }

    public void startPhotoEdit(View view)
    {
        Log.d(TAG, "Image Load Start!!");

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        requestActivity.launch(intent);
    }
}
