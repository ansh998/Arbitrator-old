package com.arbitrator;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.text.format.Time;

import static com.arbitrator.MainActivity.parts;

public class Systemser {


    private final Context context;


    public Systemser(Context context) {
        this(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    public Systemser(Context context, SharedPreferences sp) {
        this.context = context;
    }

    public void caller() {
        if (parts.length == 2) {
            //if (Pattern.matches("[0-9]",parts[1])) {
            call(parts[1]);
            //}
        } else {
            String number = "";
            for (int i = 1; i < parts.length; i++) {
                number += parts[i];
            }
            call(number);
        }
        /*else {
            String NAME="";
            ContentResolver cr = getContentResolver();
            Cursor cursor= cr.query(ContactsContract.Contacts.CONTENT_URI,null,"DISPLAY_NAME = "+NAME+"'",null,null);
            if (cursor.moveToFirst()){
                //String
            }
        }*/
    }

    private void call(String s) {

        Intent i = new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel:" + s));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        context.startActivity(i);

    }

    public void alarm() {
        MediaPlayer mp = MediaPlayer.create(context, R.raw.apple_ring);
        mp.start();
    }

}
