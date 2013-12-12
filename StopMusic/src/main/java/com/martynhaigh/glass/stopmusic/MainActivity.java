package com.martynhaigh.glass.stopmusic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainActivity extends Activity {

    public static final String SERVICECMD = "com.android.music.musicservicecommand";
    public static final String CMDNAME = "command";
    public static final String CMDSTOP = "stop";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        setContentView(R.layout.main);
        SurfaceView surfaceView = (SurfaceView)findViewById(R.id.confirmation_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();


        int textResIdToShow;
        if(mAudioManager.isMusicActive()) {
            Intent i = new Intent(SERVICECMD);
            i.putExtra(CMDNAME , CMDSTOP );
            MainActivity.this.sendBroadcast(i);
            textResIdToShow = R.string.music_stopped;
        } else {
            textResIdToShow = R.string.music_not_playing;
        }
        ConfirmationDrawer confirmationDrawer = new ConfirmationDrawer(this, textResIdToShow);
        confirmationDrawer.setActivity(this);
        surfaceHolder.addCallback(confirmationDrawer);

    }

}
