package com.hankyo.jeong.lachapelle.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
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

        Log.d(TAG, "onDraw Called");
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

        int targetBitmapWidth, targetBitmapHeight;
        public RenderingThread() {
            // For Test
            imageOrigin = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.blackberries);
        }

        public RenderingThread(Bitmap imageRes) {
            imageOrigin = imageRes;

            targetBitmapWidth = imageRes.getWidth();
            targetBitmapHeight = imageRes.getHeight();

            Log.d(TAG, "width: " + targetBitmapWidth + ", height: " + targetBitmapHeight);
        }

        public void run() {
            Canvas canvas = null;
            while (true) {
                canvas = mHolder.lockCanvas();
                try {
                    synchronized (mHolder) {
//                        canvas.drawColor(Color.CYAN);

                        Canvas c = new Canvas();
                        Bitmap result = Bitmap.createBitmap(imageOrigin.getWidth(), imageOrigin.getHeight(), Bitmap.Config.ARGB_8888);

                        c.setBitmap(result);

                        c.drawBitmap(imageOrigin, 0, 0, null);

                        Paint paint = new Paint();
                        paint.setFilterBitmap(false);
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

                        Bitmap maskBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maskimage);
                        Log.d(TAG, "maskBitmap Width: " + maskBitmap.getWidth() + ", Height: " + maskBitmap.getHeight());
                        Bitmap resizedMaskBitmap = Bitmap.createScaledBitmap(maskBitmap, imageOrigin.getWidth(), imageOrigin.getHeight(), true);
                        c.drawBitmap(resizedMaskBitmap, 0, 0, paint);

                        paint.setXfermode(null);

                        canvas.drawBitmap(result, 0, 0, null);
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
