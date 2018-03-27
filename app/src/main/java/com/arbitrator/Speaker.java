package com.arbitrator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class Speaker {

    private final Context context;

    public TextToSpeech tt;


    public Speaker(Context context) {
        this(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    public Speaker(Context context, SharedPreferences sp) {
        this.context = context;
    }

    public void Sp_Activate() {
        tt = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tt.setLanguage(Locale.getDefault());
                }
            }
        });
    }

    public void Speech(String y) {

        Sp_Activate();

        if (tt != null) {
            tt.speak(y, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public void Sp_Kill() {
        if (tt != null) {
            tt.stop();
            tt.shutdown();
        }
    }

}
