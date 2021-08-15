package com.hankyo.jeong.lachapelle.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class PhotoEditView extends SurfaceView implements SurfaceHolder.Callback {
    Context mContext;
    SurfaceHolder mHolder;
    RenderingThread mRThread;

    public PhotoEditView(Context context) {
        super(context);

        mContext = context;
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    public PhotoEditView(Context context, Bitmap imageRes) {
        super(context);

        mRThread = new PhotoEditView.RenderingThread(imageRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    class RenderingThread extends Thread {
        public RenderingThread() {

        }

        public RenderingThread(Bitmap imageRes) {

        }
    }
}
