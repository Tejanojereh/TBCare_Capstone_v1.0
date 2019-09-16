package com.tbsense.android.tbcare_capstone.PatientActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tbsense.android.tbcare_capstone.Class.WebServiceClass;
import com.tbsense.android.tbcare_capstone.Intake_HistoryActivity;
import com.tbsense.android.tbcare_capstone.R;
import com.tbsense.android.tbcare_capstone.Reading_HistoryActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class My_Progress extends AppCompatActivity implements WebServiceClass.Listener {

    private TextView startTreatment, endTreatment, progressmed1, progressmed2, overallProgress;
    private Button readingBtn, intakeBtn, backBtn;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprogress);

        InstantiateControls();
        GetProgress();

    }


    public void InstantiateControls(){
        startTreatment = findViewById(R.id.txtstartdate);
        endTreatment = findViewById(R.id.txtEndDate);
        progressmed1 = findViewById(R.id.medicationProgress);
        progressmed2 = findViewById(R.id.medicationProgress2);
        overallProgress = findViewById(R.id.overallProgressTxt);
        readingBtn = findViewById(R.id.readingHistoryBtn);
        intakeBtn = findViewById(R.id.viewIntakeBtn);
        backBtn = findViewById(R.id.btn_back);

        Bundle retrieve = getIntent().getExtras();
        id = retrieve.getString("patient_id");
        Bundle bundle = new Bundle();
        bundle.putString("patient_id", id);
        readingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(My_Progress.this, Reading_HistoryActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        intakeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(My_Progress.this, Intake_HistoryActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    public void GetProgress(){

        String address = "http://tbcarephp.azurewebsites.net/retrieve_progress.php";
        String[] value = {id};
        String[] valueName = {"ID"};

        WebServiceClass service = new WebServiceClass(address, value, valueName, My_Progress.this, My_Progress.this);
        service.execute();

    }

    @Override
    public void OnTaskCompleted(JSONArray Result, boolean flag) {
        if(flag)
        {
            if(Result != null)
            {
                try {
                    JSONObject object = Result.getJSONObject(0);
                    String hasData = object.getString("hasData");
                    if(hasData.equals("true"))
                    {
                        if(Float.parseFloat(object.getString("med1Progress")) < 0)
                        {
                            progressmed1.setText(0+"%");
                        }
                        else{
                            progressmed1.setText(String.format("%.2f", Float.parseFloat(object.getString("med1Progress")))+"%");
                        }

                        JSONObject progressData2 = Result.getJSONObject(1);
                        if(Float.parseFloat(progressData2.getString("med2Progress")) < 0)
                        {
                            progressmed2.setText(0+"%");
                        }
                        else{
                            progressmed2.setText(String.format("%.2f", Float.parseFloat(progressData2.getString("med2Progress")))+"%");
                        }
                        JSONObject overall = Result.getJSONObject(2);
                        if(Float.parseFloat(overall.getString("overall")) < 0)
                        {
                            overallProgress.setText(0+"%");
                        }
                        else{
                            overallProgress.setText(String.format("%.2f", Float.parseFloat(overall.getString("overall")))+"%");
                        }

                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                        SimpleDateFormat final_format = new SimpleDateFormat("MMM dd, yyyy");
                        JSONObject convertDateStart = overall.getJSONObject("treatment_date_start");
                        JSONObject convertDateEnd = overall.getJSONObject("end_date");
                        try {
                            Date start = df.parse(convertDateStart.getString("date"));
                            Date end = df.parse(convertDateEnd.getString("date"));

                            startTreatment.setText(final_format.format(start));
                            endTreatment.setText(final_format.format(end));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(hasData.equals("false")){
                        Toast.makeText(this, object.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
