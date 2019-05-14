package com.example.android.tbcare_capstone;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import android.widget.TextView;
import android.widget.Toast;

import com.example.android.tbcare_capstone.Class.WebServiceClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DetailedView_Patient extends AppCompatActivity implements WebServiceClass.Listener {


    FloatingActionButton intakeHistory, ph_ReadingHistory, patientProgress;
    TextView patient_case_num, patient_case_numTxtView, contactNo, weight, disease_classification, regGroup, treatmentDate_start;


    private Boolean isFABOpen = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailedview_patient);

        InstantiateControls();

        Bundle bundle = getIntent().getExtras();
        String patient_id = bundle.getString("patient_id");


        String address = "http://tbcarephp.azurewebsites.net/retrieve_patientInfo.php";
        String[] value = {patient_id};
        String[] valueName = {"patient_id"};

        WebServiceClass service = new WebServiceClass(address, value, valueName, DetailedView_Patient.this, DetailedView_Patient.this);
        service.execute();
    }

    private void InstantiateControls(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnfab);
        intakeHistory = (FloatingActionButton) findViewById(R.id.intakeHistory);
        ph_ReadingHistory = (FloatingActionButton) findViewById(R.id.ph_readingHistoryFab);
        patientProgress = (FloatingActionButton) findViewById(R.id.progressFab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });

        patient_case_num = findViewById(R.id.patient_case_numTxtView);
        patient_case_numTxtView = findViewById(R.id.patient_case_num);
        contactNo = findViewById(R.id.txtcontact);
        weight = findViewById(R.id.txt_weight);
        disease_classification = findViewById(R.id.classificationTxt);
        regGroup = findViewById(R.id.regGroupTxt);
        treatmentDate_start = findViewById(R.id.treatmentDateStart);


    }

    private void showFABMenu(){
        isFABOpen=true;
        intakeHistory.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        ph_ReadingHistory.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        patientProgress.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        intakeHistory.animate().translationY(0);
        ph_ReadingHistory.animate().translationY(0);
        patientProgress.animate().translationY(0);
    }

    @Override
    public void OnTaskCompleted(JSONArray Result, boolean flag) {

        if(flag)
        {
            if(Result!= null)
            {
                try {
                    JSONObject object = Result.getJSONObject(0);
                    patient_case_numTxtView.setText(object.getString("TB_CASE_NO"));
                    patient_case_num.setText(object.getString("TB_CASE_NO"));
                    contactNo.setText(object.getString("contact_no"));
                    weight.setText(object.getString("weight"));
                    disease_classification.setText(object.getString("disease_classification"));
                    regGroup.setText(object.getString("registration_group"));
                    JSONObject o = object.getJSONObject("treatment_date_start");
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                    SimpleDateFormat final_format = new SimpleDateFormat("MMM dd, yyyy");
                    try {
                        Date d = df.parse(o.getString("date"));
                        treatmentDate_start.setText(final_format.format(d));;
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }

                } catch (JSONException e) {
                    try {
                        JSONObject object = Result.getJSONObject(0);
                        Toast.makeText(this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }

            }
            else
                Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show();

    }
}
