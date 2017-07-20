package com.e.c.a.h.drawoverotherapps;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

public class OSLevelUserInactivityService extends Service {
    private static final String TAG = OSLevelUserInactivityService.class.getSimpleName();
    CustomViewGroup view;
    private boolean running;

    @Override
    public void onCreate() {
        if (this.running) return;

        super.onCreate();

        this.running = true;

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(1, 1,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        view = new CustomViewGroup(this);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                SharedPreferences.Editor sharedPreferencesEditor = getBaseContext().getSharedPreferences(HomeActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE).edit();
                sharedPreferencesEditor.putLong(HomeActivity.SHARED_PREFERENCES_LAST_USER_INTERACTION, System.currentTimeMillis());
                sharedPreferencesEditor.commit();

                Settings.System.putInt(getBaseContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 255);

                //To re set the alarm from zero after the user interaction
                HomeActivity.setAlarm(getBaseContext());

                long lastUserInteraction = getBaseContext().getSharedPreferences(HomeActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE).getLong(HomeActivity.SHARED_PREFERENCES_LAST_USER_INTERACTION, 0);
                Toast.makeText(getBaseContext(), "Interaction: " + lastUserInteraction, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        wm.addView(view, params);
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    /**
     * Not guaranteed to be called...
     * */
    @Override
    public void onDestroy() {
        sendBroadcast(new Intent(HomeActivity.INTENT_ACTION_BROADCAST_START_SERVICE));
        Log.d(TAG, "onDestroy: ");
        return;
    }

    private static class CustomViewGroup extends ViewGroup {

        public CustomViewGroup(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
        }

        protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
        }

        public boolean onTouchEvent(MotionEvent event) {
            return true;
        }
    }
}