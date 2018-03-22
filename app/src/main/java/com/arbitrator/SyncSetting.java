package com.arbitrator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SyncSetting extends AppCompatActivity {

    Switch b;
    TextView c, e, f, g, h, i, j, k, l;
    Spinner d;
    Boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_setting);

        b = (Switch) findViewById(R.id.b);
        d = (Spinner) findViewById(R.id.d);
        c = (TextView) findViewById(R.id.c);
        e = (TextView) findViewById(R.id.e);
        f = (TextView) findViewById(R.id.f);
        g = (TextView) findViewById(R.id.g);
        h = (TextView) findViewById(R.id.h);
        i = (TextView) findViewById(R.id.i);
        j = (TextView) findViewById(R.id.j);
        k = (TextView) findViewById(R.id.k);
        l = (TextView) findViewById(R.id.l);



        if (!check) {
            c.setEnabled(false);
            d.setEnabled(false);
            e.setEnabled(false);
            f.setEnabled(false);
            g.setEnabled(false);
            h.setEnabled(false);
            i.setEnabled(false);
            j.setEnabled(false);
            k.setEnabled(false);
            l.setEnabled(false);
        }

        List<String> cat=new ArrayList<String>();
        cat.add("phone");
        cat.add("lauda");
        cat.add("lassan");

        ArrayAdapter<String> da =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,cat);

        da.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        d.setAdapter(da);

        d.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(),parent.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }


}
