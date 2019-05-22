package com.tbsense.android.tbcare_capstone.PartnerActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tbsense.android.tbcare_capstone.AccountActivities.ChangePasswordActivity;
import com.tbsense.android.tbcare_capstone.AccountActivities.View_AuditLog;
import com.tbsense.android.tbcare_capstone.Class.PartnerClass;
import com.tbsense.android.tbcare_capstone.Class.WebServiceClass;
import com.tbsense.android.tbcare_capstone.DetailedView_Patient;
import com.tbsense.android.tbcare_capstone.MainActivity;
import com.tbsense.android.tbcare_capstone.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class My_Patients extends AppCompatActivity implements WebServiceClass.Listener, NavigationView.OnNavigationItemSelectedListener {

    private ListView listView;
    private String[] patients_number;
    private String[] patientsdisease;
    private String[] patient_id;
    private String[] weight;
    private String[] treatment_date;
    private String id;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_patients);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        SharedPreferences s = getSharedPreferences("session", 0);
        Gson gson = new Gson();
        String json = s.getString("class", "");
        PartnerClass partner = gson.fromJson(json, PartnerClass.class);

        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header, null);
        TextView userName = (TextView) headerView.findViewById(R.id.user_name);
        TextView user_id=headerView.findViewById(R.id.user_id);
        userName.setText(partner.GetUsername());
        user_id.setText(partner.TP_ID);
        navigationView.addHeaderView(headerView);

        id = Integer.toString(s.getInt("account_id", 0));
        String address = "http://tbcarephp.azurewebsites.net/retrieve_patientList.php";
        String[] value = {id};
        String[] valueName = {"id"};
        WebServiceClass wbc = new WebServiceClass(address, value, valueName, My_Patients.this, My_Patients.this);

        wbc.execute();
    }

    @Override
    public void OnTaskCompleted(JSONArray Result, boolean flag) {
        if(flag)
        {
            String patients_no;
            patients_number = new String[Result.length()];
            patientsdisease = new String[Result.length()];
            patient_id = new String[Result.length()];
            weight = new String[Result.length()];
            treatment_date = new String[Result.length()];
            try {
                JSONObject object = Result.getJSONObject(0);
                patients_no = object.getString("patients_no");
                Toast.makeText(this, "You have no patients at the moment", Toast.LENGTH_LONG).show();
                finish();
            } catch (JSONException e) {
                for (int i = 0; i < Result.length(); i++) {
                    try {
                        JSONObject object = Result.getJSONObject(i);
                        patient_id[i] = object.getString("ID");
                        patients_number[i] = object.getString("TB_CASE_NO").toString();
                        patientsdisease[i] = object.getString("disease_classification").toString();
                        weight[i] = object.getString("weight");
                        JSONObject o = object.getJSONObject("treatment_date_start");
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                        SimpleDateFormat final_format = new SimpleDateFormat("MMM dd, yyyy");
                        try {
                            Date d = df.parse(o.getString("date"));
                            treatment_date[i] = final_format.format(d);;
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }


                    } catch (JSONException er) {
                        er.printStackTrace();
                    }
                }
                listView = findViewById(R.id.listview1);
                MyAdapter adapter = new MyAdapter(this, patients_number, patientsdisease, weight, treatment_date);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(My_Patients.this, DetailedView_Patient.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("patient_id", patient_id[position]);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(true);

        switch (menuItem.getItemId())
        {
            case R.id.nav_home:
                intent = new Intent(getApplicationContext(), Menu_TBPartner.class);
                startActivity(intent);
                finish();
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
                startActivity(intent);
                finish();
                break;



        }


        return false;
    }

    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String[] pid;
        String[] patientsdisease;
        String[] p_weight;
        String[] p_treatment_date;

        MyAdapter(Context c, String[] id, String[] patientsdisease, String[] patientweight, String[] date) {
            super(c, R.layout.row_listview, R.id.linearLayoutID, id);
            this.context = c;
            this.pid = id;
            this.patientsdisease = patientsdisease;
            this.p_weight = patientweight;
            this.p_treatment_date = date;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertview, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_listview, parent, false);
            TextView ids = row.findViewById(R.id.patient_case_numTxtView);
            TextView names = row.findViewById(R.id.tp_name);
            TextView tp_weight = row.findViewById(R.id.tp_weight);
            TextView tp_date_start = row.findViewById(R.id.tp_date_started);

            ids.setText(pid[position]);
            names.setText("Classification: "+patientsdisease[position]);
            tp_weight.setText("Weight: "+p_weight[position]+"kg");
            tp_date_start.setText("Treatment Date Start: "+p_treatment_date[position]);


            return row;
        }

    }

}
