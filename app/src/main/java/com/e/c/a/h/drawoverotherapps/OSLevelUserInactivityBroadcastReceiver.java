package com.e.c.a.h.drawoverotherapps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;

public class OSLevelUserInactivityBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        long lastUserInteraction;
        float currentBrightness;

        try {
            currentBrightness = Settings.System.getFloat(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            currentBrightness = -2f;
        }

        if(intent.getAction().equals(HomeActivity.INTENT_ACTION_BROADCAST_START_SERVICE)) {
            Toast.makeText(context, "Broadcast received... CB: " + currentBrightness, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(context, OSLevelUserInactivityService.class);
            i.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            context.startService(i);

            if((lastUserInteraction = context.getSharedPreferences(HomeActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE).getLong(HomeActivity.SHARED_PREFERENCES_LAST_USER_INTERACTION, 0)) != 0) {
                Toast.makeText(context, System.currentTimeMillis() + " > " + (lastUserInteraction + HomeActivity.ZERO_BRIGHTNESS_AFTER_MS) + " ??", Toast.LENGTH_LONG).show();
                if(System.currentTimeMillis() > lastUserInteraction + HomeActivity.ZERO_BRIGHTNESS_AFTER_MS) {
                    //the user has been inactive after ZERO_BRIGHTNESS_AFTER_MS... set the brightness to zero
                    Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0);
                } else {
                    Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 255);
                }
            }
        }
    }
}
