package com.example.android.tbcare_capstone;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ChoosePartner extends AppCompatActivity implements WebServiceClass.Listener {


    String[] pid;
    String[] pname;
    String[] patientshandled;
    String[] account_Pid;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosepartner);

        String address = "http://tbcarephp.azurewebsites.net/retrieve_partnerList.php";
        String[] value = {};
        String[] valueName = {};
        WebServiceClass wbc = new WebServiceClass(address, value, valueName, ChoosePartner.this, ChoosePartner.this);

        wbc.execute();

    }

    @Override
    public void OnTaskCompleted(JSONArray Result, boolean flag) {

        pid = new String[Result.length()];
        pname = new String[Result.length()];
        patientshandled = new String[Result.length()];
        account_Pid = new String[Result.length()];
        if(flag)
        {
            if(Result != null)
            {
                for(int i = 0; i < Result.length(); i++)
                {
                    try {

                        JSONObject object = Result.getJSONObject(i);
                        pid[i] = object.getString("TP_ID");
                        pname[i] = object.getString("Firstname") +" "+ object.getString("Lastname");
                        patientshandled[i] = object.getString("no_patients");
                        account_Pid[i] = object.getString("ID");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                lv = findViewById(R.id.listview1);
                ChoosePartner.MyAdapter adapter = new ChoosePartner.MyAdapter(this, pid, pname, patientshandled);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChoosePartner.this);
                        builder.setMessage("Do you want to pick "+pname[position]+" as your partner?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        SharedPreferences s = getSharedPreferences("session", 0);
                                        String account_id = Integer.toString(s.getInt("account_id", 0));
                                        String address = "http://tbcarephp.azurewebsites.net/set_partner.php";
                                        String[] value = {account_id, account_Pid[position]};
                                        String[] valueName = {"patient_id", "partner_id"};
                                        new FinalizePartner(address, value, valueName).execute();
                                    }
                                })
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
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
        String[] pname;
        String[] patientshandled;

        MyAdapter(Context c, String id[], String name[], String[] patientshandled) {
            super(c, R.layout.choosepartner_listview, R.id.linearLayoutID, id);
            this.context = c;
            this.pid = id;
            this.pname = name;
            this.patientshandled = patientshandled;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertview, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.choosepartner_listview, parent, false);
            TextView ids = row.findViewById(R.id.txtid);
            TextView names = row.findViewById(R.id.tp_name);
            TextView patientsHandled = row.findViewById(R.id.txtpatientshandled);

            ids.setText(pid[position]);
            names.setText(pname[position]);
            patientsHandled.setText(patientshandled[position]);



            return row;
        }

    }

    class FinalizePartner extends AsyncTask {

        private String Address;
        private String[] Value;
        private String[] ValueName;
        private org.json.JSONArray RecordResult;
        private ProgressDialog progressDialog;
        private boolean flag = true;


        public FinalizePartner(String address, String[] value, String[] valueName)
        {
            Address = address;
            Value = value;
            ValueName = valueName;
            RecordResult = new org.json.JSONArray();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ChoosePartner.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChoosePartner.this);
                        builder.setMessage("You have now selected a partner. Once the partner approves your request, you will receive your medication details.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(ChoosePartner.this, Menu_Patient.class);
                                        finish();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
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
