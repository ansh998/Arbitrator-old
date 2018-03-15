package com.arbitrator;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Register extends AppCompatActivity {

    TextView dob;
    Button reg;

    String date;

    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Date myDate;

        dob = (TextView) findViewById(R.id.input_dob);
        reg = (Button) findViewById(R.id.btn_register);

        dob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                get_date();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void get_date() {
        date = dob.getText().toString();

        try {
            Date myDate = df.parse(date);
            String my = myDate.getDate() + "/" + (myDate.getMonth() + 1) + "/" + (1900 + myDate.getYear());
            Log.i("qwe", my);
        } catch (ParseException e) {
            dob.setError("Invalid Date");
            Log.e("hgfdakj", e.getMessage());
        }
    }
}
