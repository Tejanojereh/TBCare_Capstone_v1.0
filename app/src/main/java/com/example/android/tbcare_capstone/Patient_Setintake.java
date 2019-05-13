package com.example.android.tbcare_capstone;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public  class Patient_Setintake extends AppCompatActivity implements TimePickerfragments.TimePickerListener {

    private TextView tvDisplayTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_setintake);

        tvDisplayTime = findViewById(R.id.tvDisplayTime);

        Button btnShowTimePicker = findViewById(R.id.btnShowTimePicker);
        btnShowTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePickerFragment = new TimePickerfragments();
                timePickerFragment.setCancelable(false);
                timePickerFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        tvDisplayTime.setText(  hour + " :" + minute );
    }



    }


