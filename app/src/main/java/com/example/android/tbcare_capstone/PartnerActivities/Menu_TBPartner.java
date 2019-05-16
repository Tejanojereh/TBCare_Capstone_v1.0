package com.example.android.tbcare_capstone.PartnerActivities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.tbcare_capstone.ChangePasswordActivity;
import com.example.android.tbcare_capstone.Class.PartnerClass;
import com.example.android.tbcare_capstone.Class.WebServiceClass;
import com.example.android.tbcare_capstone.MainActivity;
import com.example.android.tbcare_capstone.R;
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
    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_tbpartner);


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

        barChart = findViewById(R.id.barChart);
        drawChart();
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
                finish();
                break;
            case R.id.nav_ChangePass:
                intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
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

    private void drawChart() {
        BarChart barChart = findViewById(R.id.barChart);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        Description description = new Description();
        description.setText("");
        barChart.setDescription(description);
        barChart.setMaxVisibleValueCount(50);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);

        XAxis xl = barChart.getXAxis();
        xl.setGranularity(1f);
        xl.setCenterAxisLabels(true);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(30f);
        barChart.getAxisRight().setEnabled(false);

        //data
        float groupSpace = 0.04f;
        float barSpace = 0.02f;
        float barWidth = 0.46f;

        int startYear = 1980;
        int endYear = 1985;

        List<BarEntry> yVals1 = new ArrayList<BarEntry>();
        List<BarEntry> yVals2 = new ArrayList<BarEntry>();

        for (int i = startYear; i < endYear; i++) {
            yVals1.add(new BarEntry(i, 0.4f));
        }

        for (int i = startYear; i < endYear; i++) {
            yVals2.add(new BarEntry(i, 0.7f));
        }

        BarDataSet set1, set2;

        if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) barChart.getData().getDataSetByIndex(1);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Company A");
            set1.setColor(Color.rgb(104, 241, 175));
            set2 = new BarDataSet(yVals2, "Company B");
            set2.setColor(Color.rgb(164, 228, 251));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            dataSets.add(set2);

            BarData data = new BarData(dataSets);
            barChart.setData(data);
        }

        barChart.getBarData().setBarWidth(barWidth);
        barChart.groupBars(startYear, groupSpace, barSpace);
        barChart.invalidate();

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