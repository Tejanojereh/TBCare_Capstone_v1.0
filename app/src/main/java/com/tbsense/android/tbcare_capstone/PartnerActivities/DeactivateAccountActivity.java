package com.tbsense.android.tbcare_capstone.PartnerActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbsense.android.tbcare_capstone.Class.PartnerClass;
import com.tbsense.android.tbcare_capstone.DetailedView_Patient;
import com.tbsense.android.tbcare_capstone.R;
import com.tbsense.android.tbcare_capstone.Class.WebServiceClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DeactivateAccountActivity extends AppCompatActivity implements WebServiceClass.Listener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deactivate_account);

        Button deactivateBtn = findViewById(R.id.deactivateBtn);

        deactivateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                String patient_id = bundle.getString("patient_id");

                SharedPreferences s = getSharedPreferences("session", 0);
                Gson gson = new Gson();
                String json = s.getString("class", "");
                int deactivator_id = 0;
                PartnerClass partner = gson.fromJson(json, PartnerClass.class);

                deactivator_id = partner.ID;

                String address = "http://tbcarephp.azurewebsites.net/account_action.php";
                String[] value = {patient_id, String.valueOf(deactivator_id), "RETIRE_ACCOUNT_PATIENT"};
                String[] valueName = {"account_id", "deactivator_id", "action"};

                WebServiceClass service = new WebServiceClass(address, value, valueName, DeactivateAccountActivity.this, DeactivateAccountActivity.this);
                service.execute();
            }
        });
    }

    @Override
    public void OnTaskCompleted(JSONArray Result, boolean flag) {
        if(flag)
        {
            if(Result != null){

                try {
                    JSONObject object = Result.getJSONObject(0);
                    Toast.makeText(this, object.getString("message").toString(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(DeactivateAccountActivity.this, My_Patients.class);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show();
        }
    }
}
