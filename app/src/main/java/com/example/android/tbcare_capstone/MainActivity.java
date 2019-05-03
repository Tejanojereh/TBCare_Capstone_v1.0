package com.example.android.tbcare_capstone;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.example.android.tbcare_capstone.WebServiceClass;
import com.example.android.tbcare_capstone.WebServiceClass.Listener;

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
    private ImageButton imgBtnSignIn;
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
                String address = "http://tbcarephp.azurewebsites.net/login.php";
                String[] value = {txtUsername.getText().toString(), txtPassword.getText().toString()};
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
        }
    }

    private void InstantiateControl() {
        txtUsername = (EditText) findViewById(R.id.input_email);
        txtPassword = (EditText) findViewById(R.id.input_password);
        imgBtnSignIn = (ImageButton) findViewById(R.id.btn_login);
        imgBtnForgotPassword = (TextView) findViewById(R.id.link_signup);
        setContentView(R.layout.activity_register);


        btnregister = findViewById(R.id.btnregisterm);

        imgBtnForgotPassword.setOnClickListener(this);
        imgBtnSignIn.setOnClickListener(this);


    }


    @Override
    public void OnTaskCompleted(JSONArray Result) {
//        data = Result;
//        Toast.makeText(this, "GOT IT!", Toast.LENGTH_LONG).show();

        if (Result != null) {
            try {
                JSONObject object = Result.getJSONObject(0);
                id = object.getString("id");
                object = Result.getJSONObject(1);
                uname = object.getString("username");

                if (uname.contains("TP"))
                    intent = new Intent(MainActivity.this, Menu_TBPartner.class);

                else {
                    intent = new Intent(MainActivity.this, menu_patient.class);
                }
                Bundle bundle = new Bundle();
                bundle.putString("id", uname);
                intent.putExtras(bundle);
                txtPassword.setText(" ");
                txtUsername.setText(" ");
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "Incorrect username or password!", Toast.LENGTH_LONG).show();
        }
    }
}
