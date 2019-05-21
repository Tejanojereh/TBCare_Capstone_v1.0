package com.example.android.tbcare_capstone.PatientActivities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.NavigationView;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.Toast;

import com.example.android.tbcare_capstone.AccountActivities.ChangePasswordActivity;
import com.example.android.tbcare_capstone.AccountActivities.View_AuditLog;
import com.example.android.tbcare_capstone.ChoosePartner;
import com.example.android.tbcare_capstone.Class.PatientClass;
import com.example.android.tbcare_capstone.Class.WebServiceClass;
import com.example.android.tbcare_capstone.EditNote;
import com.example.android.tbcare_capstone.MainActivity;
import com.example.android.tbcare_capstone.Notes;
import com.example.android.tbcare_capstone.Notification_medresponse;
import com.example.android.tbcare_capstone.Patient_Setintake;
import com.example.android.tbcare_capstone.R;
import com.google.gson.Gson;
import com.ramijemli.percentagechartview.PercentageChartView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Menu_Patient extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, WebServiceClass.Listener {

    NavigationView navigationView;
    Intent intent;
    DrawerLayout dLayout;
    PatientClass patient;

    String id;
    Bundle bundle1;
    TextView user;

    PercentageChartView chart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_patient);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar

        chart = findViewById(R.id.percentChart);

        //chart.setProgress(10, true);

        navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        // implement setNavigationOnClickListener event
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dLayout.openDrawer(Gravity.LEFT);
            }
        });

        SharedPreferences s = getSharedPreferences("session", 0);
        id = Integer.toString(s.getInt("account_id", 0));
        Gson gson = new Gson();
        String json = s.getString("class", "");
        patient = gson.fromJson(json, PatientClass.class);


        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header, null);
        TextView userName = (TextView) headerView.findViewById(R.id.user_name);
        TextView user_id=headerView.findViewById(R.id.user_id);
        userName.setText(patient.GetUsername());
        user_id.setText(patient.TB_CASE_NO);
        navigationView.addHeaderView(headerView);

        CheckPatientHasPartner();
        GetOverallProgress();

        //setNavigationDrawer(); // call method
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(true);

        Bundle bundle = new Bundle();
        switch (menuItem.getItemId())
        {
            case R.id.nav_home:
                break;
            case R.id.nav_account:
                intent = new Intent(Menu_Patient.this, Account_Patient.class);
                startActivity(intent);
                break;
            case R.id.nav_progress:
                intent = new Intent(Menu_Patient.this, My_Progress.class);
                bundle.putString("patient_id", id);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.nav_schedule:
                intent = new Intent(Menu_Patient.this, My_Schedule_Patient.class);
                bundle.putString("patient_id", id);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.nav_tp:
                intent = new Intent(Menu_Patient.this, My_Partner_Info.class);
                startActivity(intent);
                break;
            case R.id.nav_ChangePassword:
                intent = new Intent(Menu_Patient.this, ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_auditLog:
                intent = new Intent(getApplicationContext(), View_AuditLog.class);
                startActivity(intent);
                break;
            case R.id.nav_notes:
                intent = new Intent(getApplicationContext(), Notes.class);
                startActivity(intent);
                break;
            case R.id.nav_recordIntake:
                intent = new Intent(getApplicationContext(), Notification_medresponse.class);
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
                intent = new Intent(Menu_Patient.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;



        }

        return true;
    }

    public void CheckPatientHasPartner(){
        SharedPreferences s = getSharedPreferences("session", 0);
        id  = Integer.toString(s.getInt("account_id", 0));
        String address = "http://tbcarephp.azurewebsites.net/retrieve_AssignedPartner.php";
        String[] value = {id};
        String[] valueName = {"id"};
        WebServiceClass wbc = new WebServiceClass(address, value, valueName, Menu_Patient.this, Menu_Patient.this);

        wbc.execute();

    }

    public void GetOverallProgress(){
        String address = "http://tbcarephp.azurewebsites.net/retrieve_progress.php";
        String[] value = {id};
        String[] valueName = {"ID"};

        GetProgress service = new GetProgress(address, value, valueName);
        service.execute();

    }


    @Override
    public void OnTaskCompleted(JSONArray Result, boolean flag) {
        if(flag)
        {
            if(Result != null)
            {
                String hasPartner;
                JSONObject object = null;
                try {
                    object = Result.getJSONObject(0);
                    hasPartner = object.getString("hasPartner");

                    if(hasPartner.equals("false")){
                        Toast.makeText(this, "You have no partner assigned", Toast.LENGTH_LONG).show();
                        intent = new Intent(this, ChoosePartner.class);
                        startActivity(intent);
                        finish();
                    }
                    else if(hasPartner.equals("true")){
                        patient.Partner_id = object.getString("partner_id");
                        SharedPreferences s = getSharedPreferences("session", 0);
                        SharedPreferences.Editor editor = s.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(patient);
                        editor.putString("class", json);
                        editor.apply();
                        object = Result.getJSONObject(1);
                        if(Integer.parseInt(object.getString("intakeSchedule").toString()) == 0)
                        {
                            object = Result.getJSONObject(2);
                            int numberofIntake = Integer.parseInt(object.getString("number_of_intake"));
                            object = Result.getJSONObject(3);
                            int numberofIntake2 = Integer.parseInt(object.getString("number_of_intake"));
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Menu_Patient.this);
                            builder.setMessage("Good day! Your partner has already accepted your request. Time to set up your medication intake. Please input your first intake for the day, and we'll calculate it based on the number of intakes your partner gave you.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent = new Intent(Menu_Patient.this, Patient_Setintake.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putInt("number_of_intakes", numberofIntake);
                                            bundle.putInt("number_of_intakes2", numberofIntake2);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                    })
                                    .setCancelable(false);
                            AlertDialog alert = builder.create();
                            alert.show();
                        }

                    }
                } catch (JSONException e) {
                    Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_LONG);;
                }

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



    class GetProgress extends AsyncTask {

        private String Address;
        private String[] Value;
        private String[] ValueName;
        private org.json.JSONArray RecordResult;
        private ProgressDialog progressDialog;
        private boolean flag = true;


        public GetProgress(String address, String[] value, String[] valueName)
        {
            Address = address;
            Value = value;
            ValueName = valueName;
            RecordResult = new org.json.JSONArray();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Menu_Patient.this);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            byte data[];
            HttpPost httpPost;
            StringBuffer buffer = null;
            HttpResponse response;
            HttpClient httpClient;
            InputStream inputStream;
            final String message;

            List<NameValuePair> nameValuePairs;
            nameValuePairs = new ArrayList<NameValuePair>(ValueName.length);
            for(int i=0; i<ValueName.length; i++)
            {
                nameValuePairs.add(new BasicNameValuePair(ValueName[i].toString(), Value[i].toString()));
            }

            try {
                httpClient = new DefaultHttpClient();
                httpPost = new HttpPost(Address);
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                response = httpClient.execute(httpPost);
                inputStream = response.getEntity().getContent();
                data = new byte[256];
                buffer = new StringBuffer();
                int len = 0;

                while(-1 != (len=inputStream.read(data))) {
                    buffer.append(new String (data, 0, len));
                }

                message = buffer.toString();
                JSONObject jsonObj = new JSONObject(message);
                JSONArray RecordResult = jsonObj.getJSONArray("results");

                return RecordResult;

            }catch (final Exception e) {
                flag = false;
                return null;
            }
        }


        @Override
        protected void onPostExecute(Object o) {
            if(flag)
            {
                Object json = null;
                try {
                    json = new JSONTokener(o.toString()).nextValue();
                    if (json instanceof JSONArray) {
                        RecordResult = (JSONArray) json;
                    }
                    JSONObject success = RecordResult.getJSONObject(0);
                    String message = success.getString("hasData");
                    if(message.equals("true"))
                    {
                        JSONObject object = RecordResult.getJSONObject(2);
                        chart.setProgress(Float.parseFloat(object.getString("overall")), true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressDialog.dismiss();
            }else{
                progressDialog.dismiss();
            }

        }
    }

}




