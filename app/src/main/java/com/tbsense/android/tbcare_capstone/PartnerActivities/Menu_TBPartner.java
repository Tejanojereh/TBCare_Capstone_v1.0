package com.tbsense.android.tbcare_capstone.PartnerActivities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tbsense.android.tbcare_capstone.AccountActivities.ChangePasswordActivity;
import com.tbsense.android.tbcare_capstone.AccountActivities.View_AuditLog;
import com.tbsense.android.tbcare_capstone.Class.PartnerClass;
import com.tbsense.android.tbcare_capstone.Class.WebServiceClass;
import com.tbsense.android.tbcare_capstone.MainActivity;
import com.tbsense.android.tbcare_capstone.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Menu_TBPartner extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, WebServiceClass.Listener {

    NavigationView navigationView;
    Intent intent;
    TextView user_id, user_name, patientsHandledTxtView;
    DrawerLayout dLayout;
    //BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_tbpartner);

        Toolbar toolbar = (Toolbar) findViewById(R.id.partner_toolbar);
        setSupportActionBar(toolbar);
        dLayout = findViewById(R.id.drawer_partnet_layout);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dLayout.openDrawer(Gravity.LEFT);
            }
        });

        InstantiateControls();
        CheckPartnerHandled();
    }

    public void CheckPartnerHandled(){
        SharedPreferences s = getSharedPreferences("session", 0);
        int id = s.getInt("account_id", 0);
        String address = "http://tbcarephp.azurewebsites.net/check_handled_patients.php";
        String[] value = {Integer.toString(id)};
        String[] valueName = {"ID"};

        WebServiceClass service = new WebServiceClass(address, value, valueName, Menu_TBPartner.this, Menu_TBPartner.this);
        service.execute();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void InstantiateControls(){
        patientsHandledTxtView = findViewById(R.id.patientsHandledTxtView);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        SharedPreferences s = getSharedPreferences("session", 0);
        Gson gson = new Gson();
        String json = s.getString("class", "");
        PartnerClass partner = gson.fromJson(json, PartnerClass.class);

        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header, null);
        TextView userName = (TextView) headerView.findViewById(R.id.user_name);
        user_id=headerView.findViewById(R.id.user_id);
        userName.setText(partner.GetUsername());
        user_id.setText(partner.TP_ID);
        navigationView.addHeaderView(headerView);

        //barChart = findViewById(R.id.barChart);
        //drawChart();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(true);

        switch (menuItem.getItemId())
        {
            case R.id.nav_home:
                break;
            case R.id.nav_requests:
                intent = new Intent(getApplicationContext(), Patient_RequestActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_account:
                intent = new Intent(getApplicationContext(), Account_TBPartner.class);
                startActivity(intent);
                break;
            case R.id.nav_patients:
                intent = new Intent(getApplicationContext(), My_Patients.class);
                startActivity(intent);
                break;
            case R.id.nav_ChangePass:
                intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_auditLog:
                intent = new Intent(getApplicationContext(), View_AuditLog.class);
                startActivity(intent);
                break;
            case R.id.nav_log_out:
                SharedPreferences s = getSharedPreferences("session", 0);
                SharedPreferences.Editor editor = s.edit();
                int id = 0;
                String account_type = "";
                editor.putInt("account_id", id);
                editor.putString("account_type", account_type);
                editor.putString("class", "");
                editor.apply();
                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;



        }


        return false;
    }



    @Override
    public void OnTaskCompleted(JSONArray Result, boolean flag) {
        if(flag)
        {
            if(Result != null)
            {
                try {
                    JSONObject object = Result.getJSONObject(0);
                    String patients_handled = object.getString("patients_handled");
                    patientsHandledTxtView.setText(patients_handled);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}