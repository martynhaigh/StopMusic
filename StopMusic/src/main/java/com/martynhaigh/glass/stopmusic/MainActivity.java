package com.martynhaigh.glass.stopmusic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {

    public static final String SERVICECMD = "com.android.music.musicservicecommand";
    public static final String CMDNAME = "command";
    public static final String CMDSTOP = "stop";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if(mAudioManager.isMusicActive()) {
            Intent i = new Intent(SERVICECMD);
            i.putExtra(CMDNAME , CMDSTOP );
            MainActivity.this.sendBroadcast(i);
        }
        finish();
    }

}
