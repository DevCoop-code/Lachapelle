package com.hankyo.jeong.lachapelle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hankyo.jeong.lachapelle.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    static final String TAG = "Lachapelle";
    static final int PERMISSIONS_REQUEST_CODE = 1000;
    String[] PERMISSIONS = {"android.permission.READ_EXTERNAL_STORAGE"};

    static final int REQ_CODE_SELECT_IMAGE = 100;

    // Used to load the 'native-lib' library on application startup.
    static
    {
        System.loadLibrary("native-lib");
    }

    private ActivityMainBinding binding;

    private ImageView mImageView;
    private ImageView mEdgeImageView;

    private Bitmap mInputImage;
    private Bitmap mOriginalImage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (!hasPermissions(PERMISSIONS))
            {
                requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }

        setUI();

        // Example of a call to a native method
        TextView tv = binding.sampleText;
        tv.setText(stringFromJNI());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mInputImage.recycle();
        if (mInputImage != null)
            mInputImage = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_SELECT_IMAGE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                String path = getImagePathFromURI(data.getData());
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                mOriginalImage = BitmapFactory.decodeFile(path, options);
                mInputImage = BitmapFactory.decodeFile(path, options);

                if (mInputImage != null)
                    detectEdgeUsingJNI();
            }
        }
    }

    private void setUI()
    {
        mImageView = binding.originIv;
        mEdgeImageView = binding.edgeIv;
    }

    private String getImagePathFromURI(Uri contentUri)
    {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null)
            return contentUri.getPath();
        else
        {
            int idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String imgPath = cursor.getString(idx);
            cursor.close();

            return imgPath;
        }
    }

    private void detectEdgeUsingJNI()
    {
        Mat src = new Mat();
        Utils.bitmapToMat(mInputImage, src);
        mImageView.setImageBitmap(mOriginalImage);

        Mat edge = new Mat();
        detectEdgeJNI(src.getNativeObjAddr(), edge.getNativeObjAddr(), 50, 150);
        Utils.matToBitmap(edge, mInputImage);
        mEdgeImageView.setImageBitmap(mInputImage);
    }

    private boolean hasPermissions(String[] permissions)
    {
        int result;
        for (String perms: permissions)
        {
            result = ContextCompat.checkSelfPermission(this, perms);
            if (result == PackageManager.PERMISSION_DENIED)
            {
                return false;
            }
        }
        return true;
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

    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission(String msg)
    {
        String alertTitleMsg = getResources().getString(R.string.permissionAlertTitle);
        String alertYesMsg = getResources().getString(R.string.yesAlert);
        String alertNoMsg = getResources().getString(R.string.noAlert);
        String warningPermsMsg = getResources().getString(R.string.permissionWarningMsg);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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

    public void onButtonClicked(View view)
    {
        Log.d(TAG, "Image Load Start!!");

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public native void detectEdgeJNI(long inputImage, long outputImage, int th1, int th2);
}