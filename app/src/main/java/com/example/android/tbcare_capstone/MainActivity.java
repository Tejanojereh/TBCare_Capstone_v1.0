package com.example.android.tbcare_capstone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.*;

import com.example.android.tbcare_capstone.Class.BaseClass;
import com.example.android.tbcare_capstone.Class.PartnerClass;
import com.example.android.tbcare_capstone.Class.PatientClass;
import com.example.android.tbcare_capstone.Class.Utility.SessionClass;
import com.example.android.tbcare_capstone.Class.WebServiceClass;
import com.example.android.tbcare_capstone.Class.WebServiceClass.Listener;

import org.json.JSONObject;
import org.json.JSONArray;


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
                intent.putExtra("serial_data", partner);
                startActivity(intent);
            }
        }

    }

    @Override
    public void onClick(View v) {

        Intent intent;
        switch (v.getId()) {
            //sign in
            case R.id.btn_login: {
                BaseClass base = new PartnerClass();
                //partner.SetUsername(txtUsername.getText().toString());
                //base.SetUsername(txtUsername.getText().toString());
                base.SetUsername("");
                base.SetPassword(txtPassword.getText().toString());
                String address = "http://tbcarephp.azurewebsites.net/login.php";
                //String[] value = {base.GetUsername(), base.GetPassword()};
                String[] value = {txtUsername.getText().toString(), base.GetPassword()};
                String[] valueName = {"username", "password"};
                WebServiceClass wbc = new WebServiceClass(address, value, valueName, MainActivity.this, MainActivity.this);

                wbc.execute();

            }
            break;

            //forgot password
            case R.id.link_signup: {
                Toast.makeText(this, "Forgot Password", Toast.LENGTH_SHORT).show();
                intent = new Intent(MainActivity.this, ForgotPassword_tbpartner.class);
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
        txtUsername = (EditText) findViewById(R.id.email);
        txtPassword = (EditText) findViewById(R.id.input_password);
        imgBtnSignIn = (AppCompatButton) findViewById(R.id.btn_login);
        imgBtnForgotPassword = (TextView) findViewById(R.id.link_signup);
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

                    if (account_type.equals("PARTNER")) {
                        partner = new PartnerClass();
                        intent = new Intent(MainActivity.this, Menu_TBPartner.class);
                        intent.putExtra("serial_data", partner);
                    }
                    else {
                        patient = new PatientClass();
                        intent = new Intent(MainActivity.this, Menu_Patient.class);
                        intent.putExtra("serial_data", patient);
                    }

                    SharedPreferences s = getSharedPreferences("session", 0);
                    SharedPreferences.Editor editor = s.edit();
                    editor.putInt("account_id", id);
                    editor.putString("account_type", account_type);
                    editor.apply();
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
