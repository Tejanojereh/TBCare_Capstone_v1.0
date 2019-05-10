package com.example.android.tbcare_capstone;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import com.example.android.tbcare_capstone.Class.BaseClass;
import com.example.android.tbcare_capstone.Class.PartnerClass;
import com.example.android.tbcare_capstone.Class.PatientClass;
import com.example.android.tbcare_capstone.Class.WebServiceClass;
import com.example.android.tbcare_capstone.Class.WebServiceClass.Listener;
import com.google.gson.Gson;

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
            case R.id.btn_login: {
                base = new BaseClass();
                base.SetUsername(txtUsername.getText().toString());
                base.SetPassword(txtPassword.getText().toString());
                String address = "http://tbcarephp.azurewebsites.net/login.php";
                String[] value = {base.GetUsername(), base.GetPassword()};
                String[] valueName = {"username", "password"};
                WebServiceClass wbc = new WebServiceClass(address, value, valueName, MainActivity.this, MainActivity.this);

                wbc.execute();

            }
            break;

            //forgot password
            case R.id.forgotpassBtn: {
                /*Toast.makeText(this, "Forgot Password", Toast.LENGTH_SHORT).show();
                intent = new Intent(MainActivity.this, ForgotPassword_tbpartner.class);
                startActivity(intent);*/
                View view = LayoutInflater.from(this).inflate(R.layout.forgotpass_changepassword, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Forgot password?")
                        .setView(view)
                        .setNegativeButton("Cancel", null);
                AlertDialog alert = builder.create();
                alert.show();
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
        txtUsername = (EditText) findViewById(R.id.email);
        txtPassword = (EditText) findViewById(R.id.input_password);
        imgBtnSignIn = (AppCompatButton) findViewById(R.id.btn_login);
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
                        partner.SetUsername(base.GetUsername());
                        partner.TP_ID = object.getString("TP_ID");
                        partner.FirstName = object.getString("Firstname");
                        partner.LastName = object.getString("lastname");
                        partner.Contact_No = object.getString("contact_no");
                        partner.Email = object.getString("email");

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
                        patient.SetUsername(base.GetUsername());
                        patient.TB_CASE_NO = object.getString("TB_CASE_NO");
                        patient.Disease_Classification = object.getString("disease_classification");
                        patient.Registration_Group = object.getString("registration_group");
                        JSONObject t = object.getJSONObject("treatment_date_start");
                        Date temp = df.parse(t.getString("date"));
                        patient.Treatment_Date_Start = print.parse(print.format(temp));
                        patient.Contact_No = object.getString("contact_no");

                        String json = gson.toJson(patient);
                        editor.putString("class", json);
                        editor.apply();

                        intent = new Intent(MainActivity.this, Menu_Patient.class);
                    }



                    startActivity(intent);
                    finish();

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Incorrect username or password!", Toast.LENGTH_LONG).show();
            }
        }
        else
            Toast.makeText(MainActivity.this, "Error occured!", Toast.LENGTH_LONG).show();

    }

}
