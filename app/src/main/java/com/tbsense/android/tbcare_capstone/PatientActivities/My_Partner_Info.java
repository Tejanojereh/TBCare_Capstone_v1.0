package com.tbsense.android.tbcare_capstone.PatientActivities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tbsense.android.tbcare_capstone.Class.PatientClass;
import com.tbsense.android.tbcare_capstone.Class.WebServiceClass;
import com.tbsense.android.tbcare_capstone.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class My_Partner_Info extends AppCompatActivity implements WebServiceClass.Listener {

    TextView name, email, contact_no;
    Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_info);

        InstantiateControls();
    }

    void InstantiateControls(){

        name = findViewById(R.id.tp_name);
        email = findViewById(R.id.tp_email);
        contact_no = findViewById(R.id.tp_contact);
        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SharedPreferences s = getSharedPreferences("session", 0);
        Gson gson = new Gson();
        String json = s.getString("class", "");
        PatientClass patient = gson.fromJson(json, PatientClass.class);

        String[] value = {patient.Partner_id}, valueName = {"partner_id"};
        String address = "http://tbcarephp.azurewebsites.net/retrieve_partnerinfo.php";
        WebServiceClass service = new WebServiceClass(address, value, valueName, this, this);

        service.execute();


    }
    @Override
    public void OnTaskCompleted(JSONArray Result, boolean flag) {
        if(flag)
        {
            if(Result != null){

                try {
                    JSONObject object = Result.getJSONObject(0);
                    if(!object.getString("middlename").equals(""))
                    {
                        name.setText(object.getString("firstname") +" "+ object.getString("middlename")+" "+ object.getString("lastname"));
                    }
                    else
                        name.setText(object.getString("firstname") +" "+ object.getString("lastname"));

                    email.setText(object.getString("email"));
                    contact_no.setText(object.getString("contact_no"));
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
