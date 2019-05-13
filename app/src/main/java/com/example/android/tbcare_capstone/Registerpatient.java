package com.example.android.tbcare_capstone;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.DatePicker;

import java.text.DateFormat;
import java.util.Calendar;
import fr.ganfra.materialspinner.MaterialSpinner;
public class Registerpatient extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_patient);


        Button button= (Button) findViewById(R.id.btnintakefirst);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimepickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

       // setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.btndateinitial);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatepickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

    }




    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView textView = (TextView)findViewById(R.id.txtfirstintake);
        textView.setText("Hour:"+hourOfDay+"Minute"+minute);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        TextView textView = (TextView) findViewById(R.id.txtInitialinput);
        textView.setText(currentDateString);
    }

}
