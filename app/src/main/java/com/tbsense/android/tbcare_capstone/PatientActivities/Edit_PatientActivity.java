package com.tbsense.android.tbcare_capstone.PatientActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tbsense.android.tbcare_capstone.Class.PatientClass;
import com.tbsense.android.tbcare_capstone.Class.WebServiceClass;
import com.tbsense.android.tbcare_capstone.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Edit_PatientActivity extends AppCompatActivity implements WebServiceClass.Listener {


    private EditText username, weight, contactNo;
    private FloatingActionButton save;
    private String account_id, patient_id;
    private PatientClass patientClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editpatient);

        InstantiateControls();

    }

    public void InstantiateControls(){
        username = findViewById(R.id.usernameTxt);
        weight = findViewById(R.id.weightTxt);
        contactNo = findViewById(R.id.contactNoTxt);
        save = findViewById(R.id.saveBtn);


        SharedPreferences s = getSharedPreferences("session", 0);
        account_id = s.getString("id", "");
        patient_id = Integer.toString(s.getInt("account_id", 0));
        Gson gson = new Gson();
        String json = s.getString("class", "");
        String name = "";
        patientClass = gson.fromJson(json, PatientClass.class);


        weight.setText(Float.toString(patientClass.Weight));
        username.setText(patientClass.GetUsername());
        contactNo.setText(patientClass.Contact_No);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String address = "http://tbcarephp.azurewebsites.net/edit_patientinfo.php";
                String[] value = {patient_id, account_id, weight.getText().toString(), contactNo.getText().toString(), username.getText().toString()};
                String[] valueName = {"patient_id", "account_id", "weight", "contact_no", "username"};
                WebServiceClass wbc = new WebServiceClass(address, value, valueName, Edit_PatientActivity.this, Edit_PatientActivity.this);

                wbc.execute();
            }
        });


    }

    public boolean Validator(){

        boolean flag = true;
        if(weight.getText().toString().length() == 0)
        {
            weight.requestFocus(); weight.setError("Filed cannot be empty");flag = false;
        }
        if(username.getText().toString().length() == 0){username.requestFocus(); username.setError("Filed cannot be empty");flag = false;}
        return flag;
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
                        patientClass.SetUsername(username.getText().toString());
                        patientClass.Weight = Float.parseFloat(weight.getText().toString());
                        patientClass.Contact_No = contactNo.getText().toString();

                        SharedPreferences s = getSharedPreferences("session", 0);
                        SharedPreferences.Editor editor = s.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(patientClass);
                        editor.putString("class", json);
                        editor.apply();

                        Intent intent = new Intent(Edit_PatientActivity.this, Account_Patient.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    else if(success.equals("false"))
                    {
                        Toast.makeText(this, object.getString("message"), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show();

            }
        }
        else{
            Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show();
        }
    }
}
