package com.example.android.tbcare_capstone;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.tbcare_capstone.Class.WebServiceClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Set_PatientMedicationActivity extends AppCompatActivity implements WebServiceClass.Listener {

    private TextView patientName, classification, weight, treatment_date_start;
    private Button submit;
    private Spinner intake1, intake2;
    private String patient_id, partner_id, medication_date1, end_date1, medication_date2, end_date2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partner_acceptpendingconfirmation);
        InstantiateControls();


    }

    private void InstantiateControls(){

        Bundle bundle = getIntent().getExtras();
        patientName = findViewById(R.id.txtPatientname);
        classification = findViewById(R.id.disease_classification);
        weight = findViewById(R.id.weightTxt);
        treatment_date_start = findViewById(R.id.date_startedTxt);
        intake1 = findViewById(R.id.spinnerintakes);
        intake2 = findViewById(R.id.spinnerintakes2);
        submit = findViewById(R.id.setBtn);

        SharedPreferences s = getSharedPreferences("session", 0);
        partner_id = Integer.toString(s.getInt("account_id", 0));

        patient_id = bundle.getString("patient_id");
        patientName.setText(bundle.getString("patient_number"));
        classification.setText("Classification: "+bundle.getString("classification"));
        weight.setText("Weight: "+bundle.getString("weight")+"kg");
        treatment_date_start.setText("Treatment Date Start:"+bundle.getString("treatment_date_start"));

        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        //list.add("What was your childhood nickname?");


        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,list);
        intake1.setAdapter(adapter);
        intake2.setAdapter(adapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetDate();
                String address = "http://tbcarephp.azurewebsites.net/accept_patient.php";
                String[] value = {patient_id, partner_id, intake1.getSelectedItem().toString(), intake2.getSelectedItem().toString(), medication_date1, medication_date2, end_date1, end_date2};
                String[] valueName = {"patient_id", "partner_id", "no_of_intake1", "no_of_intake2", "medication_date1", "medication_date2", "end_date1", "end_date2"};
                //String[] valueName = {"patient_id", "no_of_intake1", "no_of_intake2", "medication1Date", "medication2Date"};
                WebServiceClass service = new WebServiceClass(address, value, valueName, Set_PatientMedicationActivity.this, Set_PatientMedicationActivity.this);
                service.execute();
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
                    if(success.equals("true")){
                        Toast.makeText(this, "Patient is now under your supervision", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else if(success.equals("false")){
                        Toast.makeText(this, object.getString("message"), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void SetDate(){
        int year, month, dayOfMonth;
        Calendar c = Calendar.getInstance();

        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        String m="", d="";

        if(month < 10){

            m = "0" + month;
        }
        else if(month > 12)
        {
            month = month - 12;
            c.add(Calendar.YEAR, 1);
            year = c.get(Calendar.YEAR);
            if(month < 10){

                m = "0" + month;
            }
        }
        else
            m=Integer.toString(month);
        if(dayOfMonth < 10){

            d  = "0" + dayOfMonth ;
        }
        else
            d=Integer.toString(dayOfMonth);

        medication_date1 = m +"-"+d+"-"+year;
/*
        c.add(Calendar.MONTH, 3);
        c.add(Calendar.DAY_OF_MONTH, 1);
        month = c.get(Calendar.MONTH);
        dayOfMonth = c.get(Calendar.DAY_OF_MONTH);*/
        month = month+3;
        if(month < 10){

            m = "0" + month;
        }
        else if(month > 12)
        {
            month = month - 12;
            c.add(Calendar.YEAR, 1);
            year = c.get(Calendar.YEAR);
            if(month < 10){

                m = "0" + month;
            }
        }
        else
            m=Integer.toString(month);
        if(dayOfMonth < 10){

            d  = "0" + dayOfMonth ;
        }
        else
            d=Integer.toString(dayOfMonth);

        end_date1 = m +"-"+d+"-"+year;

/*        c.add(Calendar.DAY_OF_MONTH, 1);
        dayOfMonth = c.get(Calendar.DAY_OF_MONTH);*/
        dayOfMonth++;
        if(month < 10){

            m = "0" + month;
        }
        else if(month > 12)
        {
            month = month - 12;
            c.add(Calendar.YEAR, 1);
            year = c.get(Calendar.YEAR);
            if(month < 10){

                m = "0" + month;
            }
        }
        else
            m=Integer.toString(month);
        if(dayOfMonth < 10){

            d  = "0" + dayOfMonth ;
        }
        else
            d=Integer.toString(dayOfMonth);

        medication_date2 = m +"-"+d+"-"+year;

        month = month + 3;
        if(month < 10){

            m = "0" + month;
        }
        else if(month > 12)
        {
            month = month - 12;
            c.add(Calendar.YEAR, 1);
            year = c.get(Calendar.YEAR);
            if(month < 10){

                m = "0" + month;
            }
        }
        else
            m=Integer.toString(month);
        if(dayOfMonth < 10){

            d  = "0" + dayOfMonth ;
        }
        else
            d=Integer.toString(dayOfMonth);

        end_date2 = m +"-"+d+"-"+year;
    }
}
