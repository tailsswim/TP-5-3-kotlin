
package com.example;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.musicapp.MusicReceiver;
import com.example.musicapp.MusicService;
import com.example.tp53.R;

public class MainActivity extends Activity {

    private TextView musicStatus;

    private MusicReceiver receiver;

    private boolean registered = false;

    @SuppressLint({"UnspecifiedRegisterReceiverFlag", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        musicStatus = findViewById(R.id.musicStatus);

        Button playButton = findViewById(R.id.playButton);
        Button pauseButton = findViewById(R.id.pauseButton);
        Button stopButton = findViewById(R.id.stopButton);

        // Enregistrer le BroadcastReceiver
        receiver = new MusicReceiver();

        IntentFilter filter = new IntentFilter();

        filter.addAction(MusicService.PLAY);
        filter.addAction(MusicService.PAUSE);
        filter.addAction(MusicService.STOP);

        if (Build.VERSION.SDK_INT >= 33) {
            registerReceiver(
                    receiver,
                    filter,
                    Context.RECEIVER_NOT_EXPORTED
            );
        } else {
            registerReceiver(receiver, filter);
        }

        registered = true;

        // Bouton Lecture
        playButton.setOnClickListener(v -> {
            sendCommand(MusicService.PLAY);
            musicStatus.setText("Lecture");
        });

        // Bouton Pause
        pauseButton.setOnClickListener(v -> {
            sendCommand(MusicService.PAUSE);
            musicStatus.setText("En Pause");
        });

        // Bouton Arrêter
        stopButton.setOnClickListener(v -> {
            sendCommand(MusicService.STOP);
            musicStatus.setText("Arrêté");
        });
    }

    private void sendCommand(String action) {

        Intent intent = new Intent(action);

        intent.setPackage(getPackageName());

        sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (registered) {
            unregisterReceiver(receiver);
            registered = false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    void requestPermissions(
            new String[] {
        String postNotifications = Manifest.permission.POST_NOTIFICATIONS;
    }
}
if (Build.VERSION.SDK_INT >= 33) {
,
        100
        );
        }
