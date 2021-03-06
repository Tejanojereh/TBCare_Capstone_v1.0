package com.tbsense.android.tbcare_capstone.PatientActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tbsense.android.tbcare_capstone.Class.PatientClass;
import com.tbsense.android.tbcare_capstone.R;
import com.google.gson.Gson;

public class Account_Patient extends AppCompatActivity {

    TextView patient_case_num, txtcontact, patient_case_numTxtView, txt_weight, txtusername, txtViewPatientID;
    FloatingActionButton editBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acct_patientinfo);
        InitiateControls();


    }

    private void InitiateControls(){
        patient_case_num=findViewById(R.id.patient_case_num);
        txtusername=findViewById(R.id.txtusername);
        txtcontact=findViewById(R.id.txtcontact);
        patient_case_numTxtView=findViewById(R.id.patient_case_numTxtView);
        txt_weight = findViewById(R.id.txt_weight);
        txtViewPatientID = findViewById(R.id.txtViewPatientID);
        editBtn = findViewById(R.id.editBtn);


        SharedPreferences s = getSharedPreferences("session", 0);
        Gson gson = new Gson();
        String json = s.getString("class", "");
        String name = "";
        PatientClass patient = gson.fromJson(json, PatientClass.class);

        /*if(!partner.MiddleName.equals(null))
        {
            name = partner.FirstName +" "+partner.MiddleName+" "+partner.LastName;
            mname.setText(partner.MiddleName);
        }
        else{
            mname.setText("");
            name = partner.FirstName +" "+partner.LastName;
        }
        fname.setText(partner.FirstName);
        lname.setText(partner.LastName);*/
        txtusername.setText(patient.GetUsername());
        txtcontact.setText(patient.Contact_No);
        txt_weight.setText(Float.toString(patient.Weight));
        patient_case_num.setText(patient.TB_CASE_NO);
        patient_case_numTxtView.setText(patient.TB_CASE_NO);
        txtViewPatientID.setText(Integer.toString(patient.ID));

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Account_Patient.this, Edit_PatientActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
