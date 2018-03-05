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
import android.net.wifi.WifiManager;
import android.provider.ContactsContract;
import android.service.autofill.RegexValidator;
import android.speech.RecognizerIntent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.*;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class MainActivity extends Activity {

    TextView tinp;
    ImageButton bspk;
    View longkey;
    final int req = 100;
    String t = "";
    String y = "";
    String parts[];
    Button ok;
    ImageView asd;
    ArrayList<String> appNameList = new ArrayList<>();
    ArrayList<String> appPackageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tinp = findViewById(R.id.txtinp);
        bspk = findViewById(R.id.btnSpeak);
        ok = findViewById(R.id.okbtn);
        asd = findViewById(R.id.menubtn);

        asd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abc();
            }
        });

        bspk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voiceRecog();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                y = tinp.getText().toString();
                parser();
                y = "";
            }
        });

        tinp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tinp.setText("");
            }
        });

    }

    private void abc() {
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

        parser();
        y = "";
    }

    public void parser() {

        parts = y.split(" ");

        y = "";

        switch (parts[0].toLowerCase()) {
            case "open":
                openCase();
                break;
            case "close":
            case "turn":
                settingcase();
                break;
            case "call":
                caller();
                break;
        }

    }

    public void openCase() {
        startApp();
        if (!parts[1].equalsIgnoreCase("arbitrator")) {
            if (parts[1].equalsIgnoreCase("wifi") || parts[1].equalsIgnoreCase("wi-fi")) {
                wifi("open");
            } else if (parts[1].equalsIgnoreCase("bluetooth")) {
                bt("open");
            } else if (parts[1].equalsIgnoreCase("torch") || parts[1].equalsIgnoreCase("flashlight")) {
                flash("open");
            } else if (parts.length == 2) {
                t = parts[1];
                startApp(appNameList.indexOf(t.toLowerCase()));
            } else {
                int hits[] = new int[appNameList.size()];
                int max = 0, in = 0;
                for (int i = 0; i < appNameList.size(); i++) {
                    String ww = appNameList.get(i);
                    int tt = 0;
                    for (int j = 1; j < parts.length; j++) {
                        if (ww.contains(parts[j].toLowerCase())) {
                            tt++;
                        }
                        //Log.i("hits:",ww+" "+tt);
                    }
                    hits[i] = tt;
                    if (tt > max) {
                        max = tt;
                        in = i;
                    }
                }
                startApp(in);
            }
        }
    }

    private void settingcase() {
        if (parts[0].equalsIgnoreCase("close")) {
            switch (parts[1].toLowerCase()) {
                case "wi-fi":
                case "wifi":
                    wifi("close");
                    break;
                case "bluetooth":
                    bt("close");
                    break;
                case "torch":
                case "flashlight":
                    flash("close");
                    break;
            }
        } else if (parts[0].equalsIgnoreCase("turn")) {
            switch (parts[1].toLowerCase()) {
                case "on":
                    switch (parts[2].toLowerCase()) {
                        case "wi-fi":
                        case "wifi":
                            wifi("open");
                            break;
                        case "bluetooth":
                            bt("open");
                            break;
                        case "torch":
                        case "flashlight":
                            flash("close");
                            break;
                    }
                    break;
                case "off":
                    switch (parts[2].toLowerCase()) {
                        case "wi-fi":
                        case "wifi":
                            wifi("close");
                            break;
                        case "bluetooth":
                            bt("close");
                            break;
                        case "torch":
                        case "flashlight":
                            flash("close");
                            break;
                    }
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case req: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> rslt = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    tinp.setText(rslt.get(0));
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
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
        }
    }


    public void startApp() {
        PackageManager pm = getPackageManager();
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> packages = pm.queryIntentActivities(i, 0);

        for (ResolveInfo ri : packages) {
            try {
                String pn = ri.activityInfo.packageName;
                String an = (String) pm.getApplicationLabel(pm.getApplicationInfo(pn, PackageManager.GET_META_DATA));

                boolean s = false;

                for (int j = 0; j < appNameList.size(); j++) {
                    if (pn.equals(appPackageList.get(j)))
                        s = true;
                }

                if (!s) {
                    appNameList.add(an.toLowerCase());
                    appPackageList.add(pn);
                }
                //Log.i("Check","package = <"+pn+"> name = <"+an+">");
            } catch (Exception e) {
            }
        }
    }

    public void startApp(int f) {
        if (f != -1) {
            activityStarter(getApplicationContext(), appPackageList.get(f));
        } else {
            Toast.makeText(getApplicationContext(), "App not Installed !", Toast.LENGTH_SHORT).show();
        }
    }

    public void activityStarter(Context c, String pn) {
        Intent i = c.getPackageManager().getLaunchIntentForPackage(pn);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(i);
    }

    private void wifi(String a) {
        WifiManager w = (WifiManager) getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE);
        if (a.equalsIgnoreCase("close")) {
            w.setWifiEnabled(false);
        } else if (a.equalsIgnoreCase("open")) {
            w.setWifiEnabled(true);
        }
    }

    private void bt(String a) {
        BluetoothAdapter bt = BluetoothAdapter.getDefaultAdapter();
        if (a.equalsIgnoreCase("close")) {
            bt.disable();
        } else if (a.equalsIgnoreCase("open")) {
            bt.enable();
        }

    }

    private void flash(String a) {
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            android.hardware.Camera cam = android.hardware.Camera.open();
            android.hardware.Camera.Parameters p = cam.getParameters();
            p.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_TORCH);

            switch (a.toLowerCase()) {
                case "open":
                    cam.setParameters(p);
                    cam.startPreview();
                    break;
                case "close":
                    cam.stopPreview();
                    cam.release();
            }
        }
    }

    private void caller() {
        if (parts.length == 2) {
            //if (Pattern.matches("[0-9]",parts[1])) {
            Intent ci = new Intent(Intent.ACTION_CALL);
            ci.setData(Uri.parse("tel:" + parts[1]));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE);
                return;
            }
            startActivity(ci);
            //}
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


}