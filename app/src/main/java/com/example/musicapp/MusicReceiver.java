
package com.example.musicapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class MusicReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (action == null) return;

        Intent serviceIntent =
                new Intent(context, MusicService.class);

        serviceIntent.setAction(action);

        if (MusicService.PLAY.equals(action)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent);
            } else {
                context.startService(serviceIntent);
            }

        } else if (MusicService.PAUSE.equals(action)
                || MusicService.STOP.equals(action)) {

            context.startService(serviceIntent);
        }
    }
}
