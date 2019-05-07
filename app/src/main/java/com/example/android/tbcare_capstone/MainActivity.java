package com.example.android.tbcare_capstone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.*;

import com.example.android.tbcare_capstone.Class.PartnerClass;
import com.example.android.tbcare_capstone.Class.PatientClass;
import com.example.android.tbcare_capstone.Class.WebServiceClass;
import com.example.android.tbcare_capstone.Class.WebServiceClass.Listener;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, Listener {

    private EditText txtUsername, txtPassword;
    private TextView imgBtnForgotPassword;
    private AppCompatButton imgBtnSignIn;
    private String id, uname;
    private ProgressDialog progressDialog;
    private Intent intent;
    private Button button;
    private Button btnregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InstantiateControl();
    }

    @Override
    public void onClick(View v) {

        Intent intent;
        switch (v.getId()) {
            //sign in
            case R.id.btn_login: {
                PartnerClass partner = new PartnerClass();
                partner.SetUsername(txtUsername.getText().toString());
                partner.SetPassword(txtPassword.getText().toString());
                String address = "http://tbcarephp.azurewebsites.net/login.php";
                String[] value = {partner.GetUsername(), partner.GetPassword()};
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
    public void OnTaskCompleted(JSONArray Result) {
        PatientClass patient;
        PartnerClass partner;
        if (Result != null) {
            try {
                JSONObject object = Result.getJSONObject(0);
                id = object.getString("id");
                object = Result.getJSONObject(1);
                uname = object.getString("username");

                if (uname.contains("TP")) {
                    partner = new PartnerClass();
                    intent = new Intent(MainActivity.this, Menu_TBPartner.class);
                    intent.putExtra("serial_data", partner);
                } else {
                    patient = new PatientClass();
                    intent = new Intent(MainActivity.this, Menu_Patient.class);
                    intent.putExtra("serial_data", patient);
                }
                /*Bundle bundle = new Bundle();
                bundle.putString("id", uname);
                intent.putExtras(bundle);*/
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "Incorrect username or password!", Toast.LENGTH_LONG).show();
        }
    }
}