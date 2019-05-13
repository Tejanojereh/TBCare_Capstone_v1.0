package com.example.android.tbcare_capstone;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Spinner;
import java.text.DateFormat;
import java.util.Calendar;
import android.support.v7.widget.AppCompatSpinner;
public class RegisterPatientStart extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_patient_start);


        Button btn = (Button) findViewById(R.id.btndateregister);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatepickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });


    }



    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        TextView textView = (TextView) findViewById(R.id.txtRegisterdate);
        textView.setText(currentDateString);
    }


}