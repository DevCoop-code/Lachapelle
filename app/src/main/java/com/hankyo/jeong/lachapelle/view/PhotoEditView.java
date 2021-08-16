package com.hankyo.jeong.lachapelle.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.hankyo.jeong.lachapelle.R;
import com.hankyo.jeong.lachapelle.databinding.PhotoeditMainBinding;

import androidx.annotation.NonNull;

public class PhotoEditView extends SurfaceView implements SurfaceHolder.Callback {
    static final String TAG = "PhotoEditView";

    Context mContext;
    SurfaceHolder mHolder;
    RenderingThread mRThread;

    private PhotoeditMainBinding binding;

    public PhotoEditView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    public void startRenderingView(Bitmap imageRes)
    {
        Log.d(TAG, "startRenderingView");
        mRThread = new PhotoEditView.RenderingThread(imageRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        Log.d(TAG, "Surface Created");
        mRThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        // Called when surface is changed
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        try {
            mRThread.interrupt();
            mRThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    class RenderingThread extends Thread {
        Bitmap imageOrigin;

        public RenderingThread() {
            // For Test
            imageOrigin = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.blackberries);
        }

        public RenderingThread(Bitmap imageRes) {
            imageOrigin = imageRes;
        }

        public void run() {
            Canvas canvas = null;
            while (true) {
                canvas = mHolder.lockCanvas();
                try {
                    synchronized (mHolder) {
                        canvas.drawBitmap(imageOrigin, 0, 0, null);
                    }
                } finally {
                    if (canvas == null)
                        return;
                    mHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
