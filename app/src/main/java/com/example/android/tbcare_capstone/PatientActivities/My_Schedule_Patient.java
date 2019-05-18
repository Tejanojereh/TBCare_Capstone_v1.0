package com.example.android.tbcare_capstone.PatientActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.tbcare_capstone.Class.WebServiceClass;
import com.example.android.tbcare_capstone.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class My_Schedule_Patient extends AppCompatActivity implements WebServiceClass.Listener {

    TextView start1, end1, time1, start2, end2, time2, txtNoOfIntakes1, txtNoOfIntakes2;
    Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_myschedule);

        InstantiateControls();


    }


    public void InstantiateControls(){
        start1 = findViewById(R.id.txtstartdate);
        start2 = findViewById(R.id.txtstartdate2);
        end1 = findViewById(R.id.txtenddate);
        end2 = findViewById(R.id.txtenddate2);
        time1 = findViewById(R.id.txttime1);
        time2 = findViewById(R.id.txttime2);
        txtNoOfIntakes1 = findViewById(R.id.txtNoOfIntakes1);
        txtNoOfIntakes2  = findViewById(R.id.txtNoOfIntakes2);
        backBtn = findViewById(R.id.btn_back);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();

        String address = "http://tbcarephp.azurewebsites.net/retrieve_patient_Schedule.php";
        String[] value = {bundle.getString("patient_id")};
        String[] valueName = {"patient_id"};

        WebServiceClass service = new WebServiceClass(address, value, valueName, My_Schedule_Patient.this, My_Schedule_Patient.this);
        service.execute();
    }



    @Override
    public void OnTaskCompleted(JSONArray Result, boolean flag) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        SimpleDateFormat final_format = new SimpleDateFormat("MMM dd, yyyy");
        SimpleDateFormat time_final_format = new SimpleDateFormat("hh:mm a");
        Date d = new Date();
        String timeString1 = "", timeString2 = "";

        if(flag){
            if(Result != null){
                try {
                    for(int i = 0; i < Result.length(); i++)
                    {
                        JSONObject object = Result.getJSONObject(i);
                        if(object.getString("drug_id").toString().equals("1")){
                            JSONObject initial = object.getJSONObject("initial_date");
                            JSONObject end = object.getJSONObject("end_date");
                            JSONObject time_of_intake = object.getJSONObject("time_of_intake");
                            try {
                                d = df.parse(initial.getString("date"));
                                start1.setText("Start Date: " +final_format.format(d));
                                d = df.parse(end.getString("date"));
                                end1.setText("End Date: " +final_format.format(d));
                                timeString1 = timeString1 +" "+time_final_format.format(df.parse(time_of_intake.getString("date")));
                                time1.setText(timeString1);
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                            txtNoOfIntakes1.setText(object.getString("number_of_intake"));
                        }
                        else if(object.getString("drug_id").toString().equals("2")){
                            JSONObject initial = object.getJSONObject("initial_date");
                            JSONObject end = object.getJSONObject("end_date");
                            JSONObject time_of_intake = object.getJSONObject("time_of_intake");
                            try {
                                d = df.parse(initial.getString("date"));
                                start2.setText("Start Date: " +final_format.format(d));
                                d = df.parse(end.getString("date"));
                                end2.setText("End Date: " +final_format.format(d));
                                timeString2 = timeString2 +" "+time_final_format.format(df.parse(time_of_intake.getString("date")));
                                time2.setText(timeString2);
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                            txtNoOfIntakes2.setText(object.getString("number_of_intake"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
