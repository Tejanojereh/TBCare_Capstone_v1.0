package com.example.android.tbcare_capstone;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.tbcare_capstone.Class.Utility.TimePickerfragments;
import com.example.android.tbcare_capstone.Class.WebServiceClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;

public  class Patient_Setintake extends AppCompatActivity implements TimePickerfragments.TimePickerListener, WebServiceClass.Listener {

    private TextView tvDisplayTime;
    private int numberofIntake;
    private Button submitBtn;
    private String first_intake, second_intake, third_intake, fourth_intake, fifth_intake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_setintake);

        first_intake = "";
        second_intake = "";
        third_intake = "";
        fourth_intake = "";
        fifth_intake = "";
        tvDisplayTime = findViewById(R.id.tvDisplayTime);
        submitBtn = findViewById(R.id.btnSubmit);
        Bundle bundle = getIntent().getExtras();
        numberofIntake = bundle.getInt("number_of_intakes");
        Button btnShowTimePicker = findViewById(R.id.btnShowTimePicker);
        btnShowTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePickerFragment = new TimePickerfragments();
                timePickerFragment.setCancelable(false);
                timePickerFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvDisplayTime.getText().toString().equals(""))
                {
                    Toast.makeText(Patient_Setintake.this, "Please select your medication schedule.", Toast.LENGTH_LONG).show();
                }else{
                    SharedPreferences s = getSharedPreferences("session", 0);
                    String patient_id  = Integer.toString(s.getInt("account_id", 0));
                    String address = "http://tbcarephp.azurewebsites.net/set_medication_schedule.php";
                    String[] value = {first_intake, second_intake, third_intake, fourth_intake, fifth_intake, patient_id, Integer.toString(numberofIntake)};
                    String[] valueName = {"first", "second", "third", "fourth", "fifth", "patient_id", "numberofIntakes"};

                    WebServiceClass service = new WebServiceClass(address, value, valueName, Patient_Setintake.this, Patient_Setintake.this);
                    service.execute();
                }

            }
        });
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        Time time = new Time(hour, minute, 0);

        timePicker.getCurrentMinute();
        timePicker.getCurrentHour();
        /*tvDisplayTime.setText(  timePicker.getCurrentHour() + " :" + timePicker.getCurrentMinute() );*/
        tvDisplayTime.setText(time.toString());
        switch(numberofIntake)
        {
            case 2:
                first_intake = time.toString();
                hour = hour + 12;
                if(hour >= 24)
                {
                    hour = hour - 24;
                }
                time = new Time(hour, minute, 0);
                second_intake = time.toString();
                break;
            case 3:
                first_intake = time.toString();
                hour = hour + 8;
                if(hour >= 24)
                {
                    hour = hour - 24;
                }
                time = new Time(hour, minute, 0);
                second_intake = time.toString();
                hour = hour + 8;
                if(hour >= 24)
                {
                    hour = hour - 24;
                }
                time = new Time(hour, minute, 0);
                third_intake = time.toString();
                break;
            case 4:
                first_intake = time.toString();
                hour = hour + 6;
                if(hour >= 24)
                {
                    hour = hour - 24;
                }
                time = new Time(hour, minute, 0);
                second_intake = time.toString();
                hour = hour + 6;
                if(hour >= 24)
                {
                    hour = hour - 24;
                }
                time = new Time(hour, minute, 0);
                third_intake = time.toString();
                hour = hour + 6;
                if(hour >= 24)
                {
                    hour = hour - 24;
                }
                time = new Time(hour, minute, 0);
                fourth_intake = time.toString();
                break;
            case 5:
                first_intake = time.toString();
                hour = hour + 5;
                if(hour >= 24)
                {
                    hour = hour - 24;
                }
                time = new Time(hour, minute, 0);
                second_intake = time.toString();
                hour = hour + 5;
                if(hour >= 24)
                {
                    hour = hour - 24;
                }
                time = new Time(hour, minute, 0);
                third_intake = time.toString();
                hour = hour + 5;
                if(hour >= 24)
                {
                    hour = hour - 24;
                }
                time = new Time(hour, minute, 0);
                fourth_intake = time.toString();
                hour = hour + 5;
                if(hour >= 24)
                {
                    hour = hour - 24;
                }
                time = new Time(hour, minute, 0);
                fifth_intake = time.toString();
                break;
        }
    }

    @Override
    public void OnTaskCompleted(JSONArray Result, boolean flag) {

        if(flag)
        {
            if(Result != null)
            {
                try {
                    JSONObject object = Result.getJSONObject(0);
                    String message = object.getString("message");
                    if(message.equals("Success"))
                    {
                        Toast.makeText(this, "You're all set!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}


