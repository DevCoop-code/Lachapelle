package com.hankyo.jeong.lachapelle;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hankyo.jeong.lachapelle.databinding.ActivityMainBinding;
import com.hankyo.jeong.lachapelle.databinding.StartMainBinding;

import org.jetbrains.annotations.NotNull;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class StartActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    static final String TAG = "StartActivity";

    private StartMainBinding binding;

    ActivityResultLauncher<Intent> requestActivity;

    static final int PERMISSIONS_REQUEST_CODE = 1000;
    String[] PERMISSIONS = {"android.permission.READ_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = StartMainBinding.inflate(getLayoutInflater());
        setContentView(binding.startMain);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (!hasPermissions(PERMISSIONS))
                requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE);
        }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0)
                {
                    boolean externalStoragePermissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (!externalStoragePermissionAccepted)
                    {
                        String needPermMsg = getResources().getString(R.string.permissionNeedMsg);
                        showDialogForPermission(needPermMsg);
                    }
                }
                break;
        }
    }

    private boolean hasPermissions(String[] permissions)
    {
        int result;
        for (String perms: permissions)
        {
            result = ContextCompat.checkSelfPermission(this, perms);
            if (result == PackageManager.PERMISSION_DENIED)
                return false;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission(String msg)
    {
        String alertTitleMsg = getResources().getString(R.string.permissionAlertTitle);
        String alertYesMsg = getResources().getString(R.string.yesAlert);
        String alertNoMsg = getResources().getString(R.string.noAlert);
        String warningPermsMsg = getResources().getString(R.string.permissionWarningMsg);

        AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
        builder.setTitle(alertTitleMsg);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton(alertYesMsg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        });
        builder.setNegativeButton(alertNoMsg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), warningPermsMsg, Toast.LENGTH_LONG).show();
                finish();
            }
        });
        builder.create().show();
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
