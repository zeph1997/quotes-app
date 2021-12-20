package com.example.quoteapp;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView dateTv = (TextView) findViewById(R.id.dateTv);
        TextView quotetv = (TextView) findViewById(R.id.quoteTv);

        Date currentDate = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
        String strDate = dateFormat.format(currentDate);

        dateTv.setText("Hello! Today is\n" + strDate);
        
    }
}