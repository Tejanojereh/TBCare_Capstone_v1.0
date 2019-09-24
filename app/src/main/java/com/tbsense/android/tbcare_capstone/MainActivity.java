package com.tbsense.android.tbcare_capstone;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.*;

import com.tbsense.android.tbcare_capstone.AccountActivities.Choose_Account_Type;
import com.tbsense.android.tbcare_capstone.Class.BaseClass;
import com.tbsense.android.tbcare_capstone.Class.PartnerClass;
import com.tbsense.android.tbcare_capstone.Class.PatientClass;
import com.tbsense.android.tbcare_capstone.Class.WebServiceClass;
import com.tbsense.android.tbcare_capstone.Class.WebServiceClass.Listener;
import com.tbsense.android.tbcare_capstone.PartnerActivities.Menu_TBPartner;
import com.tbsense.android.tbcare_capstone.PatientActivities.Menu_Patient;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, Listener {

    private EditText txtUsername, txtPassword;
    private TextView imgBtnForgotPassword;
    private AppCompatButton imgBtnSignIn;
    private String account_type;
    private int id;
    private ProgressDialog progressDialog;
    private Intent intent;
    private Button button;
    private Button btnregister;
    private PatientClass patient;
    private PartnerClass partner;
    private BaseClass base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InstantiateControl();
        SharedPreferences s = getSharedPreferences("session", 0);
        int session = s.getInt("account_id", 0);
        String session_account_type = s.getString("account_type", "");
        if(session != 0){
            if(session_account_type.equals("PARTNER")){
                intent = new Intent(MainActivity.this, Menu_TBPartner.class);
                startActivity(intent);
                finish();
            }
            else if(session_account_type.equals("PATIENT")) {
                intent = new Intent(MainActivity.this, Menu_Patient.class);
                startActivity(intent);
                finish();
            }
        }

    }

    @Override
    public void onClick(View v) {

        Intent intent;
        switch (v.getId()) {
            //sign in
            case R.id.submitBtn: {
                if(txtUsername.getText().toString().trim().equals("") || txtPassword.getText().toString().trim().equals(""))
                {
                    Toast.makeText(this, "Please input your credentials", Toast.LENGTH_SHORT).show();
                }
                else{
                    base = new BaseClass();
                    base.SetUsername(txtUsername.getText().toString());
                    base.SetPassword(txtPassword.getText().toString());
                    String address = "http://tbcarephp.azurewebsites.net/login.php";
                    String[] value = {base.GetUsername(), base.GetPassword()};
                    String[] valueName = {"username", "password"};
                    WebServiceClass wbc = new WebServiceClass(address, value, valueName, MainActivity.this, MainActivity.this);

                    wbc.execute();
                }

            }
            break;

            case R.id.forgotpassBtn: {
                intent = new Intent(MainActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
            break;
            case R.id.btnregisterm: {
                 intent = new Intent(MainActivity.this, Choose_Account_Type.class);
                startActivity(intent);
            }
            break;
        }
    }

    private void InstantiateControl() {
        txtUsername = (EditText) findViewById(R.id.input_username);
        txtPassword = (EditText) findViewById(R.id.usernameTxt);
        imgBtnSignIn = (AppCompatButton) findViewById(R.id.submitBtn);
        imgBtnForgotPassword = (TextView) findViewById(R.id.forgotpassBtn);
        btnregister = findViewById(R.id.btnregisterm);

        btnregister.setOnClickListener(this);
        imgBtnForgotPassword.setOnClickListener(this);
        imgBtnSignIn.setOnClickListener(this);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void OnTaskCompleted(JSONArray Result, boolean flag) {
        if(flag)
        {

            if (Result != null) {
                try {
                    JSONObject object = Result.getJSONObject(0);
                    if(object.getString("status").equals("DEACTIVATED"))
                    {
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Sorry, but it seems this account has been deactivated.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                })
                                .setCancelable(false);
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else if(object.getString("status").equals("ACTIVATED")){
                        id = Integer.parseInt(object.getString("ID"));
                        account_type = object.getString("account_type");

                        SharedPreferences s = getSharedPreferences("session", 0);
                        SharedPreferences.Editor editor = s.edit();
                        editor.putInt("account_id", id);
                        editor.putString("account_type", account_type);
                        Gson gson = new Gson();

                        if (account_type.equals("PARTNER")) {
                            partner = new PartnerClass();
                            partner.ID = id;
                            partner.Partner_id = object.getString("account_id");
                            partner.SetUsername(base.GetUsername());
                            partner.TP_ID = object.getString("TP_ID");
                            partner.FirstName = object.getString("firstname");
                            partner.MiddleName = object.getString("middlename");
                            partner.LastName = object.getString("lastname");
                            partner.Contact_No = object.getString("contact_no");
                            partner.Email = object.getString("email");

                            editor.putString("id", partner.Partner_id);
                            String json = gson.toJson(partner);
                            editor.putString("class", json);
                            editor.apply();

                            intent = new Intent(MainActivity.this, Menu_TBPartner.class);
                        }
                        else if(account_type.equals("PATIENT")){
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            SimpleDateFormat print = new SimpleDateFormat("MM-dd-yyyy");
                            patient = new PatientClass();
                            patient.ID = id;
                            patient.Patient_id = object.getString("account_id");
                            patient.SetUsername(base.GetUsername());
                            patient.TB_CASE_NO = object.getString("TB_CASE_NO");
                            patient.Disease_Classification = object.getString("disease_classification");
                            patient.Weight = Float.parseFloat(object.getString("weight"));
                            patient.Registration_Group = object.getString("registration_group");
                            JSONObject t = object.getJSONObject("treatment_date_start");
                            Date temp = df.parse(t.getString("date"));
                            patient.Treatment_Date_Start = print.parse(print.format(temp));
                            patient.Contact_No = object.getString("contact_no");

                            editor.putString("id", patient.Patient_id);
                            String json = gson.toJson(patient);
                            editor.putString("class", json);
                            editor.apply();

                            intent = new Intent(MainActivity.this, Menu_Patient.class);
                        }



                        startActivity(intent);
                        finish();

                    }


                } catch (Exception e) {
                    JSONObject object = null;
                    try {
                        object = Result.getJSONObject(0);
                        Toast.makeText(MainActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            } else {

            }
        }
        else
            Toast.makeText(MainActivity.this, "Error occured!", Toast.LENGTH_LONG).show();

    }

}
