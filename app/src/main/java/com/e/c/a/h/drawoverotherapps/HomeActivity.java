package com.e.c.a.h.drawoverotherapps;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    public static final String INTENT_ACTION_BROADCAST_START_SERVICE = "com.e.c.a.h.drawoverotherapps.OSLevelUserInactivityService.LAUNCH_SERVICE";
    public static final String SHARED_PREFERENCES = "com.e.c.a.h.drawoverotherapps.SHARED_PREFERENCES";
    public static final String SHARED_PREFERENCES_LAST_USER_INTERACTION = "com.e.c.a.h.drawoverotherapps.LAST_USER_INTERACTION";

    public static final long ZERO_BRIGHTNESS_AFTER_MS = 1000l * 60l * 2l; //2 minutes of inactivity
    private static final int ACTIVITY_MANAGE_SETTINGS_RQ = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        verifySettingsPermission(false);
    }

    private void verifySettingsPermission(boolean showToast) {
        if(!Settings.System.canWrite(this)) {
            if(showToast) {
                Toast.makeText(this, "Please allow the app to change the settings!", Toast.LENGTH_SHORT).show();
            }
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS), ACTIVITY_MANAGE_SETTINGS_RQ);
        } else {
            setSettingsAndStartAlarm();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ACTIVITY_MANAGE_SETTINGS_RQ) {
            verifySettingsPermission(true);
        }
    }

    private void setSettingsAndStartAlarm() {
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0);

        startService(new Intent(this, OSLevelUserInactivityService.class));

        HomeActivity.setAlarm(this);
    }

    public static void setAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent i = new Intent(context, OSLevelUserInactivityBroadcastReceiver.class);
        i.setAction(INTENT_ACTION_BROADCAST_START_SERVICE);
        i.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (AlarmManager.INTERVAL_FIFTEEN_MINUTES / 15), pendingIntent);
    }
}
