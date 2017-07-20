package com.e.c.a.h.drawoverotherapps;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

public class DrawOverOtherAppsService extends Service {
    private static final String TAG = DrawOverOtherAppsService.class.getSimpleName();
    HUDView mView;
    private boolean running;

    @Override
    public void onCreate() {
        if (this.running) return;

        super.onCreate();

        this.running = true;

        Toast.makeText(getBaseContext(), "onCreate", Toast.LENGTH_LONG).show();

        final Bitmap thumb = BitmapFactory.decodeResource(getResources(),
                R.drawable.thumb_up);

        Log.d(TAG, "onCreate: thumb.getWidth()" + thumb.getWidth());
        Log.d(TAG, "onCreate: thumb.getHeight()" + thumb.getHeight());

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                thumb.getWidth(),
                thumb.getHeight(),
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);


        params.gravity = Gravity.CENTER;
        params.setTitle("Load Average");
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);


        mView = new HUDView(this, thumb);

        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                Toast.makeText(getBaseContext(), "Clicked", Toast.LENGTH_SHORT).show();
                // TODO Auto-generated method stub
                Log.d(TAG, "arg1(" + arg1.getX() + "," + arg1.getY() + ") thumb(" + thumb.getWidth() + ":" + thumb.getHeight());
                if (arg1.getX() < thumb.getWidth() & arg1.getY() > 0) {
                    Toast.makeText(getBaseContext(), "Thumb clicked", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        wm.addView(mView, params);

        Toast.makeText(getBaseContext(), "View added...", Toast.LENGTH_SHORT).show();
    }


    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy() {
        this.running = false;
        super.onDestroy();
    }
}


@SuppressLint("DrawAllocation")
class HUDView extends ViewGroup {


    Bitmap thumb;

    public HUDView(Context context, Bitmap thumb) {
        super(context);

        this.thumb = thumb;


    }


    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);

        Paint drawPaint = new Paint();
        drawPaint.setAntiAlias(false);
        drawPaint.setFilterBitmap(false);


        // delete below line if you want transparent back color, but to understand the sizes use back color
        canvas.drawColor(Color.BLACK);

        canvas.drawBitmap(thumb, 0, 0, drawPaint);


        canvas.drawText("Hello World", 5, 15, drawPaint);

    }


    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {}

    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}