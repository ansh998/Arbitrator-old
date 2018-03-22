package com.arbitrator;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Camera;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.ContactsContract;
import android.service.autofill.RegexValidator;
import android.speech.RecognizerIntent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class MainActivity extends Activity {


    TextView tinp, op;
    ImageButton bspk;
    final int req = 100;
    String t = "";
    String y = "";
    public static String[] parts;
    ImageButton ok;
    ImageView asd;


    public TextToSpeech tts;


    private Set set = null;
    private Appopen ao = null;
    private Systemser ss = null;
    private Parser pp = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        set = new Set(this);
        ao = new Appopen(this);
        ss = new Systemser(this);
        pp = new Parser(this);

        pp.setter(set, ao, ss);

        ao.startApp();


        tinp = findViewById(R.id.txtinp1);
        bspk = findViewById(R.id.btnSpeak);
        ok = findViewById(R.id.okbtn);
        asd = findViewById(R.id.menubtn);
        op = findViewById(R.id.optv);

        op.setMovementMethod(new ScrollingMovementMethod());

        tinp.clearFocus();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        asd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menupop();
            }
        });

        bspk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voiceRecog();
            }
        });


        /*tinp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tinp.setText("");
            }
        });*/

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.getDefault());
                }
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ao.startApp();
                y = tinp.getText().toString();
                tts.speak(y, TextToSpeech.QUEUE_FLUSH, null);
                pp.parse1(y);
                op.setText(y);
                tinp.setText("");
                y = "";

            }
        });

    }

    private void menupop() {
        PopupMenu pop = new PopupMenu(this, asd);
        pop.getMenuInflater().inflate(R.menu.main_activity_actions, pop.getMenu());
        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_btn_abtus:
                        //Code for option 1
                        break;

                    case R.id.menu_btn_cnglg:
                        Intent i = new Intent(getApplicationContext(), changelog.class);
                        startActivity(i);
                        break;

                    case R.id.menu_btn_lgout:
                        FirebaseAuth.getInstance().signOut();
                        Intent li = new Intent(getApplicationContext(), Login.class);
                        startActivity(li);
                        finish();
                        break;

                    case R.id.menu_btn_sync:
                        Intent si = new Intent(getApplicationContext(), SyncSetting.class);
                        startActivity(si);
                        break;

                    default:
                        break;
                }
                return false;
            }
        });
        pop.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        pp.parse1(y);
        y = "";


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case req: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> rslt = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    op.setText(rslt.get(0));
                    tinp.setText("");
                    tts.speak(op.getText().toString(), TextToSpeech.QUEUE_FLUSH,null);
                    y = rslt.get(0);
                }
            }
            break;
        }
    }

    public void voiceRecog() {

        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));

        try {
            startActivityForResult(i, req);
            //ao.startApp();
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
        }
    }

    public void onPause() {

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}