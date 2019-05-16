package com.example.android.tbcare_capstone.PartnerActivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
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

import com.example.android.tbcare_capstone.ChangePasswordActivity;
import com.example.android.tbcare_capstone.Class.PartnerClass;
import com.example.android.tbcare_capstone.Class.WebServiceClass;
import com.example.android.tbcare_capstone.MainActivity;
import com.example.android.tbcare_capstone.R;
import com.example.android.tbcare_capstone.Set_PatientMedicationActivity;
import com.google.gson.Gson;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Patient_RequestActivity extends AppCompatActivity implements WebServiceClass.Listener, NavigationView.OnNavigationItemSelectedListener {

    private TextView user_id;
    private ListView listView;
    private NavigationView navigationView;
    private String[] patients_number;
    private String[] patientsdisease;
    private String[] patient_id;
    private String[] weight;
    private String[] treatment_date;
    private String id, patientID, patient_weight, patient_classification, patient_caseNo, date;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_patients);

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

        id = Integer.toString(s.getInt("account_id", 0));
        String address = "http://tbcarephp.azurewebsites.net/retrieve_requests.php";
        String[] value = {id};
        String[] valueName = {"id"};
        WebServiceClass wbc = new WebServiceClass(address, value, valueName, Patient_RequestActivity.this, Patient_RequestActivity.this);

        wbc.execute();
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
            case R.id.nav_requests:
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
            String patients_no;
            patients_number = new String[Result.length()];
            patientsdisease = new String[Result.length()];
            patient_id = new String[Result.length()];
            weight = new String[Result.length()];
            treatment_date = new String[Result.length()];
            try {
                JSONObject object = Result.getJSONObject(0);
                patients_no = object.getString("patients_no");
                Toast.makeText(this, "You have no patient requests at the moment", Toast.LENGTH_LONG).show();
                finish();
            } catch (JSONException e) {
                for (int i = 0; i < Result.length(); i++) {
                    try {
                        JSONObject object = Result.getJSONObject(i);
                        if(object.getString("status").equals("PENDING")){
                            patient_id[i] = object.getString("ID");
                            patients_number[i] = object.getString("TB_CASE_NO").toString();
                            patientsdisease[i] = object.getString("disease_classification").toString();
                            weight[i] = object.getString("weight");
                            JSONObject o = object.getJSONObject("treatment_date_start");
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                            SimpleDateFormat final_format = new SimpleDateFormat("MMM dd, yyyy");
                            try {
                                Date d = df.parse(o.getString("date"));

                                treatment_date[i] = final_format.format(d);
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(Patient_RequestActivity.this);
                        builder.setMessage(patients_number[position]+" is requesting you to be his/her partner?")
                                .setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        /*SharedPreferences s = getSharedPreferences("session", 0);
                                        String account_id = Integer.toString(s.getInt("account_id", 0));
                                        String address = "http://tbcarephp.azurewebsites.net/accept_patient.php";
                                        String[] value = {account_id, patient_id[position]};
                                        String[] valueName = {"partner_id", "patient_id"};
                                        new PartnerAction(address, value, valueName,"ACCEPT").execute();*/
                                        patientID = patient_id[position];
                                        patient_weight = weight[position];
                                        patient_classification = patientsdisease[position];
                                        patient_caseNo = patients_number[position];
                                        date = treatment_date[position];
                                        Intent intent = new Intent(Patient_RequestActivity.this, Set_PatientMedicationActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("patient_id", patientID);
                                        bundle.putString("weight", patient_weight);
                                        bundle.putString("classification", patient_classification);
                                        bundle.putString("patient_number", patient_caseNo);
                                        bundle.putString("treatment_date_start", date);

                                        intent.putExtras(bundle);

                                        startActivity(intent);
                                        finish();

                                    }
                                })
                                .setNegativeButton("DECLINE", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        SharedPreferences s = getSharedPreferences("session", 0);
                                        String account_id = Integer.toString(s.getInt("account_id", 0));
                                        String address = "http://tbcarephp.azurewebsites.net/decline_patient.php";
                                        String[] value = {account_id, patient_id[position]};
                                        String[] valueName = {"partner_id", "patient_id"};
                                        new PartnerAction(address, value, valueName, "DECLINE").execute();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
            }
        }
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



    class PartnerAction extends AsyncTask {

        private String Address;
        private String action;
        private String[] Value;
        private String[] ValueName;
        private org.json.JSONArray RecordResult;
        private ProgressDialog progressDialog;
        private boolean flag = true;


        public PartnerAction(String address, String[] value, String[] valueName, String action_)
        {
            Address = address;
            Value = value;
            ValueName = valueName;
            RecordResult = new org.json.JSONArray();
            this.action = action_;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Patient_RequestActivity.this);
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
                    String message = success.getString("success");
                    if(message.equals("true"))
                    {
                        if(action.equals("DECLINE"))
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Patient_RequestActivity.this);
                            builder.setMessage("Patient declined.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent = new Intent(Patient_RequestActivity.this, Patient_RequestActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                        else if(action.equals("ACCEPT")){
                            Intent intent = new Intent(Patient_RequestActivity.this, Set_PatientMedicationActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("patient_id", patientID);
                            bundle.putString("weight", patient_weight);
                            bundle.putString("classification", patient_classification);
                            bundle.putString("patient_number", patient_caseNo);
                            bundle.putString("treatment_date_start", date);

                            intent.putExtras(bundle);

                            startActivity(intent);
                            finish();
                        }

                    }
                    else
                        Toast.makeText(Patient_RequestActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
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
