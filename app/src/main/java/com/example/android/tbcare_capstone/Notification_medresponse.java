package com.example.android.tbcare_capstone;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.tbcare_capstone.Class.Utility.AlertReceiver;
import com.example.android.tbcare_capstone.Class.WebServiceClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Notification_medresponse extends Activity implements WebServiceClass.Listener {

    Spinner medicine;
    EditText notes;
    String id;
    RadioGroup rdGrp;
    RadioButton optionRdBtn;
    String address;
    WebServiceClass service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_medresponse);


        InstantiateControls();

        SharedPreferences s = getSharedPreferences("session", 0);

        address = "http://tbcarephp.azurewebsites.net/set_intake_log.php";
        String[] value = {Integer.toString(s.getInt("account_id", 0)), notes.getText().toString()};
        String[] valueNames = {"patient_id", "notes"};
        service = new WebServiceClass(address, value,valueNames, Notification_medresponse.this, Notification_medresponse.this);
    }

    public void InstantiateControls()
    {
        rdGrp = findViewById(R.id.rdGrp);
        Button btn = (Button) findViewById(R.id.btnSubmit);
        medicine = findViewById(R.id.medicineSpinner);
        notes = findViewById(R.id.txtRemarks);

        ArrayList<String> medicineOptions = new ArrayList<>();
        medicineOptions.add("QuadMax");
        medicineOptions.add("DuoMax");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,medicineOptions);
        medicine.setAdapter(adapter);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String drug_id = "";
                int selectedId = rdGrp.getCheckedRadioButtonId();
                optionRdBtn = findViewById(selectedId);

                if(selectedId == R.id.yesRadioBtn)
                {
                    //Toast.makeText(Notification_medresponse.this, medicine.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                    //Toast.makeText(Notification_medresponse.this, "Edi wao ^_-", Toast.LENGTH_SHORT).show();
                    SharedPreferences s = getSharedPreferences("session", 0);

                    if(medicine.getSelectedItem().toString().equals("QuadMax")){
                        drug_id = "1";
                    }else if(medicine.getSelectedItem().toString().equals("DuoMax")){
                        drug_id = "2";
                    }

                    address = "http://tbcarephp.azurewebsites.net/set_intake_log.php";
                    String[] value = {Integer.toString(s.getInt("account_id", 0)), notes.getText().toString(), "TAKEN", drug_id};
                    String[] valueNames = {"patient_id", "notes", "action", "drug_ID"};
                    service = new WebServiceClass(address, value,valueNames, Notification_medresponse.this, Notification_medresponse.this);
                    service.execute();

                }
                else if(selectedId == R.id.noRadioBtn)
                {
                    //Toast.makeText(Notification_medresponse.this, "HAYKIYUUU!", Toast.LENGTH_LONG).show();
                    if(notes.getText().toString().trim().equals("")){
                        notes.requestFocus();
                        notes.setError("Please describe your reason why you haven't take the medicine.");
                    }
                    else{
                        SharedPreferences s = getSharedPreferences("session", 0);

                        if(medicine.getSelectedItem().toString().equals("QuadMax")){
                            drug_id = "1";
                        }else if(medicine.getSelectedItem().toString().equals("DuoMax")){
                            drug_id = "2";
                        }

                        address = "http://tbcarephp.azurewebsites.net/set_intake_log.php";
                        String[] value = {Integer.toString(s.getInt("account_id", 0)), notes.getText().toString(), "MISSED", drug_id};
                        String[] valueNames = {"patient_id", "notes", "action", "drug_ID"};
                        service = new WebServiceClass(address, value,valueNames, Notification_medresponse.this, Notification_medresponse.this);
                        service.execute();
                    }

                }
                else
                {
                    //Calendar cal = Calendar.getInstance();
                    rdGrp.requestFocus();
                    Toast.makeText(Notification_medresponse.this, "Please select your answer. Have you taken your medicine?", Toast.LENGTH_LONG).show();
                    /*cal.add(Calendar.MINUTE, 1);
                    AlarmManager mgrAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);
                    ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();

                    for(int i = 0; i < 10; ++i)
                    {
                        Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);
                        // Loop counter `i` is used as a `requestCode`
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), i, intent, 0);
                        // Single alarms in 1, 2, ..., 10 minutes (in `i` minutes)
                        mgrAlarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                SystemClock.elapsedRealtime() + 60000 * i, AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                                pendingIntent);

                        intentArray.add(pendingIntent);
                    }*/
                }
                /*AlarmManager mgrAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);
                ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();

                for(int i = 0; i < 10; ++i)
                {
                    Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                    // Loop counter `i` is used as a `requestCode`
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), i, intent, 0);
                    // Single alarms in 1, 2, ..., 10 minutes (in `i` minutes)
                    mgrAlarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            SystemClock.elapsedRealtime() + 60000 * i,
                            pendingIntent);

                    intentArray.add(pendingIntent);
                }*/
            }
        });
    }





    @Override
    public void OnTaskCompleted(JSONArray Result, boolean flag) {
        if(flag)
        {
            if(Result != null)
            {
                try {
                    JSONObject object = Result.getJSONObject(0);
                    String success = object.getString("success");
                    if(success.equals("true"))
                    {
                        Toast.makeText(this, "Intake recorded", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else if(success.equals("false")){
                        Toast.makeText(this, object.getString("message").toString(), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else
                Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "Error occured. Please check your internet connection", Toast.LENGTH_SHORT).show();
    }
}

