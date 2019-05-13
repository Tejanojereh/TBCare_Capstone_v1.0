package com.example.android.tbcare_capstone;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.tbcare_capstone.Class.WebServiceClass;

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

public class Patient_RequestActivity extends AppCompatActivity implements WebServiceClass.Listener {

    ListView listView;
    String patients_number[];
    String patientsdisease[];
    String[] patient_id;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_patients);

        SharedPreferences s = getSharedPreferences("session", 0);
        id = Integer.toString(s.getInt("account_id", 0));
        String address = "http://tbcarephp.azurewebsites.net/retrieve_patientList.php";
        String[] value = {id};
        String[] valueName = {"id"};
        WebServiceClass wbc = new WebServiceClass(address, value, valueName, Patient_RequestActivity.this, Patient_RequestActivity.this);

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
            try {
                JSONObject object = Result.getJSONObject(0);
                patients_no = object.getString("patients_no");
                Toast.makeText(this, "You have no patient requests at the moment", Toast.LENGTH_LONG).show();
                //finish();
            } catch (JSONException e) {
                for (int i = 0; i < Result.length(); i++) {
                    try {
                        JSONObject object = Result.getJSONObject(i);
                        if(object.getString("status").equals("PENDING")){
                            patient_id[i] = object.getString("ID");
                            patients_number[i] = object.getString("TB_CASE_NO").toString();
                            patientsdisease[i] = object.getString("disease_classification").toString();
                        }
                    } catch (JSONException er) {
                        er.printStackTrace();
                    }
                }
                listView = findViewById(R.id.listview1);
                MyAdapter adapter = new MyAdapter(this, patients_number, patientsdisease);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Patient_RequestActivity.this);
                        builder.setMessage(patients_number[position]+" is requesting you to be his/her partner?")
                                .setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        SharedPreferences s = getSharedPreferences("session", 0);
                                        String account_id = Integer.toString(s.getInt("account_id", 0));
                                        String address = "http://tbcarephp.azurewebsites.net/decline_patient.php";
                                        String[] value = {account_id, patient_id[position]};
                                        String[] valueName = {"partner_id", "patient_id"};
                                        WebServiceClass service = new WebServiceClass(address, value, valueName, Patient_RequestActivity.this, Patient_RequestActivity.this);
                                        service.execute();
                                    }
                                })
                                .setNegativeButton("DECLINE", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        SharedPreferences s = getSharedPreferences("session", 0);
                                        String account_id = Integer.toString(s.getInt("account_id", 0));
                                        String address = "http://tbcarephp.azurewebsites.net/decline_patient.php";
                                        String[] value = {account_id, patient_id[position]};
                                        String[] valueName = {"partner_id", "patient_id"};
                                        new PartnerAction(address, value, valueName).execute();
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
        String pid[];
        String patientsdisease[];

        MyAdapter(Context c, String id[], String patientsdisease[]) {
            super(c, R.layout.row_listview, R.id.linearLayoutID, id);
            this.context = c;
            this.pid = id;
            this.patientsdisease = patientsdisease;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertview, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_listview, parent, false);
            TextView ids = row.findViewById(R.id.txtid);
            TextView names = row.findViewById(R.id.tp_name);

            ids.setText(pid[position]);
            names.setText(patientsdisease[position]);


            return row;
        }

    }



    class PartnerAction extends AsyncTask {

        private String Address;
        private String[] Value;
        private String[] ValueName;
        private org.json.JSONArray RecordResult;
        private ProgressDialog progressDialog;
        private boolean flag = true;


        public PartnerAction(String address, String[] value, String[] valueName)
        {
            Address = address;
            Value = value;
            ValueName = valueName;
            RecordResult = new org.json.JSONArray();
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
